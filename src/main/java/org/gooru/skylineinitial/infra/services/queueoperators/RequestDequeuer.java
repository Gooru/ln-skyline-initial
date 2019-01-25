package org.gooru.skylineinitial.infra.services.queueoperators;

import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface RequestDequeuer {

  void dequeue(SkylineInitialQueueModel model);

  static RequestDequeuer build(DBI dbi) {
    return new RequestDequeuerImpl(dbi);
  }

  static RequestDequeuer build() {
    return new RequestDequeuerImpl(DBICreator.getDbiForDefaultDS());
  }

}
