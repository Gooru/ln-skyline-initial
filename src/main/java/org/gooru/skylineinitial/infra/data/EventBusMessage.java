package org.gooru.skylineinitial.infra.data;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import java.util.UUID;
import org.gooru.skylineinitial.infra.constants.Constants;

/**
 * @author ashish
 */
public final class EventBusMessage {

  public static EventBusMessage eventBusMessageBuilder(Message<JsonObject> message) {
    String sessionToken = message.body().getString(Constants.Message.MSG_SESSION_TOKEN);
    String userId = message.body().getString(Constants.Message.MSG_USER_ID);
    JsonObject requestBody = message.body().getJsonObject(Constants.Message.MSG_HTTP_BODY);
    JsonObject session = message.body().getJsonObject(Constants.Message.MSG_KEY_SESSION);
    UUID user = userId == null ? null : UUID.fromString(userId);
    return new EventBusMessage(sessionToken, requestBody, user, session);
  }

  private final String sessionToken;
  private final JsonObject requestBody;
  private final UUID userId;
  private final JsonObject session;

  private EventBusMessage(String sessionToken, JsonObject requestBody, UUID userId,
      JsonObject session) {
    this.sessionToken = sessionToken;
    this.requestBody = requestBody;
    this.userId = userId;
    this.session = session;
  }

  public String getSessionToken() {
    return sessionToken;
  }

  public JsonObject getRequestBody() {
    return requestBody;
  }

  public UUID getUserId() {
    return userId;
  }

  public JsonObject getSession() {
    return session;
  }

}
