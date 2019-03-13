package org.gooru.skylineinitial.infra.services.settings;

import java.util.UUID;

/**
 * @author ashish.
 */

public class SettingsDbModel {

  private UUID classId;
  private UUID courseId;
  private UUID studentId;
  private Long gradeLowerBound;
  private Long gradeUpperBound;
  private Long gradeCurrent;
  private Boolean forceCalculateILP;
  private Integer primaryLanguage;
  private Long studentGradeLowerBound;
  private Long studentGradeUpperBound;
  private Boolean profileBaselineDone;
  private UUID diagnosticAssessmentAssigned;
  private Integer diagnosticAssessmentState;
  private Boolean initialLPDone;

  public UUID getClassId() {
    return classId;
  }

  public void setClassId(UUID classId) {
    this.classId = classId;
  }

  public UUID getCourseId() {
    return courseId;
  }

  public void setCourseId(UUID courseId) {
    this.courseId = courseId;
  }

  public Long getGradeLowerBound() {
    return gradeLowerBound;
  }

  public void setGradeLowerBound(Long gradeLowerBound) {
    this.gradeLowerBound = gradeLowerBound;
  }

  public Long getGradeUpperBound() {
    return gradeUpperBound;
  }

  public void setGradeUpperBound(Long gradeUpperBound) {
    this.gradeUpperBound = gradeUpperBound;
  }

  public Long getGradeCurrent() {
    return gradeCurrent;
  }

  public void setGradeCurrent(Long gradeCurrent) {
    this.gradeCurrent = gradeCurrent;
  }

  public Boolean getForceCalculateILP() {
    return forceCalculateILP;
  }

  public void setForceCalculateILP(Boolean forceCalculateILP) {
    this.forceCalculateILP = forceCalculateILP;
  }

  public Integer getPrimaryLanguage() {
    return primaryLanguage;
  }

  public void setPrimaryLanguage(Integer primaryLanguage) {
    this.primaryLanguage = primaryLanguage;
  }

  public Long getStudentGradeLowerBound() {
    return studentGradeLowerBound;
  }

  public void setStudentGradeLowerBound(Long studentGradeLowerBound) {
    this.studentGradeLowerBound = studentGradeLowerBound;
  }

  public Long getStudentGradeUpperBound() {
    return studentGradeUpperBound;
  }

  public void setStudentGradeUpperBound(Long studentGradeUpperBound) {
    this.studentGradeUpperBound = studentGradeUpperBound;
  }

  public Boolean getProfileBaselineDone() {
    return profileBaselineDone;
  }

  public void setProfileBaselineDone(Boolean profileBaselineDone) {
    this.profileBaselineDone = profileBaselineDone;
  }

  public UUID getDiagnosticAssessmentAssigned() {
    return diagnosticAssessmentAssigned;
  }

  public void setDiagnosticAssessmentAssigned(UUID diagnosticAssessmentAssigned) {
    this.diagnosticAssessmentAssigned = diagnosticAssessmentAssigned;
  }

  public Integer getDiagnosticAssessmentState() {
    return diagnosticAssessmentState;
  }

  public void setDiagnosticAssessmentState(Integer diagnosticAssessmentState) {
    this.diagnosticAssessmentState = diagnosticAssessmentState;
  }

  public Boolean getInitialLPDone() {
    return initialLPDone;
  }

  public void setInitialLPDone(Boolean initialLPDone) {
    this.initialLPDone = initialLPDone;
  }

  public UUID getStudentId() {
    return studentId;
  }

  public void setStudentId(UUID studentId) {
    this.studentId = studentId;
  }
}
