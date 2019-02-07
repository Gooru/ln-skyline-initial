package org.gooru.skylineinitial.infra.services.learnerprofile;

import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.services.algebra.competency.CompetencyLine;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class LearnerProfilePersisterFromDiagnosticPlay implements LearnerProfilePersister {

  private final DBI dbi4ds;
  private final ProcessingContext context;
  private LearnerProfilePersisterDao learnerProfilePersisterDao;
  private static final Logger LOGGER = LoggerFactory.getLogger(LearnerProfilePersister.class);

  LearnerProfilePersisterFromDiagnosticPlay(DBI dbi4ds, ProcessingContext context) {
    this.dbi4ds = dbi4ds;
    this.context = context;
  }

  @Override
  public void persistLearnerProfile(CompetencyLine skyline) {
    try {
      LOGGER.debug("LPCS Update will be done");
      LearnerProfilePersisterModel model = LearnerProfilePersisterModelBuilder
          .buildForDiagnostic(context, skyline);

      LOGGER.debug("Will try to update LPCS");
      fetchDao().persistLearnerProfileCompetencyStatus(model, model.getGutCodes());
      LOGGER.debug("Will try to update LPCS TS");
      fetchDao().persistLearnerProfileCompetencyStatusTS(model, model.getGutCodes());
      LOGGER.debug("Will try to update LPCE");
      fetchDao().persistLearnerProfileCompetencyEvidence(model, model.getGutCodes());
      LOGGER.debug("Will try to update LPCE TS");
      fetchDao().persistLearnerProfileCompetencyEvidenceTS(model, model.getGutCodes());
    } catch (Throwable throwable) {
      LOGGER.warn("Exception updating LP. Aborting", throwable);
    }
  }

  private LearnerProfilePersisterDao fetchDao() {
    if (learnerProfilePersisterDao == null) {
      learnerProfilePersisterDao = dbi4ds.onDemand(LearnerProfilePersisterDao.class);
    }
    return learnerProfilePersisterDao;
  }

}
