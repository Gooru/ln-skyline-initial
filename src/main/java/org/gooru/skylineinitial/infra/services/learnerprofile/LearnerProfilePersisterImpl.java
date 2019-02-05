package org.gooru.skylineinitial.infra.services.learnerprofile;

import java.util.UUID;
import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class LearnerProfilePersisterImpl implements LearnerProfilePersister {

  private final DBI dbi4ds;
  private final ProcessingContext context;

  LearnerProfilePersisterImpl(DBI dbi4ds, ProcessingContext context) {
    this.dbi4ds = dbi4ds;
    this.context = context;
  }

  @Override
  public void persistLearnerProfile(CompetencyLine skyline, UUID userId) {
    // TODO: Implement this
  }
}
