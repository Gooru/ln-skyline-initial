package org.gooru.skylineinitial.infra.services.classsetting;

import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface ClassGradeLowBoundFinder {

  CompetencyLine findLowGradeForClassMember(ProcessingContext context);

  static ClassGradeLowBoundFinder build(DBI dbi4core, DBI dbi4ds) {
    return new ClassGradeLowBoundFinderImpl(dbi4core, dbi4ds);
  }

  static ClassGradeLowBoundFinder build() {
    return new ClassGradeLowBoundFinderImpl(DBICreator.getDbiForDefaultDS(),
        DBICreator.getDbiForDsdbDS());
  }

}
