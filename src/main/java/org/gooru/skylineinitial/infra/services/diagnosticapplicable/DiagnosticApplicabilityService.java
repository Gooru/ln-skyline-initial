package org.gooru.skylineinitial.infra.services.diagnosticapplicable;

import org.gooru.skylineinitial.infra.services.settings.SettingsModel;
import org.skife.jdbi.v2.DBI;

public interface DiagnosticApplicabilityService {

  boolean isDiagnosticApplicable();

  static DiagnosticApplicabilityService build(DBI dbi4ds, SettingsModel settingsModel) {
    return new DiagnosticApplicabilityServiceImpl(dbi4ds, settingsModel);
  }
}
