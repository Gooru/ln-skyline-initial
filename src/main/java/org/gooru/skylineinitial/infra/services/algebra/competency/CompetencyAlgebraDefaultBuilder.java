package org.gooru.skylineinitial.infra.services.algebra.competency;

import java.util.ArrayList;

/**
 * @author ashish.
 */

public final class CompetencyAlgebraDefaultBuilder {

  private static CompetencyLine emptyCompetencyLine;

  public static CompetencyLine getEmptyCompetencyLine() {
    if (emptyCompetencyLine == null) {
      emptyCompetencyLine = CompetencyMap.build(new ArrayList<>()).getCeilingLine();
    }
    return emptyCompetencyLine;
  }

  private CompetencyAlgebraDefaultBuilder() {
    throw new IllegalStateException();
  }

}
