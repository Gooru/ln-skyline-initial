package org.gooru.skylineinitial.infra.services.queuerequest;

import java.util.List;
import java.util.UUID;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * Module to queue the request for initial skyline for offline classes which are associated
 * with premium course. The specified validations are not enforced in this module, it is left to
 * caller
 *
 * @author ashish
 */
public interface RequestQueueService {

  /*
   * It is assumed that caller has verified class and membership being valid and course being premium
   */
  void enqueueForOfflineClassWithPremiunCourse(UUID classId, List<UUID> users);

  static RequestQueueService build() {
    return new RequestQueueServiceImpl(DBICreator.getDbiForDefaultDS());
  }

  static RequestQueueService build(DBI dbi) {
    return new RequestQueueServiceImpl(dbi);
  }

}
