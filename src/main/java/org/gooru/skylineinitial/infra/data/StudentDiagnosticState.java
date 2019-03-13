package org.gooru.skylineinitial.infra.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ashish.
 */

public enum StudentDiagnosticState {
  NOT_INITIALIZED(0),
  NOT_NEEDED(1),
  SUGGESTED(2),
  DONE(3),
  NOT_AVAILABLE(4),
  FORCE_CALCULATE(5);

  private final Integer value;
  private static final Map<Integer, StudentDiagnosticState> LOOKUP = new HashMap<>(values().length);

  static {
    for (StudentDiagnosticState studentDiagnosticState : values()) {
      LOOKUP.put(studentDiagnosticState.value, studentDiagnosticState);
    }
  }

  StudentDiagnosticState(Integer value) {
    this.value = value;
  }

  public Integer getValue() {
    return value;
  }

  public static StudentDiagnosticState builder(Integer diagnosticState) {
    StudentDiagnosticState result = LOOKUP.get(diagnosticState);
    if (result == null) {
      throw new IllegalArgumentException("Invalid diagnostic state: " + diagnosticState);
    }
    return result;
  }

}
