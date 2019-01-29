package org.gooru.skylineinitial.processors.stateapi;

import java.util.UUID;
import org.gooru.skylineinitial.infra.data.EventBusMessage;
import org.gooru.skylineinitial.infra.utils.UuidUtils;

/**
 * @author ashish.
 */

class StateApiProcessorCommand {

  UUID getUserId() {
    return userId;
  }

  UUID getClassId() {
    return classId;
  }

  private final UUID userId;
  private final UUID classId;

  private StateApiProcessorCommand(UUID userId, UUID classId) {

    this.userId = userId;
    this.classId = classId;
  }

  @Override
  public String toString() {
    return "StateApiProcessorCommand {userId=" + userId + ", classId=" + classId + '}';
  }

  static StateApiProcessorCommand builder(EventBusMessage eventBusMessage) {
    UUID classId = UuidUtils.convertStringToUuid(
        eventBusMessage.getRequestBody().getString(RequestAttributes.CLASS_ID));
    if (classId == null) {
      throw new IllegalArgumentException("Invalid UUID for classId");
    }
    UUID userId = eventBusMessage.getUserId();
    if (userId == null) {
      throw new IllegalArgumentException("Invalid user id");
    }
    return new StateApiProcessorCommand(userId, classId);
  }

  private static class RequestAttributes {
    private static final String CLASS_ID = "classId";
  }
}
