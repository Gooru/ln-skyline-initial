package org.gooru.skylineinitial.infra.services.ilpcalculator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.gooru.skylineinitial.infra.utils.UuidUtils;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish.
 */

class QuestionIdGutCodeTuple {

  private UUID questionId;
  private String gutCode;

  public UUID getQuestionId() {
    return questionId;
  }

  public void setQuestionId(UUID questionId) {
    this.questionId = questionId;
  }

  public String getGutCode() {
    return gutCode;
  }

  public void setGutCode(String gutCode) {
    this.gutCode = gutCode;
  }

  public static class QuestionIdGutCodeTupleMapper implements
      ResultSetMapper<QuestionIdGutCodeTuple> {

    private static final String QUESTION_ID = "question_id";
    private static final String GUT_CODE = "gut_code";

    @Override
    public QuestionIdGutCodeTuple map(int index, ResultSet r, StatementContext ctx)
        throws SQLException {
      QuestionIdGutCodeTuple model = new QuestionIdGutCodeTuple();
      model.setQuestionId(UuidUtils.convertStringToUuid(r.getString(QUESTION_ID)));
      model.setGutCode(r.getString(GUT_CODE));
      return model;
    }
  }
}
