package org.gooru.skylineinitial.infra.services.r0applicable;

import java.util.UUID;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

public interface Route0ApplicableService {

  boolean isRoute0ApplicableToClass(UUID classId);

  boolean isRoute0ApplicableToCourseInIL(UUID courseId);

  static Route0ApplicableService build(DBI dbi) {
    return new Route0ApplicableServiceImpl(dbi);
  }

  static Route0ApplicableService build() {
    return new Route0ApplicableServiceImpl(DBICreator.getDbiForDefaultDS());
  }
}
