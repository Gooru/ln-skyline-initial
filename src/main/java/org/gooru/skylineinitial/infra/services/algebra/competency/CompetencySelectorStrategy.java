package org.gooru.skylineinitial.infra.services.algebra.competency;

import java.util.List;

/**
 * @author ashish.
 */

public interface CompetencySelectorStrategy {

  Competency selectCompetencyForDomain(DomainCode domainCode, List<Competency> competencies);

}
