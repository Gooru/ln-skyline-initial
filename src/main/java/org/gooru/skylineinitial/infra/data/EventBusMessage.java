package org.gooru.skylineinitial.infra.data;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.gooru.skylineinitial.infra.constants.Constants;

/**
 * @author ashish
 */
public final class EventBusMessage {

  public static EventBusMessage eventBusMessageBuilder(Message<JsonObject> message) {
    JsonObject requestBody = message.body().getJsonObject(Constants.Message.MSG_HTTP_BODY);

    return new EventBusMessage(requestBody);
  }

  private final JsonObject requestBody;

  private EventBusMessage(JsonObject requestBody) {
    this.requestBody = requestBody;
  }

  public JsonObject getRequestBody() {
    return requestBody;
  }

}
