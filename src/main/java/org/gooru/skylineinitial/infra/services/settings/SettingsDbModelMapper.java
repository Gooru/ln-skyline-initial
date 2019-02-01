package org.gooru.skylineinitial.infra.services.settings;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.gooru.skylineinitial.infra.utils.UuidUtils;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish.
 */

public class SettingsDbModelMapper implements ResultSetMapper<SettingsDbModel> {


  @Override
  public SettingsDbModel map(int index, ResultSet r, StatementContext ctx) throws SQLException {
    SettingsDbModel model = new SettingsDbModel();
    model.setClassId(UuidUtils.convertStringToUuid(r.getString(CLASS_ID)));
    model.setCourseId(UuidUtils.convertStringToUuid(r.getString(COURSE_ID)));
    model.setStudentId(UuidUtils.convertStringToUuid(r.getString(STUDENT_ID)));
    model.setGradeLowerBound(r.getLong(GRADE_LOWER_BOUND));
    model.setGradeUpperBound(r.getLong(GRADE_UPPER_BOUND));
    model.setGradeCurrent(r.getLong(GRADE_CURRENT));
    model.setOffline(r.getBoolean(IS_OFFLINE));
    model.setPrimaryLanguage(r.getInt(PRIMARY_LANGUAGE));
    model.setStudentGradeLowerBound(r.getLong(STUDENT_GRADE_LOWER_BOUND));
    model.setStudentGradeUpperBound(r.getLong(STUDENT_GRADE_UPPER_BOUND));
    model.setProfileBaselineDone(r.getBoolean(PROFILE_BASELINE_DONE));
    model.setDiagnosticAssessmentAssigned(
        UuidUtils.convertStringToUuid(r.getString(DIAG_ASMT_ASSIGNED)));
    model.setInitialLPDone(r.getBoolean(INITIAL_LP_DONE));
    model.setDiagnosticAssessmentState(r.getInt(DIAG_ASMT_STATE));
    return model;
  }

  private static final String CLASS_ID = "class_id";
  private static final String COURSE_ID = "course_id";
  private static final String STUDENT_ID = "student_id";
  private static final String GRADE_LOWER_BOUND = "grade_lower_bound";
  private static final String GRADE_UPPER_BOUND = "grade_upper_bound";
  private static final String GRADE_CURRENT = "grade_current";
  private static final String IS_OFFLINE = "is_offline";
  private static final String PRIMARY_LANGUAGE = "primary_language";
  private static final String STUDENT_GRADE_LOWER_BOUND = "student_grade_lower_bound";
  private static final String STUDENT_GRADE_UPPER_BOUND = "student_grade_upper_bound";
  private static final String PROFILE_BASELINE_DONE = "profile_baseline_done";
  private static final String DIAG_ASMT_ASSIGNED = "diag_asmt_assigned";
  private static final String INITIAL_LP_DONE = "initial_lp_done";
  private static final String DIAG_ASMT_STATE = "diag_asmt_state";


}
