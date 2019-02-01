package org.gooru.skylineinitial.infra.services.diagnosticfetcher;

import java.util.UUID;
import org.gooru.skylineinitial.infra.services.settings.SettingsModel;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class DiagnosticFetcherServiceImpl implements DiagnosticFetcherService {

  private final DBI dbi4Core;
  private final SettingsModel settingsModel;
  private DiagnosticFetcherDao dao;

  DiagnosticFetcherServiceImpl(DBI dbi4Core, SettingsModel settingsModel) {
    this.dbi4Core = dbi4Core;
    this.settingsModel = settingsModel;
  }

  @Override
  public UUID fetchDiagnosticAssessment() {
    return fetchDao().fetchDiagnosticAssessmentForGrade(settingsModel.getStudentGradeLowerBound());
  }

  private DiagnosticFetcherDao fetchDao() {
    if (dao == null) {
      dao = dbi4Core.onDemand(DiagnosticFetcherDao.class);
    }
    return dao;
  }
}
