package org.gooru.skylineinitial.infra.services.queuerequest;

import java.util.List;
import java.util.UUID;
import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish
 */
class RequestQueueServiceImpl implements RequestQueueService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestQueueService.class);
  private final DBI dbi;
  private SkylineInitialQueueModel model;
  private RequestQueueDao queueDao;

  RequestQueueServiceImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public void enqueue(SkylineInitialQueueModel model) {
    this.model = model;
    queueDao = dbi.onDemand(RequestQueueDao.class);
    if (model.getClassId() != null) {
      doQueueingForClass();
    } else {
      doQueueingForIL();
    }

  }

  @Override
  public void enqueueForSpecifiedUsers(SkylineInitialQueueModel model, List<UUID> users) {
    // TODO: Implement this
    throw new AssertionError("Not implemented");
  }

  @Override
  public void enqueueForWholeClass(SkylineInitialQueueModel model) {
    // TODO: Implement this
    throw new AssertionError("Not implemented");
  }

  private void doQueueingForIL() {
    if (model.getCourseId() == null) {
      LOGGER.warn("Initial skyline queue request fired for IL without courseId. Abort.");
      return;
    }
    if (!queueDao.isCourseNotDeleted(model.getCourseId())) {
      LOGGER.warn("Initial skyline fired for deleted or not existing course: '{}'. Abort.",
          model.getCourseId());
      return;
    }
    queueInDb();
  }

  private void doQueueingForClass() {
    if (!queueDao.isClassNotDeletedAndNotArchived(model.getClassId())) {
      LOGGER
          .warn("Initial skyline fired for archived or deleted class: '{}'", model.getClassId());
      return;
    }
    UUID courseId = queueDao.fetchCourseForClass(model.getClassId());
    if (courseId == null) {
      LOGGER.warn("No course associated with class: '{}'. ", model.getClassId());
      return;
    }

    if (model.getCourseId() != null && !model.getCourseId().equals(courseId)) {
      LOGGER.warn(
          "Course specified in request '{}' does not match course associated with class '{}'. Will use "
              + "the one associated with class", model.getCourseId(), courseId);
    }

    queueInDb();
  }

  private void queueInDb() {
    queueDao.queueRequest(model);
  }

}
