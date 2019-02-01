package org.gooru.skylineinitial.infra.services.settings;

import java.util.UUID;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class SettingsModelBuilder {


  private final DBI dbi4core;
  private final UUID classId;
  private final UUID studentId;

  SettingsModelBuilder(DBI dbi4core, UUID classId, UUID studentId) {

    this.dbi4core = dbi4core;
    this.classId = classId;
    this.studentId = studentId;
  }

  SettingsModel build() {
    SettingsModelDao dao = dbi4core.onDemand(SettingsModelDao.class);

    SettingsDbModel dbModel = dao
        .createSettingDbModelForSpecifiedClassAndStudent(classId, studentId);

    if (dbModel == null) {
      return null;
    }

    Boolean isCoursePremium = dao.isCoursePremium(dbModel.getCourseId());

    return buildSettingsModel(dbModel, isCoursePremium);
  }

  private SettingsModel buildSettingsModel(SettingsDbModel dbModel, Boolean isCoursePremium) {
    return new SettingsModelImpl(dbModel, isCoursePremium);
  }
}
