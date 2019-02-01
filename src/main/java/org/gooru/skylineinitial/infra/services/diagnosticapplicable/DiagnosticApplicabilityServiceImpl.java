package org.gooru.skylineinitial.infra.services.diagnosticapplicable;

import org.gooru.skylineinitial.infra.services.diagnosticfetcher.DiagnosticFetcherService;
import org.gooru.skylineinitial.infra.services.settings.SettingsModel;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class DiagnosticApplicabilityServiceImpl implements DiagnosticApplicabilityService {

  private final DBI dbi4ds;
  private final SettingsModel settingsModel;
  private DiagnosticApplicabilityDao dao;
  private static final Logger LOGGER = LoggerFactory.getLogger(DiagnosticFetcherService.class);
  private String subjectCode;

  DiagnosticApplicabilityServiceImpl(DBI dbi4ds, SettingsModel settingsModel) {
    this.dbi4ds = dbi4ds;
    this.settingsModel = settingsModel;
  }

  @Override
  public boolean isDiagnosticApplicable() {

    initializeSubjectCode();

    int domainsCountInGradeForStudentOrigin = fetchDao()
        .fetchDomainsCountInGrade(subjectCode, settingsModel.getStudentGradeLowerBound());
    if (domainsCountInGradeForStudentOrigin == 0) {
      LOGGER.warn("No domains found for grade: '{}'", settingsModel.getStudentGradeLowerBound());
      throw new IllegalStateException("No domains found for grade: " +
          settingsModel.getStudentGradeLowerBound());
    }

    int masteredDomainsCount = fetchDao().fetchDomainsCountMasteredByUserInGrade(subjectCode,
        settingsModel.getStudentGradeLowerBound(), settingsModel.getStudentId().toString());

    return domainsCountInGradeForStudentOrigin > masteredDomainsCount;
  }

  private void initializeSubjectCode() {
    subjectCode = fetchDao()
        .fetchSubjectCodeForGradeId(settingsModel.getStudentGradeLowerBound());
    if (subjectCode == null) {
      LOGGER.warn("Not able to find subject for grade: '{}'",
          settingsModel.getStudentGradeLowerBound());
      throw new IllegalStateException(
          "Not able to find subject for grade: " + settingsModel.getStudentGradeLowerBound());
    }
  }

  private DiagnosticApplicabilityDao fetchDao() {
    if (dao == null) {
      dao = dbi4ds.onDemand(DiagnosticApplicabilityDao.class);
    }
    return dao;
  }
}
