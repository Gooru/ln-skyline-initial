package org.gooru.skylineinitial.infra.services.algebra.competency;

import java.util.List;

/**
 * This is representation of a line in {@link CompetencyMap}
 * <p>
 * This involves one point in space which is to say, it stores one competency per domain. It could
 * model the highest progression topic covered by content, or lowest progression covered by content
 * or proficiency of learner
 *
 * @author ashish.
 */
public interface CompetencyLine {

  /**
   * Fetch the domains list for the competency lines
   *
   * @return List of domains, not in any predefined order
   */
  List<DomainCode> getDomains();

  /**
   * Fetch the competency for specified domain. For Competency Line single domain should have one
   * competency.
   *
   * @param domainCode Code of the domain for which competency needs to be fetched
   * @return Competency
   */
  Competency getCompetencyForDomain(DomainCode domainCode);

  /**
   * Compute the Competency Route from this Competency line to specified competency line
   *
   * @param competencyLine The destination or target Competency line
   * @return Competency Route
   */
  CompetencyRoute getRouteToCompetencyLine(CompetencyLine competencyLine);

  /**
   * Is this competency line empty, i.e. it has no domains and ergo no competencies
   *
   * @return true if it is empty
   */
  boolean isEmpty();

  /**
   * Merge this competency line with specified competency line and return the result competency
   * line
   *
   * The merge is progression aware. It picks up the higher progression competency in common
   * domains. In case the domain is not present in one of competency lines, whatever is present is
   * picked up. If one of the competency lines is empty, the other one is returned. Both lines
   * should belong to same subject bucket, else result will not make sense
   *
   * @param competencyLine The target line with which merge need to be done
   * @param mergeWithHighValue If true, then while merging result will be higher value based on
   * progression.
   * @return New competencyLine which represents the merged line
   */
  CompetencyLine merge(CompetencyLine competencyLine, boolean mergeWithHighValue);

  static CompetencyLine build(CompetencyMap competencyMap, boolean ceiling) {
    return new CompetencyLineImpl(competencyMap, ceiling);
  }
}
