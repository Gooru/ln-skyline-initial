package org.gooru.skylineinitial.infra.data;

import java.util.UUID;

/**
 * @author ashish.
 */

public final class LearnerProfileSourceGenerator {

  private static final String DIAGNOSTIC = "diagnostic";
  private static final String OFFLINE_CLASS_TRIGGER = "OfflineClassTrigger";

  private LearnerProfileSourceGenerator() {
    throw new AssertionError();
  }

  public static String generateProfileSource(UUID assessmentId) {
    return DIAGNOSTIC + ":" + assessmentId;
  }

  public static String generateProfileSourceForOfflineClass(UUID classId) {
    return OFFLINE_CLASS_TRIGGER + ":" + classId;
  }
}
