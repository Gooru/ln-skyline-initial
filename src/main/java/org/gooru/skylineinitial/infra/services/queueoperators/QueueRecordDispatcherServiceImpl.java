package org.gooru.skylineinitial.infra.services.queueoperators;

import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish
 */
class QueueRecordDispatcherServiceImpl implements
    QueueRecordDispatcherService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(QueueRecordDispatcherService.class);
  private final DBI dbi;

  QueueRecordDispatcherServiceImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public SkylineInitialQueueModel getNextRecordToDispatch() {
    QueueOperatorDao dao = dbi.onDemand(QueueOperatorDao.class);
    SkylineInitialQueueModel model = dao.getNextDispatchableModel();
    if (model == null) {
      model = SkylineInitialQueueModel.createNonPersistedEmptyModel();
    } else {
      dao.setQueuedRecordStatusAsDispatched(model.getId());
    }
    return model;
  }
}
