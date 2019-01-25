package org.gooru.skylineinitial.routes.utils;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.Objects;
import org.gooru.skylineinitial.infra.constants.Constants;

/**
 * @author ashish.
 */
public final class RouteRequest {

  private static JsonObject copyJsonObject(JsonObject input) {
    if (input == null || input.isEmpty()) {
      return new JsonObject();
    } else {
      return input.copy();
    }
  }

  private final JsonObject routeRequestBody;
  private final JsonObject httpBody;

  public RouteRequest(RoutingContext routingContext) {
    routeRequestBody = RouteRequestUtility.getBodyForMessage(routingContext);
    httpBody = routeRequestBody.getJsonObject(Constants.Message.MSG_HTTP_BODY);
  }

  public JsonObject getHttpBody() {
    return copyJsonObject(routeRequestBody.getJsonObject(Constants.Message.MSG_HTTP_BODY));
  }

  public String getRequestParamSingleValueForGetRequest(String param) {
    Objects.requireNonNull(param);
    JsonArray result = (JsonArray) httpBody.getValue(param);
    if (result != null && !result.isEmpty()) {
      return result.getString(0);
    }
    return null;
  }

  public JsonArray getRequestParamMultiValueForGetRequest(String param) {
    Objects.requireNonNull(param);
    return (JsonArray) httpBody.getValue(param);
  }

  public Object getRequestParamValue(String param) {
    Objects.requireNonNull(param);
    return httpBody.getValue(param);
  }

}
