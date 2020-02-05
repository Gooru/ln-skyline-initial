package org.gooru.skylineinitial.infra.constants;

import java.util.UUID;

/**
 * @author ashish.
 */
public final class Constants {

  private Constants() {
    throw new AssertionError();
  }

  public static final class EventBus {

    public static final String MBEP_API_DISPATCHER = "org.gooru.skylineinitial.eventbus.api.dispatcher";
    public static final String MBEP_AUTH = "org.gooru.skylineinitial.eventbus.auth";
    public static final String MBEP_SKYLINE_INITIAL_QUEUE_PROCESSOR = "org.gooru.skylineinitial.eventbus.queueprocessor";
    public static final String MBUS_TIMEOUT = "event.bus.send.timeout.seconds";

    private EventBus() {
      throw new AssertionError();
    }
  }

  public static final class Message {

    public static final String MSG_OP = "mb.operation";
    public static final String MSG_OP_STATUS = "mb.op.status";
    public static final String MSG_OP_STATUS_SUCCESS = "mb.op.status.success";
    public static final String MSG_OP_STATUS_FAIL = "mb.op.status.fail";
    public static final String MSG_USER_ANONYMOUS = "anonymous";
    public static final String MSG_USER_ID = "user_id";
    public static final String MSG_HTTP_STATUS = "http.status";
    public static final String MSG_HTTP_BODY = "http.body";
    public static final String MSG_HTTP_HEADERS = "http.headers";

    public static final String MSG_KEY_SESSION = "session";
    public static final String MSG_SESSION_TOKEN = "session.token";
    public static final String MSG_OP_AUTH = "auth";
    public static final String MSG_OP_SKYLINE_INITIAL_FORCE_CALCULATE = "op.skylineinitial.force.calculate";
    public static final String MSG_OP_SKYLINE_INITIAL_STATE = "op.skylineinitial.state";
    public static final String MSG_MESSAGE = "message";
    public static final String ACCESS_TOKEN_VALIDITY = "access_token_validity";

    private Message() {
      throw new AssertionError();
    }
  }

  public static final class Route {

    public static final String API_INTERNAL_BANNER = "/api/internal/banner";
    public static final String API_INTERNAL_METRICS = "/api/internal/metrics";
    public static final String API_AUTH_ROUTE = "/api/skyline-initial/*";
    private static final String API_BASE_ROUTE = "/api/skyline-initial/:version/";
    public static final String API_SKYLINEINITIAL_STATE = API_BASE_ROUTE + "state";
    public static final String API_SKYLINEINITIAL_FORCE_CALCULATE =
        API_BASE_ROUTE + "calculate";

    private Route() {
      throw new AssertionError();
    }
  }
  
  public static final class LPStatus {
    public static final int NOT_STARTED = 0;
    public static final int IN_PROGRESS = 1;
    public static final int INFERRED = 2;
    public static final int ASSERTED = 3;
    public static final int COMPLETED = 4;
    public static final int MASTERED = 5;

    private LPStatus() {
      throw new AssertionError();
    }
  }

  public static final class Misc {

    public static final String COMPETENCY_PLACEHOLDER = new UUID(0, 0).toString();
    public static final String USER_PLACEHOLDER = new UUID(0, 0).toString();

    private Misc() {
      throw new AssertionError();
    }
  }

}
