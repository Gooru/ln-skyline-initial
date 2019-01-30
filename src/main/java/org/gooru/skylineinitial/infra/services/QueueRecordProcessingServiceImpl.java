package org.gooru.skylineinitial.infra.services;

import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.gooru.skylineinitial.infra.services.baselinedonehandler.BaselineDoneInformer;
import org.gooru.skylineinitial.infra.services.classsetting.ClassGradeLowBoundFinder;
import org.gooru.skylineinitial.infra.services.learnerprofile.LearnerProfileProvider;
import org.gooru.skylineinitial.infra.services.queueoperators.ProcessingEligibilityVerifier;
import org.gooru.skylineinitial.infra.services.queueoperators.RequestDequeuer;
import org.gooru.skylineinitial.infra.services.subjectinferer.SubjectInferer;
import org.gooru.skylineinitial.infra.services.validators.ContextValidator;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Here is the algorithmic logic:
 * <li> Verify it baseline is not already done or is picked up by some other thread. If any of
 * these two cases, no action needed
 * <li> Validate the class/user/course combination for not being not deleted. If it is, no action
 * needed
 * <li> If class context is present, fetch the low grade line. Note that it could be null as well.
 * In IL case it will be null (or empty)
 * <li> Fetch the subject bucket associated with the course. If subject bucket is not present, no
 * action needed further
 * <li> Fetch the current learner profile for specified subject as LP line
 * <li> Now that there is LP line and low grade line, do a UNION to get the better of these two
 * <li> Persist the resultant line as LP baseline. Note that there is need to persist both the
 * master and details record
 * <li> Fetch the read API kind of response, with join of domain/competency/baseline path. Note
 * that this contains domain's name as well as sequence
 * <li> Create a JSON from resultant response
 * <li> Persist the specified JSON as cached value
 * <li> Send out a message for post processing
 *
 * @author ashish.
 */

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
    if (!ProcessingEligibilityVerifier.build(dbi4core, dbi4ds)
        .isEligibleForProcessing(model)) {
      LOGGER.debug("Record is not found to be in dispatched state, may be processed already.");
      dequeueRecord();
      return;
    }
    // TODO: Implement this
    // processRecord();
  }

  private void dequeueRecord() {
    LOGGER.debug("Dequeueing record");
    RequestDequeuer.build(dbi4core).dequeue(model);
  }

  private void processRecord() {
    LOGGER.debug("Doing real processing");
    context = ProcessingContext.buildFromQueueModel(model);

    try {
      validate();
      initializeSubject();
      // TODO: Implement this

      // fetch low grade
      CompetencyLine lowGradeLine = ClassGradeLowBoundFinder.build(dbi4core, dbi4ds)
          .findLowGradeForClassMember(context);
      // fetch learner profile
      CompetencyLine learnerProfileLine = LearnerProfileProvider.build(dbi4core, dbi4ds)
          .findLearnerProfileForUser(context);
      // merge these two lines
      CompetencyLine resultLine = learnerProfileLine.merge(lowGradeLine, true);

      // do whatever post processing needed
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

  private void doPostProcessing() {
    // TODO: Implement this
    BaselineDoneInformer.build(dbi4core).inform(context);
  }

  private void validate() {
    ContextValidator.build(dbi4core, dbi4ds).validate(context);
  }

  private void initializeSubject() {
    String subject = SubjectInferer.build(dbi4core).inferSubjectForCourse(context.getCourseId());
    context.setSubject(subject);
  }

}
