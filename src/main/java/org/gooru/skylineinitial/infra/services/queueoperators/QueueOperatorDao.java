package org.gooru.skylineinitial.infra.services.queueoperators;

import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel.SkylineInitialQueueModelMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

interface QueueOperatorDao {

  @SqlUpdate("update skyline_initial_queue set status = 0 where status != 0")
  void initializeQueueStatus();

  @Mapper(SkylineInitialQueueModelMapper.class)
  @SqlQuery(
      "select id, user_id, course_id, class_id, category, status, payload from skyline_initial_queue where status = 0 order by"
          + " id asc limit 1")
  SkylineInitialQueueModel getNextDispatchableModel();

  @SqlUpdate("update skyline_initial_queue set status = 1 where id = :modelId")
  void setQueuedRecordStatusAsDispatched(@Bind("modelId") Long id);

  @Mapper(SkylineInitialQueueModelMapper.class)
  @SqlQuery(
      "select id, user_id, course_id, class_id, category, status, payload from skyline_initial_queue where id = :id")
  SkylineInitialQueueModel fetchSpecifiedRecord(@Bind("id") Long id);
}
