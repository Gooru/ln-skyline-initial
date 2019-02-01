package org.gooru.skylineinitial.infra.services.stateverifier;

import org.gooru.skylineinitial.infra.services.settings.SettingsModel;

public interface ClassAndStudentStateVerifierService {

  boolean isClassNonNavigator();

  boolean isClassSetupCompleted();

  boolean isStudentSetupDone();

  boolean isStudentBaselineDone();

  boolean isStudentILPDone();

  static ClassAndStudentStateVerifierService build(SettingsModel model) {
    return new ClassAndStudentStateVerifierServiceImpl(model);
  }

}
