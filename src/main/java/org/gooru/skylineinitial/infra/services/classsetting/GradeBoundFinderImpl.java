package org.gooru.skylineinitial.infra.services.classsetting;

import java.util.List;
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

class GradeBoundFinderImpl implements GradeBoundFinder {

  private final DBI dbi4ds;
  private GradeBoundFinderDao dao4ds;
  private static final Logger LOGGER = LoggerFactory.getLogger(GradeBoundFinderImpl.class);
  private Long gradeId;

  GradeBoundFinderImpl(DBI dbi4ds) {
    this.dbi4ds = dbi4ds;
  }

  @Override
  public CompetencyLine findAverageLineForGrade(Long gradeId) {
    this.gradeId = gradeId;
    return averageLineForGrade();
  }

  private CompetencyLine averageLineForGrade() {
    List<Competency> earthLineCompetencies = fetchDao4DS()
        .fetchCompetenciesForAveragelineForGrade(gradeId);
    if (earthLineCompetencies != null && !earthLineCompetencies.isEmpty()) {
      return CompetencyMap.build(earthLineCompetencies).getCeilingLine();
    }
    return CompetencyAlgebraDefaultBuilder.getEmptyCompetencyLine();
  }

  private GradeBoundFinderDao fetchDao4DS() {
    if (dao4ds == null) {
      dao4ds = dbi4ds.onDemand(GradeBoundFinderDao.class);
    }
    return dao4ds;
  }

}
