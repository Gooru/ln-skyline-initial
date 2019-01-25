package org.gooru.skylineinitial.infra.services.queuerequest;

import java.util.List;
import java.util.UUID;
import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish
 */
public interface RequestQueueService {

  void enqueue(SkylineInitialQueueModel model);

  void enqueueForSpecifiedUsers(SkylineInitialQueueModel model, List<UUID> users);

  void enqueueForWholeClass(SkylineInitialQueueModel model);

  static RequestQueueService build() {
    return new RequestQueueServiceImpl(DBICreator.getDbiForDefaultDS());
  }

  static RequestQueueService build(DBI dbi) {
    return new RequestQueueServiceImpl(dbi);
  }

}
