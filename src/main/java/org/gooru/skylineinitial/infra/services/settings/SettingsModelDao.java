package org.gooru.skylineinitial.infra.services.settings;

import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish.
 */

interface SettingsModelDao {

  @Mapper(SettingsDbModelMapper.class)
  @SqlQuery(
      "select c.id as class_id, c.course_id, c.grade_lower_bound, c.grade_upper_bound, c.grade_current, "
          + " c.is_offline, c.primary_language, cm.grade_lower_bound as student_grade_lower_bound, "
          + " cm.grade_upper_bound as student_grade_upper_bound, cm.profile_baseline_done, cm.diag_asmt_assigned, "
          + " cm.initial_lp_done, cm.diag_asmt_state, cm.user_id as student_id "
          + " from class c inner join class_member cm on c.id = cm.class_id "
          + " where c.id = :classId and cm.user_id = :studentId and c.is_deleted = false and c.is_archived = false "
          + " and cm.is_active = true and c.course_id is not null")
  SettingsDbModel createSettingDbModelForSpecifiedClassAndStudent(@Bind("classId") UUID classId,
      @Bind("studentId") UUID studentId);


  @SqlQuery("select exists (select 1 from course where id = :courseId and version = 'premium')")
  boolean isCoursePremium(@Bind("courseId") UUID courseId);
}
