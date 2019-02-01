package org.gooru.skylineinitial.infra.services.diagnosticfetcher;

import java.util.UUID;
import org.gooru.skylineinitial.infra.jdbi.UUIDMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish.
 */

interface DiagnosticFetcherDao {

  @Mapper(UUIDMapper.class)
  @SqlQuery("select assessment_id from diagnostic_assessment where grade_id = :studentOrigin")
  UUID fetchDiagnosticAssessmentForGrade(@Bind("studentOrigin") Long studentGradeLowerBound);
}
