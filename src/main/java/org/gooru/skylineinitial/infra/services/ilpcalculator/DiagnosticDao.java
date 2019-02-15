package org.gooru.skylineinitial.infra.services.ilpcalculator;

import java.util.List;
import java.util.UUID;
import org.gooru.skylineinitial.infra.services.ilpcalculator.QuestionIdGutCodeTuple.QuestionIdGutCodeTupleMapper;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author ashish.
 */

interface DiagnosticDao {

  @Mapper(QuestionIdGutCodeTupleMapper.class)
  @SqlQuery("select question_id, gut_code from diagnostic_assessment_questions where "
      + " diagnostic_assessment_id = :assessmentId and grade_id = :gradeId")
  List<QuestionIdGutCodeTuple> selectQuestionIdGutCodeTuplesForSpecifiedDiagnostic(
      @Bind("assessmentId") UUID assessmentId, @Bind("gradeId") Long gradeId);

}
