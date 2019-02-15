package org.gooru.skylineinitial.infra.services.diagnosticapplicable;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;

/**
 * @author ashish.
 */

interface DiagnosticApplicabilityDao {

  @SqlQuery("select count(tx_domain_code) from grade_competency_bound where tx_subject_code = :subjectCode and grade_id = :gradeId")
  int fetchDomainsCountInGrade(@Bind("subjectCode") String subjectCode,
      @Bind("gradeId") Long gradeId);


  @SqlQuery("select count(distinct dcm.tx_domain_code) from domain_competency_matrix dcm "
      + " inner join learner_profile_competency_status lpcs on lpcs.gut_code = dcm.tx_comp_code and dcm.tx_subject_code = lpcs.tx_subject_code "
      + " where tx_domain_code in (select tx_domain_code from grade_competency_bound where tx_subject_code = :subjectCode and grade_id = :gradeId) "
      + " and dcm.tx_subject_code = :subjectCode and lpcs.user_id = :userId and lpcs.status >= 4")
  int fetchDomainsCountMasteredByUserInGrade(@Bind("subjectCode") String subjectCode,
      @Bind("gradeId") Long gradeId, @Bind("userId") String userId);

  @SqlQuery("select tx_subject_code from grade_master where id = :gradeId")
  String fetchSubjectCodeForGradeId(@Bind("gradeId") Long gradeId);

}
