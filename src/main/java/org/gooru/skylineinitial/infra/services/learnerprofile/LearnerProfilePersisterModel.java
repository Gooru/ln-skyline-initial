package org.gooru.skylineinitial.infra.services.learnerprofile;

import java.util.List;

/**
 * @author ashish.
 */

public class LearnerProfilePersisterModel {

  private String userId;
  private List<String> gutCodes;
  private String classId;
  private String latestSessionId;
  private String collectionId;
  private Double collectionScore;
  private int status;
  private String subjectCode;
  private String profileSource;

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public List<String> getGutCodes() {
    return gutCodes;
  }

  public void setGutCodes(List<String> gutCodes) {
    this.gutCodes = gutCodes;
  }

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public String getLatestSessionId() {
    return latestSessionId;
  }

  public void setLatestSessionId(String latestSessionId) {
    this.latestSessionId = latestSessionId;
  }

  public String getCollectionId() {
    return collectionId;
  }

  public void setCollectionId(String collectionId) {
    this.collectionId = collectionId;
  }

  public Double getCollectionScore() {
    return collectionScore;
  }

  public void setCollectionScore(Double collectionScore) {
    this.collectionScore = collectionScore;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getSubjectCode() {
    return subjectCode;
  }

  public void setSubjectCode(String subjectCode) {
    this.subjectCode = subjectCode;
  }

  public String getProfileSource() {
    return profileSource;
  }

  public void setProfileSource(String profileSource) {
    this.profileSource = profileSource;
  }
}
