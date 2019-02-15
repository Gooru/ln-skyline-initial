package org.gooru.skylineinitial.infra.services.subjectinferer;

import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */
interface SubjectInfererDao {

  @SqlQuery("select subject_bucket from course where id = :courseId and is_deleted = false")
  String fetchSubjectBucketForCourse(@Bind("courseId") UUID courseId);

  @SqlQuery("select default_taxonomy_subject_id from taxonomy_subject where code = :subjectCode")
  String fetchGutSubjectCodeForFrameworkSubjectCode(@Bind("subjectCode") String subjectCode);

  @SqlQuery("select tx_subject_code from grade_master where id = (select grade_current from class where id = :classId)")
  String fetchSubjectForClass(@Bind("classId") UUID classId);
}
