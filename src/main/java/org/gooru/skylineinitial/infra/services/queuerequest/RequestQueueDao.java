package org.gooru.skylineinitial.infra.services.queuerequest;

import java.util.List;
import java.util.UUID;
import org.gooru.skylineinitial.infra.data.SkylineInitialQueueModel;
import org.gooru.skylineinitial.infra.jdbi.PGArray;
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

  @SqlQuery("select exists (select 1 from class where id = :classId and is_deleted = false and is_archived = false)")
  boolean isClassNotDeletedAndNotArchived(@Bind("classId") UUID classId);

  @Mapper(UUIDMapper.class)
  @SqlQuery("select user_id from class_member where class_id = :classId and user_id is not null and is_active = true")
  List<UUID> fetchMembersOfClass(@Bind("classId") UUID classId);

  @Mapper(UUIDMapper.class)
  @SqlQuery("select user_id from class_member where class_id = :classId and user_id = any(:usersList) and is_active = true")
  List<UUID> fetchSpecifiedMembersOfClass(@Bind("classId") UUID classId,
      @Bind("usersList") PGArray<UUID> members);

  @Mapper(UUIDMapper.class)
  @SqlQuery("select course_id from class where id = :classId")
  UUID fetchCourseForClass(@Bind("classId") UUID classId);

  @SqlQuery("select exists(select 1 from course where id = :courseId and is_deleted = false)")
  boolean isCourseNotDeleted(@Bind("courseId") UUID courseId);

  @SqlUpdate(
      "insert into skyline_initial_queue(user_id, course_id, class_id, category, status, payload) values (:userId, :courseId,"
          + " :classId, :category, :status, :payload) ON CONFLICT DO NOTHING")
  void queueRequest(@BindBean SkylineInitialQueueModel model);

  @SqlBatch(
      "insert into skyline_initial_queue(user_id, course_id, class_id, category, status) values (:members, :courseId,"
          + " :classId, :category, :status) ON CONFLICT DO NOTHING")
  void queueRequests(@Bind("members") List<UUID> userId, @BindBean SkylineInitialQueueModel model);


}
