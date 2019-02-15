package org.gooru.skylineinitial.infra.services.ilpcalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.services.algebra.competency.Competency;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyMap;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencySelectorStrategy;
import org.gooru.skylineinitial.infra.services.learnerprofile.LearnerProfileProvider;
import org.gooru.skylineinitial.infra.utils.CollectionUtils;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class IlpCalculatorServiceForDiagnosticPlayed implements IlpCalculatorService {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private final ProcessingContext context;
  private DiagnosticDao diagnosticDao;
  private CompetencyDao competencyDao;
  private List<String> gutCodesAttempted;
  private List<String> gutCodesCompleted;

  IlpCalculatorServiceForDiagnosticPlayed(DBI dbi4core, DBI dbi4ds,
      ProcessingContext context) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
    this.context = context;
  }

  @Override
  public CompetencyLine calculateCompetenciesCompleted() {
    // Get competencies list for questions
    List<QuestionIdGutCodeTuple> questionIdGutCodeTuples = fetchDiagnosticDao()
        .selectQuestionIdGutCodeTuplesForSpecifiedDiagnostic(
            context.getDiagnosticAssessmentPlayedCommand().getAssessmentId(),
            context.getSettingsModel().getStudentGradeLowerBound());

    initializeDiagnosticResult(questionIdGutCodeTuples);

    // Accumulate all competencies from Gut codes
    List<Competency> competenciesAttempted = fetchCompetencyDao()
        .transformGutCodesToCompetency(context.getSubject(),
            CollectionUtils.convertToSqlArrayOfString(gutCodesAttempted));

    // Create competency map
    CompetencyMap competencyMap = CompetencyMap.build(competenciesAttempted);

    // Create Competency selector strategy
    CompetencySelectorStrategy strategy = ContiguousCorrectMaximumCompetencySelectorStrategy
        .build(gutCodesCompleted);

    // Get algorithm specific line
    CompetencyLine diagnosticResultLine = competencyMap.getSelectedLine(strategy);

    // Get LP specific line for specified user and subject
    CompetencyLine learnerProfileLine = LearnerProfileProvider.build(dbi4ds)
        .findLearnerProfileForUser(context);

    // Merge these two lines using "better one selected" algorithm
    return learnerProfileLine.merge(diagnosticResultLine, true);
  }

  private void initializeDiagnosticResult(List<QuestionIdGutCodeTuple> questionIdGutCodeTuples) {
    if (questionIdGutCodeTuples == null || questionIdGutCodeTuples.isEmpty()) {
      throw new IllegalStateException("No questions found in diagnostic assessment: " + context
          .getDiagnosticAssessmentPlayedCommand().getAssessmentId());
    }
    gutCodesAttempted = new ArrayList<>(questionIdGutCodeTuples.size());
    gutCodesCompleted = new ArrayList<>(questionIdGutCodeTuples.size());
    List<UUID> questionsCorrectlyAnswered = context.getDiagnosticAssessmentPlayedCommand()
        .getQuestions();

    for (QuestionIdGutCodeTuple questionIdGutCodeTuple : questionIdGutCodeTuples) {
      gutCodesAttempted.add(questionIdGutCodeTuple.getGutCode());
      if (questionsCorrectlyAnswered.contains(questionIdGutCodeTuple.getQuestionId())) {
        gutCodesCompleted.add(questionIdGutCodeTuple.getGutCode());
      }
    }
  }

  private DiagnosticDao fetchDiagnosticDao() {
    if (diagnosticDao == null) {
      diagnosticDao = dbi4core.onDemand(DiagnosticDao.class);
    }
    return diagnosticDao;
  }

  private CompetencyDao fetchCompetencyDao() {
    if (competencyDao == null) {
      competencyDao = dbi4ds.onDemand(CompetencyDao.class);
    }
    return competencyDao;
  }
}
