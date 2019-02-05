package org.gooru.skylineinitial.infra.services.queueoperators;

import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */

interface ProcessingEligibilityVerifierDao {

  @SqlQuery(
      "select exists (select 1 from class_member where user_id = :userId and "
          + " class_id = :classId and initial_lp_done = true)")
  boolean ilpAlreadyDoneForUser(@BindBean SkylineInitialQueueModel model);

  @SqlQuery("select exists (select 1 from skyline_initial_queue where id = :id and status = 1)")
  boolean isQueuedRecordStillDispatched(@Bind("id") Long id);
}
