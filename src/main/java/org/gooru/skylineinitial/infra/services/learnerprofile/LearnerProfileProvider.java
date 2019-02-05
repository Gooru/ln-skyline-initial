package org.gooru.skylineinitial.infra.services.learnerprofile;

import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface LearnerProfileProvider {

  CompetencyLine findLearnerProfileForUser(ProcessingContext context);

  static LearnerProfileProvider build(DBI dbi4ds) {
    return new LearnerProfileProviderImpl(dbi4ds);
  }

  static LearnerProfileProvider build() {
    return new LearnerProfileProviderImpl(DBICreator.getDbiForDsdbDS());
  }

}
