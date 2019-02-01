package org.gooru.skylineinitial.processors.stateapi;

import io.vertx.core.json.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author ashish.
 */

class StateApiResponse {
  /*
   Here is the response structure
    {
      "destination": "diagnostic-play",
      "context": {
        "diagnosticId": "UUID for diag asmt"
      }
    }
 */

  private final StateDestination destination;
  private final JsonObject context;
  private static final String DESTINATION_KEY = "destination";
  private static final String CONTEXT_KEY = "context";
  private static final String DIAGNOSTIC_ID_KEY = "diagnosticId";

  private StateApiResponse(
      StateDestination destination, JsonObject context) {
    this.destination = destination;
    this.context = context;
  }


  JsonObject asJson() {
    return new JsonObject().put(DESTINATION_KEY, destination.getName()).put(CONTEXT_KEY, context);
  }

  static final class StateApiResponseBuilder {

    private StateApiResponseBuilder() {
      throw new AssertionError();
    }

    static StateApiResponse buildForShowCourseMap() {
      return build(StateDestination.COURSE_MAP, null);
    }

    static StateApiResponse buildForDiagnosticPlay(UUID diagnosticAssessmentId) {
      if (diagnosticAssessmentId == null) {
        throw new IllegalStateException("Missing diagnostic assessment id");
      }
      return build(StateDestination.DIAGNOSTIC_PLAY,
          new JsonObject().put(DIAGNOSTIC_ID_KEY, diagnosticAssessmentId.toString()));
    }

    static StateApiResponse buildForShowDirections() {
      return build(StateDestination.SHOW_DIRECTIONS, null);
    }

    static StateApiResponse buildForILPInProgress() {
      return build(StateDestination.ILP_IN_PROGRESS, null);
    }

    static StateApiResponse buildForClassSetupIncomplete() {
      return build(StateDestination.CLASS_SETUP_INCOMPLETE, null);
    }

    private static StateApiResponse build(StateDestination destination, JsonObject context) {
      if (destination == null) {
        throw new IllegalStateException("Invalid destination");
      }
      if (destination != StateDestination.DIAGNOSTIC_PLAY && context != null) {
        throw new IllegalStateException("Invalid context for destination: " + destination.name);
      } else if (destination == StateDestination.DIAGNOSTIC_PLAY && context == null) {
        throw new IllegalStateException("Invalid context for destination: " + destination.name);
      } else {
        return new StateApiResponse(destination, context);
      }
    }
  }

  enum StateDestination {
    COURSE_MAP("course-map"),
    DIAGNOSTIC_PLAY("diagnostic-play"),
    SHOW_DIRECTIONS("show-directions"),
    ILP_IN_PROGRESS("ilp-in-progress"),
    CLASS_SETUP_INCOMPLETE("class-setup-incomplete");

    private final String name;
    private static final Map<String, StateDestination> LOOKUP = new HashMap<>(values().length);

    static {
      for (StateDestination stateDestination : values()) {
        LOOKUP.put(stateDestination.name, stateDestination);
      }
    }

    StateDestination(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public static StateDestination builder(String destination) {
      StateDestination result = LOOKUP.get(destination);
      if (result == null) {
        throw new IllegalArgumentException("Invalid stateDestination: " + destination);
      }
      return result;
    }

  }

}
