package org.gooru.skylineinitial.infra.services.stateverifier;

import org.gooru.skylineinitial.infra.services.settings.SettingsModel;

/**
 * @author ashish.
 */

class ClassAndStudentStateVerifierServiceImpl implements ClassAndStudentStateVerifierService {

  private final SettingsModel model;

  ClassAndStudentStateVerifierServiceImpl(SettingsModel model) {
    this.model = model;
  }

  @Override
  public boolean isClassNonNavigator() {
    return model.isClassNonNavigator();
  }

  @Override
  public boolean isClassSetupCompleted() {
    return model.getClassGradeCurrent() != null;
  }

  @Override
  public boolean isStudentSetupDone() {
    return model.getStudentGradeLowerBound() != null;
  }

  @Override
  public boolean isStudentBaselineDone() {
    return model.isProfileBaselineDone();
  }

  @Override
  public boolean isStudentILPDone() {
    return model.isInitialLPDone();
  }
}
