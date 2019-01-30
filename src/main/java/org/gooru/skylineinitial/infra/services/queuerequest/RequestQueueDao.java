package org.gooru.skylineinitial.infra.services.queuerequest;

import java.util.List;
import java.util.UUID;
import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.gooru.skylineinitial.infra.jdbi.UUIDMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish on 18/5/18.
 */
interface RequestQueueDao {


  @Mapper(UUIDMapper.class)
  @SqlQuery("select id from course where id = (select course_id from class where id = :classId) and is_deleted = false")
  UUID fetchCourseForClass(@Bind("classId") UUID classId);

  @SqlQuery("select exists(select 1 from course where id = :courseId and is_deleted = false)")
  boolean isCourseNotDeleted(@Bind("courseId") UUID courseId);

  @SqlBatch(
      "insert into skyline_initial_queue(user_id, course_id, class_id, category, status) values (:members, :courseId,"
          + " :classId, 1, 0) ON CONFLICT DO NOTHING")
  void queueRequestsForOfflineClass(@Bind("members") List<UUID> members,
      @Bind("courseId") UUID courseId, @Bind("classId") UUID classId);

}
