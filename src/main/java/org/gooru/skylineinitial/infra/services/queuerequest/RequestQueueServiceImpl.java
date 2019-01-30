package org.gooru.skylineinitial.infra.services.queuerequest;

import java.util.List;
import java.util.UUID;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish
 */
class RequestQueueServiceImpl implements RequestQueueService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestQueueService.class);
  private final DBI dbi;
  private RequestQueueDao queueDao;
  private UUID classId;
  private UUID courseId;
  private List<UUID> users;

  RequestQueueServiceImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public void enqueueForOfflineClassWithPremiunCourse(UUID classId, List<UUID> users) {
    queueDao = dbi.onDemand(RequestQueueDao.class);
    this.classId = classId;
    this.users = users;
    doQueueingForClass();
  }

  private void doQueueingForClass() {
    courseId = queueDao.fetchCourseForClass(classId);
    if (courseId == null) {
      LOGGER.warn("No valid course associated with class: '{}'. ", classId);
      return;
    }
    queueInDb();
  }

  private void queueInDb() {
    queueDao.queueRequestsForOfflineClass(users, courseId, classId);
  }

}
