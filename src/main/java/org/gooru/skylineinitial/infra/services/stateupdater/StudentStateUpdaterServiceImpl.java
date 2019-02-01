package org.gooru.skylineinitial.infra.services.stateupdater;

import java.util.UUID;
import org.gooru.skylineinitial.infra.services.settings.SettingsModel;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class StudentStateUpdaterServiceImpl implements StudentStateUpdaterService {

  private final DBI dbi4core;
  private final SettingsModel settingsModel;
  private StudentStateUpdaterDao dao;

  StudentStateUpdaterServiceImpl(DBI dbi4core, SettingsModel settingsModel) {
    this.dbi4core = dbi4core;
    this.settingsModel = settingsModel;
  }

  @Override
  public void updateStateToDiagnosticNotNeeded() {
    fetchDao()
        .updateStateToDiagnosticNotNeeded(settingsModel.getClassId(), settingsModel.getStudentId());
  }

  @Override
  public void updateStateToDiagnosticSuggested(UUID diagnosticAsmtId) {
    fetchDao()
        .updateStateToDiagnosticSuggested(settingsModel.getClassId(), settingsModel.getStudentId(),
            diagnosticAsmtId);
  }

  @Override
  public void updateStateToDiagnosticNotAvailable() {
    // Do set ILP done flag as well
    fetchDao().updateStateToDiagnosticNotAvailableAndILPDone(settingsModel.getClassId(),
        settingsModel.getStudentId());
  }

  private StudentStateUpdaterDao fetchDao() {
    if (dao == null) {
      dao = dbi4core.onDemand(StudentStateUpdaterDao.class);
    }
    return dao;
  }

}
