package org.gooru.skylineinitial.infra.services.settings;

import java.util.UUID;
import org.gooru.skylineinitial.infra.data.StudentDiagnosticState;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface SettingsModel {

  UUID getClassId();

  UUID getStudentId();

  UUID getCourseId();

  Long getClassGradeLowerBound();

  Long getClassGradeUpperBound();

  Long getClassGradeCurrent();

  boolean isClassSetupToForceCalculateILP();

  boolean isClassNonNavigator();

  Integer getClassPrimaryLanguage();

  Long getStudentGradeLowerBound();

  Long getStudentGradeUpperBound();

  boolean isProfileBaselineDone();

  UUID getDiagnosticAssessmentId();

  boolean isInitialLPDone();

  StudentDiagnosticState getStudentDiagnosticState();

  static SettingsModel build(DBI dbi4core, UUID classId, UUID studentId) {
    return new SettingsModelBuilder(dbi4core, classId, studentId).build();
  }
}
