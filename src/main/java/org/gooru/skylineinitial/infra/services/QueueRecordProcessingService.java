package org.gooru.skylineinitial.infra.services;


import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish
 */

public interface QueueRecordProcessingService {

  void processQueueRecord(SkylineInitialQueueModel model);

  static QueueRecordProcessingService build() {
    return new QueueRecordProcessingServiceImpl(DBICreator.getDbiForDefaultDS(),
        DBICreator.getDbiForDsdbDS());
  }

  static QueueRecordProcessingService build(DBI dbi4core, DBI dbi4ds) {
    return new QueueRecordProcessingServiceImpl(dbi4core, dbi4ds);
  }

}
