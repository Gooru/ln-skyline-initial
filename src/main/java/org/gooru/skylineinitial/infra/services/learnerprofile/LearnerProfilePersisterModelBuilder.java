package org.gooru.skylineinitial.infra.services.learnerprofile;

import java.util.ArrayList;
import java.util.List;
import org.gooru.skylineinitial.infra.data.DiagnosticAssessmentPlayedCommand;
import org.gooru.skylineinitial.infra.data.LearnerProfileSourceGenerator;
import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.services.algebra.competency.Competency;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.gooru.skylineinitial.infra.services.algebra.competency.DomainCode;
import org.gooru.skylineinitial.infra.utils.UuidUtils;

/**
 * @author ashish.
 */

final class LearnerProfilePersisterModelBuilder {

  private static final int STATUS_COMPLETED = 4;

  private LearnerProfilePersisterModelBuilder() {
    throw new AssertionError();
  }


  static LearnerProfilePersisterModel buildForDiagnostic(ProcessingContext context,
      CompetencyLine completedCompetencies) {
    DiagnosticAssessmentPlayedCommand command = context.getDiagnosticAssessmentPlayedCommand();

    LearnerProfilePersisterModel model = new LearnerProfilePersisterModel();

    model.setClassId(UuidUtils.uuidToString(command.getClassId()));
    model.setCollectionId(UuidUtils.uuidToString(command.getAssessmentId()));
    model.setCollectionScore(command.getScore());
    model.setLatestSessionId(UuidUtils.uuidToString(command.getSessionId()));
    model.setStatus(STATUS_COMPLETED);
    model.setSubjectCode(context.getSubject());
    model.setUserId(UuidUtils.uuidToString(command.getUserId()));
    model.setGutCodes(getCompetenciesFromCompetencyLine(completedCompetencies));
    model.setProfileSource(command.getProfileSource());
    return model;
  }

  static LearnerProfilePersisterModel buildForNonDiagnostic(ProcessingContext context,
      CompetencyLine completedCompetencies) {

    LearnerProfilePersisterModel model = new LearnerProfilePersisterModel();

    model.setClassId(UuidUtils.uuidToString(context.getClassId()));
    model.setCollectionId(null);
    model.setCollectionScore(null);
    model.setLatestSessionId(null);
    model.setStatus(STATUS_COMPLETED);
    model.setSubjectCode(context.getSubject());
    model.setUserId(UuidUtils.uuidToString(context.getUserId()));
    model.setGutCodes(getCompetenciesFromCompetencyLine(completedCompetencies));
    model.setProfileSource(
        LearnerProfileSourceGenerator.generateProfileSourceForForceCalculate(context.getClassId()));
    return model;
  }


  private static List<String> getCompetenciesFromCompetencyLine(
      CompetencyLine completedCompetencies) {
    List<String> completedGutCodes = new ArrayList<>();
    List<DomainCode> domains = completedCompetencies.getDomains();
    for (DomainCode domain : domains) {
      Competency competencyForDomain = completedCompetencies.getCompetencyForDomain(domain);
      if (competencyForDomain != null) {
        completedGutCodes.add(competencyForDomain.getCompetencyCode().getCode());
      }
    }
    return completedGutCodes;
  }


}
