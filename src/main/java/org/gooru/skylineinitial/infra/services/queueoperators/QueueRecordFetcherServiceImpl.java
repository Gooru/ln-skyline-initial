package org.gooru.skylineinitial.infra.services.queueoperators;

import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class QueueRecordFetcherServiceImpl implements QueueRecordFetcherService {

  private final DBI dbi;

  QueueRecordFetcherServiceImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public SkylineInitialQueueModel fetchRecordById(Long id) {
    QueueOperatorDao dao = dbi.onDemand(QueueOperatorDao.class);
    return dao.fetchSpecifiedRecord(id);
  }
}
