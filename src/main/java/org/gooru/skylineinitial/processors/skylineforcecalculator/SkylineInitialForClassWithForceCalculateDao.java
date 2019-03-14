package org.gooru.skylineinitial.processors.skylineforcecalculator;

import java.util.UUID;
import org.gooru.skylineinitial.infra.jdbi.PGArray;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */

interface SkylineInitialForClassWithForceCalculateDao {

  @SqlQuery(
      "select count(*) from class_member cm inner join class c on c.id = cm.class_id where cm.class_id = :classId "
          + " and cm.user_id = any(:users) and cm.is_active = true and cm.grade_lower_bound is not null "
          + " and c.is_archived = false and c.is_deleted = false and c.force_calculate_ilp = true"
          + "  and (c.creator_id = :teacherId OR collaborator ?? :teacherId::text)")
  int validatedCountForClassMembers(@Bind("classId") UUID classId,
      @Bind("users") PGArray<UUID> users, @Bind("teacherId") UUID teacherId);

  @SqlQuery("select exists (select 1 from course where id = "
      + " (select course_id from class where id = :classId) and is_deleted = false and version = 'premium')")
  boolean validateCourseAndItsPremiumness(@Bind("classId") UUID classId);
}
