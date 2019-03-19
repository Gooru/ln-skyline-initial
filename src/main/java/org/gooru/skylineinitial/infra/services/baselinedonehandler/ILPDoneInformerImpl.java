package org.gooru.skylineinitial.infra.services.baselinedonehandler;

import org.gooru.skylineinitial.infra.data.ProcessingContext;
import org.gooru.skylineinitial.infra.data.StudentDiagnosticState;
import org.skife.jdbi.v2.DBI;

/**
 * @author ashish.
 */

class ILPDoneInformerImpl implements ILPDoneInformer {

  private ProcessingContext context;
  private final DBI dbi4core;
  private ILPDoneInformerDao dao;

  ILPDoneInformerImpl(DBI dbi4core) {
    this.dbi4core = dbi4core;
  }

  @Override
  public void inform(ProcessingContext context) {
    this.context = context;
    if (context.getClassId() != null) {
      updateStatusForClassMembership();
    }
  }

  private void updateStatusForClassMembership() {
    if (context.getSettingsModel().isClassSetupToForceCalculateILP()) {
      fetchDao().updateDoneStatusForClassMember(context.getClassId().toString(),
          context.getUserId().toString(), StudentDiagnosticState.FORCE_CALCULATE.getValue());
    } else {
      fetchDao().updateDoneStatusForClassMember(context.getClassId().toString(),
          context.getUserId().toString(), StudentDiagnosticState.DONE.getValue());
    }
  }

  private ILPDoneInformerDao fetchDao() {
    if (dao == null) {
      dao = dbi4core.onDemand(ILPDoneInformerDao.class);
    }
    return dao;
  }
}
