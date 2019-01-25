package org.gooru.skylineinitial.infra.services.classsetting;

import java.util.List;
import java.util.UUID;
import org.gooru.skylineinitial.infra.services.algebra.competency.Competency;
import org.gooru.skylineinitial.infra.services.algebra.competency.mappers.CompetencyMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish.
 */

interface ClassGradeLowBoundFinderDao {

  @SqlQuery("select grade_lower_bound from class where id = :classId and is_deleted = false")
  Long fetchGradeLowerBoundForSpecifiedClass(@Bind("classId") UUID classId);

  @SqlQuery("select grade_lower_bound from class_member where class_id = :classId and user_id = :userId")
  Long fetchGradeLowerBoundForSpecifiedClassMember(@Bind("classId") UUID classId,
      @Bind("userId") UUID userId);

  @SqlQuery("select exists (select 1 from grade_master where id = :gradeId)")
  boolean validateGrade(@Bind("gradeId") Long gradeId);

  @Mapper(CompetencyMapper.class)
  @SqlQuery(
      "SELECT dcmt.tx_subject_code, dcmt.tx_domain_code, dcmt.tx_comp_code, dcmt.tx_comp_seq FROM   "
          + " domain_competency_matrix dcmt WHERE  dcmt.tx_subject_code = :subjectCode AND "
          + " dcmt.tx_comp_code = any(select lowline_tx_comp_code from grade_competency_bound where "
          + " tx_subject_code = :subjectCode and grade_id = :gradeId)")
  List<Competency> fetchCompetenciesForEarthlineForGradeAndSubject(
      @Bind("subjectCode") String subjectCode,
      @Bind("gradeId") Long gradeId);

}
