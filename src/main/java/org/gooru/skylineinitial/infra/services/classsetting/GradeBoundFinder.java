package org.gooru.skylineinitial.infra.services.classsetting;

import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface GradeBoundFinder {

  CompetencyLine findAverageLineForGrade(Long gradeId);

  static GradeBoundFinder build(DBI dbi4ds) {
    return new GradeBoundFinderImpl(dbi4ds);
  }

  static GradeBoundFinder build() {
    return new GradeBoundFinderImpl(DBICreator.getDbiForDsdbDS());
  }

}
