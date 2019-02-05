package org.gooru.skylineinitial.infra.services.baselinedonehandler;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author ashish.
 */

interface ILPDoneInformerDao {

  @SqlUpdate("update class_member set initial_lp_done = true, diag_asmt_state = :state where "
      + " class_id = :classId::uuid and user_id = :userId::uuid")
  void updateDoneStatusForClassMember(@Bind("classId") String classId,
      @Bind("userId") String userId, @Bind("state") Integer state);
}
