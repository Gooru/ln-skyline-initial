package org.gooru.skylineinitial.infra.services.baselinedonehandler;

import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.skife.jdbi.v2.DBI;

public interface ILPDoneInformer {

  void inform(ProcessingContext context);

  static ILPDoneInformer build(DBI dbi4core) {
    return new ILPDoneInformerImpl(dbi4core);
  }

  static ILPDoneInformer build() {
    return new ILPDoneInformerImpl(DBICreator.getDbiForDefaultDS());
  }

}
