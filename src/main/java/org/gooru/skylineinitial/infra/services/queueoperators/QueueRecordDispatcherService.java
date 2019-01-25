package org.gooru.skylineinitial.infra.services.queueoperators;

import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * This service is responsible to read the record from the queue and return to caller. Caller needs
 * to decides as to what they want to do with the record. This means updating the status of record
 * to indicate that they are being processed. However, fetching the record using this service will
 * mark the record for being dispatched.
 *
 * @author ashish
 */
public interface QueueRecordDispatcherService {

  SkylineInitialQueueModel getNextRecordToDispatch();

  static QueueRecordDispatcherService build() {
    return new QueueRecordDispatcherServiceImpl(DBICreator.getDbiForDefaultDS());
  }

  static QueueRecordDispatcherService build(DBI dbi) {
    return new QueueRecordDispatcherServiceImpl(dbi);
  }

}
