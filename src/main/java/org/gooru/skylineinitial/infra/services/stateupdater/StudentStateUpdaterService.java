package org.gooru.skylineinitial.infra.services.stateupdater;

import java.util.UUID;
import org.gooru.skylineinitial.infra.services.settings.SettingsModel;
import org.skife.jdbi.v2.DBI;

public interface StudentStateUpdaterService {

  void updateStateToDiagnosticNotNeeded();

  void updateStateToDiagnosticSuggested(UUID diagnosticAsmtId);

  /*
   * Handles the auto update of state of ILP Done as well
   */
  void updateStateToDiagnosticNotAvailable();

  static StudentStateUpdaterService build(DBI dbi4core, SettingsModel settingsModel) {
    return new StudentStateUpdaterServiceImpl(dbi4core, settingsModel);
  }
}
