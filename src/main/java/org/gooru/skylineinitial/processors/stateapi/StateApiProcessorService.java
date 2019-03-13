package org.gooru.skylineinitial.processors.stateapi;

import java.util.UUID;
import org.gooru.skylineinitial.infra.data.StudentDiagnosticState;
import org.gooru.skylineinitial.infra.services.diagnosticapplicable.DiagnosticApplicabilityService;
import org.gooru.skylineinitial.infra.services.diagnosticfetcher.DiagnosticFetcherService;
import org.gooru.skylineinitial.infra.services.settings.SettingsModel;
import org.gooru.skylineinitial.infra.services.stateupdater.StudentStateUpdaterService;
import org.gooru.skylineinitial.infra.services.stateverifier.ClassAndStudentStateVerifierService;
import org.gooru.skylineinitial.processors.stateapi.StateApiResponse.StateApiResponseBuilder;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// @formatter:off
/**
 * Following logic is encapsulated :
 * - State API
 *   - Is class non navigator
 *       - Show course map. Done
 *   - Is class setup (and student specific setup) complete
 *   - If yes, then
 *       - Is baseline available
 *       - If yes
 *           - Show course map. Done
 *       - Else check state of diagnostic
 *           - If state == diag-suggested
 *               - Take specified diagnostic. Done
 *           - if state == diag-not-needed
 *               - Show directions. Done
 *           - If state == diag-done || state == diag-not-available
 *               - if initial lp state == done
 *                   - Show directions. Done
 *               - Else
 *                   - Wait for ILP being done. Done
 *           - if state == null
 *               - Is diagnostic needed
 *               - If yes
 *                   - find diagnostic
 *                   - if found
 *                       - update state to diag-suggested
 *                       - Do specified diagnostic. Done
 *                   - else
 *                       - update state to diag-not-available
 *                       - update flag of ILP done to true
 *                       - Show directions. Done.
 *               - Else
 *                   - update state to diag-not-needed
 *                   - Show directions. Done
 *   - Else
 *       - Provide state as class setup pending from teacher side. Done
 *
 * @author ashish.
 */
// @formatter:on

class StateApiProcessorService {

  private final DBI dbi4core;
  private final DBI dbiForDsdbDS;
  private StateApiProcessorCommand command;
  private static final Logger LOGGER = LoggerFactory.getLogger(StateApiProcessorService.class);
  private SettingsModel model;
  private ClassAndStudentStateVerifierService classAndStudentStateVerifierService;

  StateApiProcessorService(DBI dbi4core, DBI dbiForDsdbDS) {
    this.dbi4core = dbi4core;
    this.dbiForDsdbDS = dbiForDsdbDS;
  }

  StateApiResponse calculateAndFetchState(StateApiProcessorCommand command) {
    this.command = command;
    LOGGER.info("Command is : {}", command.toString());
    initialize();

    if (classAndStudentStateVerifierService.isClassNonNavigator()) {
      LOGGER.debug("Non navigator class, will show course map");
      return StateApiResponse.StateApiResponseBuilder.buildForShowCourseMap();
    }

    if (!classAndStudentStateVerifierService.isClassSetupDone()
        || !classAndStudentStateVerifierService.isStudentSetupDone()
        || !classAndStudentStateVerifierService.isCourseSetupDone()) {
      LOGGER.debug("Class/student/course setup is not completed yet");
      return StateApiResponseBuilder.buildForClassSetupIncomplete();
    }

    if (classAndStudentStateVerifierService.isStudentBaselineDone()) {
      LOGGER.debug("Student baseline is done, will show course map");
      return StateApiResponseBuilder.buildForShowCourseMap();
    }

    if (model.isClassSetupToForceCalculateILP()) {
      LOGGER.debug("Will handle force calculate fo class flow");
      return handleClassForceCalculate();
    } else {
      LOGGER.debug("Will handle online class flow");
      return handleNonForceCalculateClass();
    }
  }

  private StateApiResponse handleNonForceCalculateClass() {
    StudentDiagnosticState studentDiagnosticState = model.getStudentDiagnosticState();
    switch (studentDiagnosticState) {
      case SUGGESTED:
        LOGGER.debug("Current diagnostic state is suggested, will ask to play it");
        return StateApiResponseBuilder.buildForDiagnosticPlay(model.getDiagnosticAssessmentId());
      case NOT_NEEDED:
        LOGGER.debug("Current diagnostic state is not needed, will show directions");
        return StateApiResponseBuilder.buildForShowDirections();
      case DONE:
      case NOT_AVAILABLE:
      case FORCE_CALCULATE:
        LOGGER.debug("Current state is done/not available/force calculate. Will check of ILP");
        if (classAndStudentStateVerifierService.isStudentILPDone()) {
          // FORCE_CALCULATE state should always end here
          LOGGER.debug("Student ILP is done, will show directions");
          return StateApiResponseBuilder.buildForShowDirections();
        } else if (studentDiagnosticState != StudentDiagnosticState.FORCE_CALCULATE) {
          LOGGER.debug("Student ILP is not done, ask user to wait while it is being done");
          return StateApiResponseBuilder.buildForILPInProgress();
        } else {
          LOGGER.warn(
              "State is set to force calculated, while ILP is not done yet. Class: '{}', user: '{}'",
              command.getClassId(), command.getUserId());
          throw new IllegalStateException(
              "State is set to force calculated, while ILP is not done yet");
        }
      case NOT_INITIALIZED:
        LOGGER.debug("Current diagnostic state is uninitialized, will handle accordingly");
        return handleNonInitializedDiagnosticStateForOnlineClasses();
      default:
        int state = studentDiagnosticState != null ? studentDiagnosticState.getValue() : -1;
        throw new IllegalStateException("Not sure how to deal with this state: " + state);
    }
  }

  private StateApiResponse handleNonInitializedDiagnosticStateForOnlineClasses() {
    DiagnosticApplicabilityService diagnosticApplicabilityService = DiagnosticApplicabilityService
        .build(dbiForDsdbDS, model);
    StudentStateUpdaterService studentStateUpdaterService = StudentStateUpdaterService
        .build(dbi4core, model);

    if (diagnosticApplicabilityService.isDiagnosticApplicable()) {
      LOGGER.debug("Diagnostic is applicable for student");
      DiagnosticFetcherService diagnosticFetcherService = DiagnosticFetcherService
          .build(dbi4core, model);
      UUID diagnosticAssessmentId = diagnosticFetcherService.fetchDiagnosticAssessment();
      if (diagnosticAssessmentId != null) {
        LOGGER.debug("Found diagnostic assessment. Will ask for play.");
        studentStateUpdaterService.updateStateToDiagnosticSuggested(diagnosticAssessmentId);
        return StateApiResponseBuilder.buildForDiagnosticPlay(diagnosticAssessmentId);
      } else {
        LOGGER.debug("Diagnostic assessment not found. Will show directions");
        studentStateUpdaterService.updateStateToDiagnosticNotAvailable();
        return StateApiResponseBuilder.buildForShowDirections();
      }
    } else {
      LOGGER.debug("Diagnostic is not needed for student. Will show directions.");
      studentStateUpdaterService.updateStateToDiagnosticNotNeeded();
      return StateApiResponseBuilder.buildForShowDirections();
    }
  }

  private StateApiResponse handleClassForceCalculate() {
    if (classAndStudentStateVerifierService.isStudentBaselineDone()) {
      LOGGER.debug("Student baseline done, will show course map");
      return StateApiResponseBuilder.buildForShowCourseMap();
    } else if (classAndStudentStateVerifierService.isStudentILPDone()) {
      LOGGER.debug("Student ILP done, will show directions");
      return StateApiResponseBuilder.buildForShowDirections();
    } else {
      LOGGER
          .warn("Teacher need to trigger ILP for student: '{}' in class: '{}'", command.getUserId(),
              command.getClassId());
      return StateApiResponseBuilder.buildForClassSetupIncomplete();
    }
  }

  private void initialize() {
    model = SettingsModel.build(dbi4core, command.getClassId(), command.getUserId());
    if (model == null) {
      LOGGER
          .warn("Class/Course/Student invalid. Class: '{}' and Student: '{}'", command.getClassId(),
              command.getUserId());
      throw new IllegalArgumentException("Invalid class/course/student specified");
    }
    classAndStudentStateVerifierService = ClassAndStudentStateVerifierService.build(model);
  }
}
