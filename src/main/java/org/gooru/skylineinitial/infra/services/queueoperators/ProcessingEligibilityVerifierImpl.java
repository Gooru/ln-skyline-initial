package org.gooru.skylineinitial.infra.services.queueoperators;

import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class ProcessingEligibilityVerifierImpl implements
    ProcessingEligibilityVerifier {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(ProcessingEligibilityVerifierImpl.class);
  private SkylineInitialQueueModel model;
  private ProcessingEligibilityVerifierDao dao4core;
  private ProcessingEligibilityVerifierDao dao4ds;

  ProcessingEligibilityVerifierImpl(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public boolean isEligibleForProcessing(SkylineInitialQueueModel model) {
    this.model = model;
    if (!recordIsStillInDispatchedState()) {
      LOGGER.debug("Record is not found to be in dispatched state, may be processed already.");
      return false;
    }
    if (wasBaselineAlreadyDone()) {
      LOGGER.debug("Profile baseline was already done");
      return false;
    }

    return true;
  }


  private boolean wasBaselineAlreadyDone() {
    if (model.getClassId() == null) {
      return fetchDsDao().profileBaselineDoneForUserInIL(model);
    }
    return fetchDsDao().profileBaselineDoneForUserInClass(model);
  }

  private boolean recordIsStillInDispatchedState() {
    return fetchCoreDao().isQueuedRecordStillDispatched(model.getId());
  }

  private ProcessingEligibilityVerifierDao fetchCoreDao() {
    if (dao4core == null) {
      dao4core = dbi4core.onDemand(ProcessingEligibilityVerifierDao.class);
    }
    return dao4core;
  }

  private ProcessingEligibilityVerifierDao fetchDsDao() {
    if (dao4ds == null) {
      dao4ds = dbi4ds.onDemand(ProcessingEligibilityVerifierDao.class);
    }
    return dao4ds;
  }

}
