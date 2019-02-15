package org.gooru.skylineinitial.infra.services.validators;

import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish.
 */

class ContextValidatorImpl implements ContextValidator {

  private final DBI dbi4core;
  private final DBI dbi4ds;
  private static final Logger LOGGER = LoggerFactory.getLogger(ContextValidatorImpl.class);
  private ContextCoreValidatorDao dao4core = null;
  private ContextDsValidatorDao dao4ds = null;
  private ProcessingContext context;

  ContextValidatorImpl(DBI dbi4core, DBI dbi4ds) {
    this.dbi4core = dbi4core;
    this.dbi4ds = dbi4ds;
  }

  @Override
  public void validate(ProcessingContext context) {
    this.context = context;
    if (!getDao4Core().validateClassCourseUserCombo(context.getClassId(), context.getCourseId(),
        context.getUserId())) {
      LOGGER.warn("Course: '{}'; Class: '{}'; User: '{}' combination validation check failed",
          context.getCourseId(), context.getClassId(), context.getUserId());
      throw new IllegalStateException(
          "Course/Class/Member combination validation failed. Course: " + context.getCourseId()
              + ", Class: " + context.getClassId() + ", User: " + context.getUserId());
    }
  }

  private ContextCoreValidatorDao getDao4Core() {
    if (dao4core == null) {
      dao4core = dbi4core.onDemand(ContextCoreValidatorDao.class);
    }
    return dao4core;
  }

  private ContextDsValidatorDao getDao4Ds() {
    if (dao4ds == null) {
      dao4ds = dbi4ds.onDemand(ContextDsValidatorDao.class);
    }
    return dao4ds;
  }

}
