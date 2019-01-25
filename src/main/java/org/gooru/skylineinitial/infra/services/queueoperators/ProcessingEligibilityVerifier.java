package org.gooru.skylineinitial.infra.services.queueoperators;

import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface ProcessingEligibilityVerifier {

  boolean isEligibleForProcessing(SkylineInitialQueueModel model);

  static ProcessingEligibilityVerifier build() {
    return new ProcessingEligibilityVerifierImpl(DBICreator.getDbiForDefaultDS(),
        DBICreator.getDbiForDsdbDS());
  }

  static ProcessingEligibilityVerifier build(DBI dbi4core, DBI dbi4ds) {
    return new ProcessingEligibilityVerifierImpl(dbi4core, dbi4ds);
  }

}
