package org.gooru.skylineinitial.infra.data;

import io.vertx.core.json.JsonObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.gooru.skylineinitial.infra.utils.UuidUtils;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

/**
 * @author ashish
 */
public class SkylineInitialQueueModel {

  public static final int RQ_STATUS_QUEUED = 0;
  public static final int RQ_STATUS_DISPATCHED = 1;
  public static final int RQ_STATUS_INPROCESS = 2;

  private Long id;
  private UUID userId;
  private UUID courseId;
  private UUID classId;
  private int category;
  private int status;
  private JsonObject payload;

  public static SkylineInitialQueueModel createNonPersistedEmptyModel() {
    return new SkylineInitialQueueModel();
  }

  public String toSummaryJson() {
    return new JsonObject().put("id", id).toString();
  }

  public String toJson() {
    return new JsonObject().put("id", id).put("userId", UuidUtils.uuidToString(userId))
        .put("courseId", UuidUtils.uuidToString(courseId))
        .put("classId", UuidUtils.uuidToString(classId))
        .put("category", category).put("status", status).toString();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public UUID getCourseId() {
    return courseId;
  }

  public void setCourseId(UUID courseId) {
    this.courseId = courseId;
  }

  public UUID getClassId() {
    return classId;
  }

  public void setClassId(UUID classId) {
    this.classId = classId;
  }

  public int getCategory() {
    return category;
  }

  public void setCategory(int category) {
    this.category = category;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public boolean isModelPersisted() {
    return id != null;
  }

  public JsonObject getPayload() {
    return payload;
  }

  public void setPayload(JsonObject payload) {
    this.payload = payload;
  }

  public DiagnosticAssessmentPlayedCommand payloadAsDiagnosticAssessmentPlayedCommand() {
    if (payload != null) {
      return DiagnosticAssessmentPlayedCommand.build(payload);
    }
    return null;
  }

  public static class SkylineInitialQueueModelMapper implements
      ResultSetMapper<SkylineInitialQueueModel> {

    @Override
    public SkylineInitialQueueModel map(final int index, final ResultSet resultSet,
        final StatementContext statementContext) throws SQLException {
      SkylineInitialQueueModel model = new SkylineInitialQueueModel();
      model.setId(resultSet.getLong("id"));
      model.setCategory(resultSet.getInt("category"));
      model.setStatus(resultSet.getInt("status"));
      model.setUserId(UuidUtils.convertStringToUuid(resultSet.getString("user_id")));
      model.setCourseId(UuidUtils.convertStringToUuid(resultSet.getString("course_id")));
      model.setClassId(UuidUtils.convertStringToUuid(resultSet.getString("class_id")));
      String payloadString = resultSet.getString("payload");
      JsonObject payload = payloadString != null ? new JsonObject(payloadString) : null;
      model.setPayload(payload);
      return model;
    }

  }

}
