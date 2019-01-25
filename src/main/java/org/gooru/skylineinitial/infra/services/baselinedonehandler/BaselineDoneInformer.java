package org.gooru.skylineinitial.infra.services.baselinedonehandler;

import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

public interface BaselineDoneInformer {

  void inform(ProcessingContext context);

  static BaselineDoneInformer build(DBI dbi4core) {
    return new BaselineDoneInformerImpl(dbi4core);
  }

  static BaselineDoneInformer build() {
    return new BaselineDoneInformerImpl(DBICreator.getDbiForDefaultDS());
  }

}
