package org.gooru.skylineinitial.infra.data;

import java.util.Objects;
import java.util.UUID;

/**
 * @author ashish.
 */

public final class LearnerProfileSourceGenerator {

  private static final String DIAGNOSTIC = "diagnostic";

  private LearnerProfileSourceGenerator() {
    throw new AssertionError();
  }

  public static String generateProfileSource(UUID assessmentId) {
    return DIAGNOSTIC + ":" + Objects.toString(assessmentId);
  }
}
