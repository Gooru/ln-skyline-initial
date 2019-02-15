package org.gooru.skylineinitial.infra.services.ilpcalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.gooru.skylineinitial.infra.services.algebra.competency.Competency;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencySelectorStrategy;
import org.gooru.skylineinitial.infra.services.algebra.competency.DomainCode;

/**
 * This strategy encapsulates the logic of finding the applicable competency in a domain based on
 * student's performance in diagnostic assessment.
 *
 * @author ashish.
 */

class ContiguousCorrectMaximumCompetencySelectorStrategy implements CompetencySelectorStrategy {

  private final List<String> completedGutCodes;

  private ContiguousCorrectMaximumCompetencySelectorStrategy(List<String> completedGutCodes) {
    this.completedGutCodes = completedGutCodes;
  }

  static ContiguousCorrectMaximumCompetencySelectorStrategy build(List<String> completedGutCodes) {
    if (completedGutCodes == null || completedGutCodes.isEmpty()) {
      return new ContiguousCorrectMaximumCompetencySelectorStrategy(Collections.emptyList());
    }
    return new ContiguousCorrectMaximumCompetencySelectorStrategy(completedGutCodes);
  }

  @Override
  public Competency selectCompetencyForDomain(DomainCode domainCode,
      List<Competency> competencies) {
    if (competencies == null || competencies.isEmpty() || completedGutCodes.isEmpty()) {
      return null;
    }

    validateCompetenciesToBeOnSamePath(competencies);

    List<Competency> sortedListOfCompetencies = new ArrayList<>(competencies);
    sortedListOfCompetencies.sort(new CompetencyComparatorBasedOnProgression());

    Competency lastCorrectCompetency = null;
    for (Competency competency : sortedListOfCompetencies) {
      if (!completedGutCodes.contains(competency.getCompetencyCode().getCode())) {
        return lastCorrectCompetency;
      }
      lastCorrectCompetency = competency;
    }
    return lastCorrectCompetency;
  }

  private void validateCompetenciesToBeOnSamePath(List<Competency> competencies) {
    Competency initialCompetency = Competency.buildInitialCompetency(competencies.get(0));

    for (Competency competency : competencies) {
      if (!competency.belongToSamePath(initialCompetency)) {
        throw new IllegalStateException(
            "Competencies provided for selector does not belong to same domain");
      }
    }
  }

  static class CompetencyComparatorBasedOnProgression implements Comparator<Competency> {

    @Override
    public int compare(Competency c1, Competency c2) {
      return Integer
          .compare(c1.getProgressionLevel().getProgressionLevel(), c2.getProgressionLevel()
              .getProgressionLevel());
    }
  }
}
