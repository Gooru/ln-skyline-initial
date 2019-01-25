package org.gooru.skylineinitial.infra.services.rescopeapplicable;

import java.util.UUID;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface RescopeApplicableService {

  boolean isRescopeApplicableToClass(UUID classId);

  boolean isRescopeApplicableToCourseInIL(UUID courseId);

  static RescopeApplicableService build(DBI dbi) {
    return new RescopeApplicableServiceImpl(dbi);
  }

  static RescopeApplicableService build() {
    return new RescopeApplicableServiceImpl(DBICreator.getDbiForDefaultDS());
  }
}
