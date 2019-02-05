package org.gooru.skylineinitial.infra.services.ilpcalculator;

import java.util.List;
import org.gooru.skylineinitial.infra.jdbi.PGArray;
import org.gooru.skylineinitial.infra.services.algebra.competency.Competency;
import org.gooru.skylineinitial.infra.services.algebra.competency.mappers.CompetencyMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish.
 */

interface CompetenyDao {

  @Mapper(CompetencyMapper.class)
  @SqlQuery(
      "SELECT dcmt.tx_subject_code, dcmt.tx_domain_code, dcmt.tx_comp_code, dcmt.tx_comp_seq FROM   "
          + " domain_competency_matrix dcmt WHERE  dcmt.tx_subject_code = :subjectCode AND "
          + " dcmt.tx_comp_code = any(:gutCodes)")
  List<Competency> transformGutCodesToCompetency(@Bind("subjectCode") String subjectCode,
      @Bind("gutCodes") PGArray<String> gutCodes);


}
