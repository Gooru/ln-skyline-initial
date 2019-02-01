package org.gooru.skylineinitial.infra.services.stateupdater;

import java.util.UUID;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

/**
 * @author ashish.
 */

interface StudentStateUpdaterDao {

  @SqlUpdate("update class_member set diag_asmt_state = 1 where class_id = :classId and user_id = :studentId")
  void updateStateToDiagnosticNotNeeded(@Bind("classId") UUID classId,
      @Bind("studentId") UUID studentId);

  @SqlUpdate("update class_member set diag_asmt_state = 2, diag_asmt_assigned = :diagnosticAsmtId where class_id = :classId and user_id = :studentId")
  void updateStateToDiagnosticSuggested(@Bind("classId") UUID classId,
      @Bind("studentId") UUID studentId, @Bind("diagnosticAsmtId") UUID diagnosticAsmtId);

  @SqlUpdate("update class_member set diag_asmt_state = 4, initial_lp_done = true where class_id = :classId and user_id = :studentId")
  void updateStateToDiagnosticNotAvailableAndILPDone(@Bind("classId") UUID classId,
      @Bind("studentId") UUID studentId);

}
