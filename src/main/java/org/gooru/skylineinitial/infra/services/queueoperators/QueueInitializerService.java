package org.gooru.skylineinitial.infra.services.queueoperators;

import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * This service will be used once at the start of application. This service will mark all the record
 * in DB queue which are marked as either dispatched or in process, to queued state. This is to
 * handle cases where some records were being processed while the system shut down, and thus those
 * record need to be reprocessed.
 *
 * @author ashish
 */
public interface QueueInitializerService {

  void initializeQueue();

  static QueueInitializerService build() {
    return new QueueInitializerServiceImpl(DBICreator.getDbiForDefaultDS());
  }

  static QueueInitializerService build(DBI dbi) {
    return new QueueInitializerServiceImpl(dbi);
  }

}
