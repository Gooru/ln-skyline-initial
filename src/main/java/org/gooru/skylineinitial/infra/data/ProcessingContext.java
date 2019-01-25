package org.gooru.skylineinitial.infra.data;

import java.util.UUID;

/**
 * @author ashish.
 */

public class ProcessingContext {

  private final SkylineInitialQueueModel model;
  private String subject;

  private ProcessingContext(SkylineInitialQueueModel model) {
    this.model = model;
  }


  public static ProcessingContext buildFromQueueModel(SkylineInitialQueueModel model) {
    return new ProcessingContext(model);
  }

  public UUID getUserId() {
    return model.getUserId();
  }

  public UUID getCourseId() {
    return model.getCourseId();
  }

  public UUID getClassId() {
    return model.getClassId();
  }

  public boolean isInClassExperience() {
    return model.getClassId() != null;
  }

  public boolean isILExperience() {
    return model.getClassId() == null;
  }

  public String getSubject() {
    return subject;
  }

  public DiagnosticAssessmentPlayedCommand getDiagnosticAssessmentPlayedCommand() {
    return model.payloadAsDiagnosticAssessmentPlayedCommand();
  }

  public void setSubject(String subject) {
    if (this.subject == null) {
      this.subject = subject;
    } else {
      throw new IllegalStateException(
          "Tried to initialize the subject while it is already initialized");
    }
  }

  @Override
  public String toString() {
    return "ProcessingContext{" +
        "userId=" + model.getUserId() +
        ", courseId=" + model.getCourseId() +
        ", classId=" + model.getClassId() +
        ", subject='" + subject + '\'' +
        '}';
  }

}
