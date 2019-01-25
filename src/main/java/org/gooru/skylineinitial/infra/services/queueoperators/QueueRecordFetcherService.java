package org.gooru.skylineinitial.infra.services.queueoperators;

import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface QueueRecordFetcherService {

  SkylineInitialQueueModel fetchRecordById(Long id);

  static QueueRecordFetcherService build() {
    return new QueueRecordFetcherServiceImpl(DBICreator.getDbiForDefaultDS());
  }

  static QueueRecordFetcherService build(DBI dbi) {
    return new QueueRecordFetcherServiceImpl(dbi);
  }


}
