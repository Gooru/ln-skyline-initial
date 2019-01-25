package org.gooru.skylineinitial.infra.services.baselinedonehandler;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author ashish.
 */

interface BaselineDoneInformerDao {

  @SqlUpdate("update class_member set profile_baseline_done = true where class_id = :classId::uuid and user_id = :userId::uuid")
  void updateDoneStatusForClassMember(@Bind("classId") String classId,
      @Bind("userId") String userId);
}
