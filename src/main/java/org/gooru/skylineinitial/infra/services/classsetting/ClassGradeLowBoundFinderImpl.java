package org.gooru.skylineinitial.infra.services.classsetting;

import java.util.List;
import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.services.algebra.competency.Competency;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyAlgebraDefaultBuilder;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyMap;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class ClassGradeLowBoundFinderImpl implements ClassGradeLowBoundFinder {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private Long gradeLowerBoundInClass;
  private ClassGradeLowBoundFinderDao dao4core;
  private ClassGradeLowBoundFinderDao dao4ds;
  private ProcessingContext context;
  private static final Logger LOGGER = LoggerFactory.getLogger(ClassGradeLowBoundFinderImpl.class);

  ClassGradeLowBoundFinderImpl(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public CompetencyLine findLowGradeForClassMember(ProcessingContext context) {
    this.context = context;
    if (context.isILExperience()) {
      return CompetencyAlgebraDefaultBuilder.getEmptyCompetencyLine();
    }

    initializeGradeLowerBoundForClass();
    if (gradeLowerBoundInClass == null) {
      LOGGER.info("Grade lower bound not found for class: '{}'. Return with empty low grade line ",
          context.getClassId());
      return CompetencyAlgebraDefaultBuilder.getEmptyCompetencyLine();
    }
    validateGradeWithMaster();

    return earthlineForGradeAndSubject();
  }

  private CompetencyLine earthlineForGradeAndSubject() {
    List<Competency> earthLineCompetencies = fetchDao4DS()
        .fetchCompetenciesForEarthlineForGradeAndSubject(context.getSubject(),
            gradeLowerBoundInClass);
    if (earthLineCompetencies != null && !earthLineCompetencies.isEmpty()) {
      return CompetencyMap.build(earthLineCompetencies).getCeilingLine();
    }
    return CompetencyAlgebraDefaultBuilder.getEmptyCompetencyLine();
  }

  private void validateGradeWithMaster() {
    if (!fetchDao4Core().validateGrade(gradeLowerBoundInClass)) {
      throw new IllegalArgumentException(
          "Invalid grade specified in class lower bound. Class: " + context.getClassId()
              + "  Grade: " + gradeLowerBoundInClass);
    }
  }

  private void initializeGradeLowerBoundForClass() {
    gradeLowerBoundInClass = fetchDao4Core()
        .fetchGradeLowerBoundForSpecifiedClassMember(context.getClassId(), context.getUserId());
    if (gradeLowerBoundInClass == null) {
      gradeLowerBoundInClass = fetchDao4Core()
          .fetchGradeLowerBoundForSpecifiedClass(context.getClassId());
    }
  }

  private ClassGradeLowBoundFinderDao fetchDao4Core() {
    if (dao4core == null) {
      dao4core = dbi4core.onDemand(ClassGradeLowBoundFinderDao.class);
    }
    return dao4core;
  }

  private ClassGradeLowBoundFinderDao fetchDao4DS() {
    if (dao4ds == null) {
      dao4ds = dbi4ds.onDemand(ClassGradeLowBoundFinderDao.class);
    }
    return dao4ds;
  }

}
