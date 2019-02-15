package org.gooru.skylineinitial.infra.services.learnerprofile;

import java.util.List;
import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.services.algebra.competency.Competency;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyAlgebraDefaultBuilder;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyMap;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class LearnerProfileProviderImpl implements LearnerProfileProvider {

  private final DBI dbi4ds;
  private LearnerProfileProviderDao dsDao;

  LearnerProfileProviderImpl(DBI dbi4ds) {
    this.dbi4ds = dbi4ds;
  }

  @Override
  public CompetencyLine findLearnerProfileForUser(ProcessingContext context) {
    List<Competency> competenciesForLP = fetchDsDao()
        .fetchProficiencyForUserInSpecifiedSubjectAndDomains(context.getUserId().toString(),
            context.getSubject());
    if (competenciesForLP != null && !competenciesForLP.isEmpty()) {
      return CompetencyMap.build(competenciesForLP).getCeilingLine();
    }
    return CompetencyAlgebraDefaultBuilder.getEmptyCompetencyLine();
  }

  private LearnerProfileProviderDao fetchDsDao() {
    if (dsDao == null) {
      dsDao = dbi4ds.onDemand(LearnerProfileProviderDao.class);
    }
    return dsDao;
  }

}
