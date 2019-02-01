package org.gooru.skylineinitial.infra.services.diagnosticfetcher;

import java.util.UUID;
import org.gooru.skylineinitial.infra.services.settings.SettingsModel;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface DiagnosticFetcherService {

  UUID fetchDiagnosticAssessment();

  static DiagnosticFetcherService build(DBI dbi4Core, SettingsModel settingsModel) {
    return new DiagnosticFetcherServiceImpl(dbi4Core, settingsModel);
  }
}
