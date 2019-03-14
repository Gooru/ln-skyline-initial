package org.gooru.skylineinitial.infra.data;

import java.util.UUID;

/**
 * @author ashish.
 */

public final class LearnerProfileSourceGenerator {

  private static final String DIAGNOSTIC = "diagnostic";
  private static final String FORCE_CALCULATE_TRIGGER = "ForceCalculateTrigger";

  private LearnerProfileSourceGenerator() {
    throw new AssertionError();
  }

  public static String generateProfileSource(UUID assessmentId) {
    return DIAGNOSTIC + ":" + assessmentId;
  }

  public static String generateProfileSourceForForceCalculate(UUID classId) {
    return FORCE_CALCULATE_TRIGGER + ":" + classId;
  }
}
