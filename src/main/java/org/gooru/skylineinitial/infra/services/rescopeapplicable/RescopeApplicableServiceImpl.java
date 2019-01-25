package org.gooru.skylineinitial.infra.services.rescopeapplicable;

import java.util.UUID;
import org.gooru.skylineinitial.infra.components.AppConfiguration;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish
 */
final class RescopeApplicableServiceImpl implements RescopeApplicableService {

  private static final Logger LOGGER = LoggerFactory.getLogger(RescopeApplicableServiceImpl.class);
  private final DBI dbi;
  private RescopeApplicableDao dao;

  RescopeApplicableServiceImpl(DBI dbi) {
    this.dbi = dbi;
  }

  @Override
  public boolean isRescopeApplicableToClass(UUID classId) {
    RescopeApplicableDao dao = getRescopeApplicableDao();
    String courseId = dao.fetchCourseForClass(classId);
    if (courseId == null) {
      LOGGER.info("Course is not assigned to class '{}' hence rescope not applicable",
          classId.toString());
      return false;
    }
    return AppConfiguration.getInstance().getRescopeApplicableCourseVersion()
        .equals(dao.fetchCourseVersion(courseId));
  }

  @Override
  public boolean isRescopeApplicableToCourseInIL(UUID courseId) {
    RescopeApplicableDao dao = getRescopeApplicableDao();
    return AppConfiguration.getInstance().getRescopeApplicableCourseVersion()
        .equals(dao.fetchCourseVersion(courseId));
  }

  private RescopeApplicableDao getRescopeApplicableDao() {
    if (dao == null) {
      dao = dbi.onDemand(RescopeApplicableDao.class);
    }
    return dao;
  }

}
