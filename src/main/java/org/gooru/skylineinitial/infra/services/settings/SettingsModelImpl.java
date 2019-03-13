package org.gooru.skylineinitial.infra.services.settings;

import java.util.UUID;
import org.gooru.skylineinitial.infra.data.StudentDiagnosticState;

/**
 * @author ashish.
 */

class SettingsModelImpl implements SettingsModel {

  private final SettingsDbModel dbModel;
  private final Boolean isCoursePremium;

  SettingsModelImpl(SettingsDbModel dbModel, Boolean isCoursePremium) {

    this.dbModel = dbModel;
    this.isCoursePremium = isCoursePremium;
  }

  @Override
  public UUID getClassId() {
    return dbModel.getClassId();
  }

  @Override
  public UUID getCourseId() {
    return dbModel.getCourseId();
  }

  @Override
  public Long getClassGradeLowerBound() {
    return dbModel.getGradeLowerBound();
  }

  @Override
  public Long getClassGradeUpperBound() {
    return dbModel.getGradeUpperBound();
  }

  @Override
  public Long getClassGradeCurrent() {
    return dbModel.getGradeCurrent();
  }

  @Override
  public boolean isClassSetupToForceCalculateILP() {
    return dbModel.getForceCalculateILP();
  }

  @Override
  public boolean isClassNonNavigator() {
    return !isCoursePremium;
  }

  @Override
  public Integer getClassPrimaryLanguage() {
    return dbModel.getPrimaryLanguage();
  }

  @Override
  public Long getStudentGradeLowerBound() {
    return dbModel.getStudentGradeLowerBound();
  }

  @Override
  public Long getStudentGradeUpperBound() {
    return dbModel.getStudentGradeUpperBound();
  }

  @Override
  public boolean isProfileBaselineDone() {
    return dbModel.getProfileBaselineDone();
  }

  @Override
  public UUID getDiagnosticAssessmentId() {
    return dbModel.getDiagnosticAssessmentAssigned();
  }

  @Override
  public boolean isInitialLPDone() {
    return dbModel.getInitialLPDone();
  }

  @Override
  public StudentDiagnosticState getStudentDiagnosticState() {
    return StudentDiagnosticState.builder(dbModel.getDiagnosticAssessmentState());
  }

  @Override
  public UUID getStudentId() {
    return dbModel.getStudentId();
  }
}
