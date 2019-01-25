package org.gooru.skylineinitial.infra.services.queueoperators;

import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class RequestDequeuerImpl implements RequestDequeuer {

  private final DBI dbi;
  private RequestDequeuerDao dao;

  RequestDequeuerImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public void dequeue(SkylineInitialQueueModel model) {
    fetchDao().dequeueRecord(model.getId());
  }

  private RequestDequeuerDao fetchDao() {
    if (dao == null) {
      dao = dbi.onDemand(RequestDequeuerDao.class);
    }
    return dao;
  }
}
