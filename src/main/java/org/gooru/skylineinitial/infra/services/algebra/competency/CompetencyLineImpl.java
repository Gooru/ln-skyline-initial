package org.gooru.skylineinitial.infra.services.algebra.competency;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author ashish.
 */
class CompetencyLineImpl implements CompetencyLine {

  private final List<DomainCode> domains;
  private final Map<DomainCode, Competency> domainCodeCompetencyMap;

  CompetencyLineImpl(CompetencyMap competencyMap, boolean ceiling) {
    Objects.requireNonNull(competencyMap);

    domains = Collections.unmodifiableList(new ArrayList<>(competencyMap.getDomains()));
    domainCodeCompetencyMap = new HashMap<>();

    for (DomainCode domainCode : domains) {
      List<Competency> competencies = competencyMap.getCompetenciesForDomain(domainCode);
      if (ceiling) {
        domainCodeCompetencyMap.put(domainCode, competencies.get(competencies.size() - 1));
      } else {
        domainCodeCompetencyMap.put(domainCode, competencies.get(0));
      }
    }

  }

  @Override
  public List<DomainCode> getDomains() {
    return Collections.unmodifiableList(domains);
  }

  @Override
  public Competency getCompetencyForDomain(DomainCode domainCode) {
    if (domainCode != null) {
      return domainCodeCompetencyMap.get(domainCode);
    }
    return null;
  }

  @Override
  public CompetencyRoute getRouteToCompetencyLine(CompetencyLine competencyLine) {
    if (competencyLine != null) {
      return CompetencyRoute.build(this, competencyLine);
    }
    return null;
  }

  @Override
  public boolean isEmpty() {
    return domains == null || domains.isEmpty();
  }

  @Override
  public CompetencyLine merge(CompetencyLine competencyLine, boolean mergeWithHighValue) {
    if (competencyLine.isEmpty()) {
      return cloned(this);
    } else if (this.isEmpty()) {
      return cloned(competencyLine);
    } else {
      return mergedLine(competencyLine, mergeWithHighValue);
    }
  }

  private CompetencyLine mergedLine(CompetencyLine competencyLine, boolean mergeWithHighValue) {
    Set<DomainCode> domainCodeSet = new HashSet<>();
    Map<DomainCode, Competency> resultDomainCodeCompetencyMap = new HashMap<>();
    domainCodeSet.addAll(competencyLine.getDomains());
    domainCodeSet.addAll(domains);
    for (DomainCode domainCode : domainCodeSet) {
      Competency sourceCompetency, targetCompetency;
      sourceCompetency = getCompetencyForDomain(domainCode);
      targetCompetency = competencyLine.getCompetencyForDomain(domainCode);

      if (sourceCompetency == null && targetCompetency != null) {
        resultDomainCodeCompetencyMap.put(domainCode, targetCompetency);
      } else if (sourceCompetency != null && targetCompetency == null) {
        resultDomainCodeCompetencyMap.put(domainCode, sourceCompetency);
      } else if (sourceCompetency != null) {
        resultDomainCodeCompetencyMap.put(domainCode,
            mergeTwoValidCompetencies(sourceCompetency, targetCompetency, mergeWithHighValue));
      }
    }
    return new CompetencyLineImpl(new ArrayList<>(domainCodeSet), resultDomainCodeCompetencyMap);
  }

  private Competency mergeTwoValidCompetencies(Competency sourceCompetency,
      Competency targetCompetency, boolean mergeWithHighValue) {
    if (!sourceCompetency.belongToSameSubject(targetCompetency)) {
      throw new IllegalStateException(
          "Source and Target competency do not belong to same subject. Source: "
              + sourceCompetency.toString() + "  Target: " + targetCompetency.toString());
    }
    if (mergeWithHighValue) {
      if (sourceCompetency.getProgressionLevel().getProgressionLevel() > targetCompetency
          .getProgressionLevel().getProgressionLevel()) {
        return sourceCompetency;
      } else {
        return targetCompetency;
      }
    } else {
      if (sourceCompetency.getProgressionLevel().getProgressionLevel() > targetCompetency
          .getProgressionLevel().getProgressionLevel()) {
        return targetCompetency;
      } else {
        return sourceCompetency;
      }
    }

  }

  @Override
  public String toString() {
    return "CompetencyLine{" + "domains=" + domains + ", domainCodeCompetencyMap="
        + domainCodeCompetencyMap + '}';
  }

  private CompetencyLineImpl(List<DomainCode> domains,
      Map<DomainCode, Competency> domainCodeCompetencyMap) {
    this.domains = domains;
    this.domainCodeCompetencyMap = domainCodeCompetencyMap;
  }

  private static CompetencyLine cloned(CompetencyLine competencyLine) {
    if (competencyLine.isEmpty()) {
      return CompetencyAlgebraDefaultBuilder.getEmptyCompetencyLine();
    }
    Map<DomainCode, Competency> domainCodeCompetencyMap = new HashMap<>();
    List<DomainCode> domainCodes = competencyLine.getDomains();

    for (DomainCode domainCode : domainCodes) {
      Competency competency = competencyLine.getCompetencyForDomain(domainCode);
      domainCodeCompetencyMap.put(domainCode, competency);
    }

    return new CompetencyLineImpl(domainCodes, domainCodeCompetencyMap);
  }
}
