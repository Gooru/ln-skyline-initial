package org.gooru.skylineinitial.infra.services.queueoperators;

import org.skife.jdbi.v2.DBI;

/**
 * @author ashish
 */
class QueueInitializerServiceImpl implements QueueInitializerService {

  private final DBI dbi;

  QueueInitializerServiceImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public void initializeQueue() {
    QueueOperatorDao dao = dbi.onDemand(QueueOperatorDao.class);
    dao.initializeQueueStatus();
  }
}
