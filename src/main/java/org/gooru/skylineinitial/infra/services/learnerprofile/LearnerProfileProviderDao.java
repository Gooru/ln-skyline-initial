package org.gooru.skylineinitial.infra.services.learnerprofile;

import java.util.List;
import org.gooru.skylineinitial.infra.services.algebra.competency.Competency;
import org.gooru.skylineinitial.infra.services.algebra.competency.mappers.CompetencyMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish.
 */

interface LearnerProfileProviderDao {

  @Mapper(CompetencyMapper.class)
  @SqlQuery(
      "SELECT tx_subject_code, tx_domain_code, tx_comp_code, tx_comp_seq FROM domain_competency_matrix "
          + " WHERE (tx_subject_code, tx_domain_code, tx_comp_seq) IN (SELECT dcmt.tx_subject_code, dcmt.tx_domain_code, "
          + " Max(dcmt.tx_comp_seq) tx_comp_seq FROM domain_competency_matrix dcmt "
          + " INNER JOIN learner_profile_competency_status lpcs ON lpcs.gut_code = dcmt.tx_comp_code AND "
          + " dcmt.tx_subject_code = lpcs.tx_subject_code AND dcmt.tx_subject_code = :subjectCode AND "
          + " lpcs.user_id = :userId AND lpcs.status >= 4 GROUP BY dcmt.tx_subject_code, dcmt.tx_domain_code)")
  List<Competency> fetchProficiencyForUserInSpecifiedSubjectAndDomains(
      @Bind("userId") String userId, @Bind("subjectCode") String subjectCode);


}
