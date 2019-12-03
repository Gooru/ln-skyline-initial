
package org.gooru.skylineinitial.infra.services.learnerprofile.postprocessing;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.gooru.skylineinitial.infra.jdbi.DBICreator;
import org.gooru.skylineinitial.infra.services.learnerprofile.LearnerProfilePersisterModel;

/**
 * @author szgooru Created On 19-Nov-2019
 */
public class StrugglingCompetencyRemoverProcessor {

  private final LearnerProfilePersisterModel model;
  private final static StrugglingCompetencyRemoverService SERVICE =
      new StrugglingCompetencyRemoverService(DBICreator.getDbiForDsdbDS());

  public StrugglingCompetencyRemoverProcessor(LearnerProfilePersisterModel model) {
    this.model = model;
  }

  public void process() {
    String subject = model.getSubjectCode();
    List<String> gutCodes = model.getGutCodes();
    Timestamp toDate = Timestamp.valueOf(LocalDateTime.now());

    // Fetch all inferred and completed competencies from user skyline so that it can be removed
    // from the struggling competencies
    Set<String> completedAndInferredCompetencies =
        SERVICE.fetchCompletedAndInferredCompetencies(model.getUserId(), subject, toDate);

    gutCodes.forEach(gut -> {
      if (completedAndInferredCompetencies.contains(gut)) {
        SERVICE.removeStrugglingCompetency(model.getUserId(), gut);
      }
    });
  }
}
