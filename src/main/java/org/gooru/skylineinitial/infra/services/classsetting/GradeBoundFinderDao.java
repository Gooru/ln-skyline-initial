package org.gooru.skylineinitial.infra.services.classsetting;

import java.util.List;
import org.gooru.skylineinitial.infra.services.algebra.competency.Competency;
import org.gooru.skylineinitial.infra.services.algebra.competency.mappers.CompetencyMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish.
 */

interface GradeBoundFinderDao {

  @Mapper(CompetencyMapper.class)
  @SqlQuery(
      "SELECT dcmt.tx_subject_code, dcmt.tx_domain_code, dcmt.tx_comp_code, dcmt.tx_comp_seq FROM   "
          + " domain_competency_matrix dcmt WHERE dcmt.tx_comp_code = any(select average_tx_comp_code from grade_competency_bound where "
          + " grade_id = :gradeId)")
  List<Competency> fetchCompetenciesForAveragelineForGrade(
      @Bind("gradeId") Long gradeId);

}
