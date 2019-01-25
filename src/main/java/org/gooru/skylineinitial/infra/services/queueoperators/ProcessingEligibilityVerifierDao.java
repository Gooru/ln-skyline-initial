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
      "select exists (select 1 from learner_profile_baselined where user_id = :userId::text and "
          + " course_id = :courseId::text  and class_id is null)")
  boolean profileBaselineDoneForUserInIL(@BindBean SkylineInitialQueueModel model);

  @SqlQuery(
      "select exists (select 1 from learner_profile_baselined where user_id = :userId::text and "
          + "course_id = :courseId::text  and class_id = :classId::text)")
  boolean profileBaselineDoneForUserInClass(@BindBean SkylineInitialQueueModel model);

  @SqlQuery("select exists (select 1 from profile_baseline_queue where id = :id and status = 1)")
  boolean isQueuedRecordStillDispatched(@Bind("id") Long id);
}
