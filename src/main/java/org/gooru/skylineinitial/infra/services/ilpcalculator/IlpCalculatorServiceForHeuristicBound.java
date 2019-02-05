package org.gooru.skylineinitial.infra.services.ilpcalculator;

import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.gooru.skylineinitial.infra.services.classsetting.GradeBoundFinder;
import org.gooru.skylineinitial.infra.services.learnerprofile.LearnerProfileProvider;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class IlpCalculatorServiceForHeuristicBound implements IlpCalculatorService {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private final ProcessingContext context;

  IlpCalculatorServiceForHeuristicBound(DBI dbi4core, DBI dbi4ds,
      ProcessingContext context) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
    this.context = context;
  }

  @Override
  public CompetencyLine calculateCompetenciesCompleted() {
    // Fetch average line
    CompetencyLine lowGradeLine = GradeBoundFinder.build(dbi4ds)
        .findAverageLineForGrade(context.getSettingsModel().getStudentGradeLowerBound());
    // fetch learner profile
    CompetencyLine learnerProfileLine = LearnerProfileProvider.build(dbi4ds)
        .findLearnerProfileForUser(context);
    // merge these two lines

    return learnerProfileLine.merge(lowGradeLine, true);
  }
}
