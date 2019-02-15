package org.gooru.skylineinitial.infra.services.learnerprofile;

import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface LearnerProfilePersister {

  void persistLearnerProfile(CompetencyLine skyline);

  static LearnerProfilePersister buildForDiagnosticPlay(DBI dbi4ds, ProcessingContext context) {
    return new LearnerProfilePersisterFromDiagnosticPlay(dbi4ds, context);
  }

  static LearnerProfilePersister buildForNonDiagnosticCase(DBI dbi4ds, ProcessingContext context) {
    return new LearnerProfilePersisterForNonDiagnosticCase(dbi4ds, context);
  }
}
