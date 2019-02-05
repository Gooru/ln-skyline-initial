package org.gooru.skylineinitial.infra.services;

import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.gooru.skylineinitial.infra.services.baselinedonehandler.ILPDoneInformer;
import org.gooru.skylineinitial.infra.services.queueoperators.ProcessingEligibilityVerifier;
import org.gooru.skylineinitial.infra.services.queueoperators.RequestDequeuer;
import org.gooru.skylineinitial.infra.services.settings.SettingsModel;
import org.gooru.skylineinitial.infra.services.subjectinferer.SubjectInferer;
import org.gooru.skylineinitial.infra.services.validators.ContextValidator;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// @formatter:off
/**
 *
 * - Start
 * - Before the processing is actually started, the flag in class member table for diag-done is set
 * - The processing is done and ILP is updated, using ILP update flow as outlined below
 *   - Read the queue record
 *   - parse the payload and map it to relevant command
 *   - if class is NOT offline and premium, and diagnostic is played
 *     - based on the command define on competency to be updated
 *     - Find out relevant competency in each domain
 *       - Arrange competency covered by diagnostic in each domain ordered by sequence id
 *       - Find maximum competency order for which all competency below are completed
 *       - Choose it as destination competency
 *     - In LPCS, find if user has any competency in that domain already mastered or completed
 *     - Take better of user's LPCS evidenced competency and relevant competency from diagnostic
 *     - Update LPCS (and other table)
 *   - else if class is offline and premium
 *     - Read student origin, average value
 *     - populate it as ILP using better algo with existing LP
 *   - dequeue record
 * - The class member table is updated with flag for ILP generation completed
 *
 * - End
 *
 * @author ashish.
 */
// @formatter:on

class QueueRecordProcessingServiceImpl implements QueueRecordProcessingService {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private SkylineInitialQueueModel model;
  private static final Logger LOGGER = LoggerFactory
      .getLogger(QueueRecordProcessingServiceImpl.class);
  private ProcessingContext context;

  QueueRecordProcessingServiceImpl(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public void processQueueRecord(SkylineInitialQueueModel model) {
    this.model = model;
    if (!ProcessingEligibilityVerifier.build(dbi4core)
        .isEligibleForProcessing(model)) {
      LOGGER.debug("Record is not found to be in dispatched state, may be processed already.");
      dequeueRecord();
      return;
    }
    processRecord();
  }

  private void dequeueRecord() {
    LOGGER.debug("Dequeueing record");
    RequestDequeuer.build(dbi4core).dequeue(model);
  }

  private void processRecord() {
    LOGGER.debug("Doing real processing");
    context = ProcessingContext.buildFromQueueModel(model);

    try {
      preprocess();
      process();
      doPostProcessing();
    } catch (Throwable e) {
      LOGGER.warn("Not able to do initial skyline for model: '{}'. Will dequeue record.",
          model.toJson(), e);
      throw new IllegalStateException(
          "Not able to do initial skyline for model: " + model.toJson(), e);
    } finally {
      dequeueRecord();
    }
  }

  private void process() {
    // TODO: Implement this
  }

  private void preprocess() {
    validate();
    initialize();
  }

  private void doPostProcessing() {
    ILPDoneInformer.build(dbi4core).inform(context);
  }

  private void validate() {
    ContextValidator.build(dbi4core, dbi4ds).validate(context);
  }

  private void initialize() {
    initializeSubject();
    initializeSettingsModel();
  }

  private void initializeSettingsModel() {
    SettingsModel model = SettingsModel.build(dbi4core, context.getClassId(), context.getUserId());
    context.setSettingsModel(model);
  }

  private void initializeSubject() {
    String subject = SubjectInferer.build(dbi4core).inferSubjectForClass(context.getClassId());
    context.setSubject(subject);
  }

}
