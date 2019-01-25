package org.gooru.skylineinitial.infra.services.validators;

import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.services.subjectinferer.SubjectInferer;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The validator works on basis of whether it is "In Class" or IL experience.
 *
 * In case of "In Class" experience, validator validates the following:
 * <ul>
 * <li>Class is not deleted</li>
 * <li>Specified course is not deleted and is attached to specified class</li>
 * <li>Specified user is member of specified class</li>
 * </ul>
 *
 * In case of IL, the validation is:
 * <ul>
 * <li>The specified course is not deleted in system</li>
 * </ul>
 *
 * In both cases, the common validations are:
 * <ul>
 * <li>The course is tagged to a subject bucket</li>
 * </ul>
 *
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

    if (context.isInClassExperience()) {
      validateInClass();
    } else {
      validateForIL();
    }
  }

  private void validateForIL() {
    if (!getDao4Core().validateCourseExists(context.getCourseId())) {
      LOGGER.warn("Course: '{}' does not exist", context.getCourseId());
      throw new IllegalStateException("Course does not exist: " + context.getCourseId());
    }
    validateCommon();
  }

  private void validateInClass() {
    if (!getDao4Core().validateClassCourseUserCombo(context.getClassId(), context.getCourseId(),
        context.getUserId())) {
      LOGGER.warn("Course: '{}'; Class: '{}'; User: '{}' combination validation check failed",
          context.getCourseId(), context.getClassId(), context.getUserId());
      throw new IllegalStateException(
          "Course/Class/Member combination validation failed. Course: " + context.getCourseId()
              + ", Class: " + context.getClassId() + ", User: " + context.getUserId());
    }
    validateCommon();
  }

  private void validateCommon() {
    validatePresenceOfBaselinedLPForUser();
  }

  private void validatePresenceOfBaselinedLPForUser() {
    String subjectBucket = SubjectInferer.build(dbi4core)
        .inferSubjectForCourse(context.getCourseId());
    if (subjectBucket == null || subjectBucket.trim().isEmpty()) {
      LOGGER.warn("Subject bucket is not present or is empty for course: '{}'",
          context.getCourseId());
      throw new IllegalStateException("Subject bucket is not present or is empty for course: " +
          context.getCourseId());
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
