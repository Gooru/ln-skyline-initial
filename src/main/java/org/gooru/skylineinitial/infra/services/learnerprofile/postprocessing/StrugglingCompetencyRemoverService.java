
package org.gooru.skylineinitial.infra.services.learnerprofile.postprocessing;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.gooru.skylineinitial.infra.constants.Constants;
import org.skife.jdbi.v2.DBI;

/**
 * @author szgooru Created On 19-Nov-2019
 */
public class StrugglingCompetencyRemoverService {
  
  private final StrugglingCompetencyRemoverDao dao;

  public StrugglingCompetencyRemoverService(DBI dbi) {
    this.dao = dbi.onDemand(StrugglingCompetencyRemoverDao.class);
  }
  
  public void removeStrugglingCompetency(String user, String competency) {
    this.dao.removeStruggling(user, competency);
  }
  
  public Set<String> fetchCompletedAndInferredCompetencies(String user, String subject, Timestamp toDate) {
    
    // Fetch user skyline 
    List<UserDomainCompetencyMatrixModel> userSkyline = this.dao.fetchUserSkyline(user, subject, toDate);
    
    // Collect all the inferred and completed competencies 
    Set<String> allCompetencies =  new HashSet<>();
    
    List<UserDomainCompetencyMatrixModel> completed = userSkyline.stream()
        .filter(model -> model.getStatus() >= Constants.LPStatus.COMPLETED)
        .collect(Collectors.toList());
    
    Map<String, Map<String, UserDomainCompetencyMatrixModel>> completedCompMatrixMap =
        new HashMap<>();
    completed.forEach(model -> {
      String domain = model.getDomainCode();
      String compCode = model.getCompetencyCode();
      allCompetencies.add(compCode);
      if (completedCompMatrixMap.containsKey(domain)) {
        Map<String, UserDomainCompetencyMatrixModel> competencies =
            completedCompMatrixMap.get(domain);
        competencies.put(compCode, model);
        completedCompMatrixMap.put(domain, competencies);
      } else {
        Map<String, UserDomainCompetencyMatrixModel> competencies = new HashMap<>();
        competencies.put(compCode, model);
        completedCompMatrixMap.put(domain, competencies);
      }
    });

    userSkyline.forEach(model -> {
      String domainCode = model.getDomainCode();
      int sequence = model.getCompetencySeq();
      int status = model.getStatus();

      if (completedCompMatrixMap.containsKey(domainCode)) {
        Map<String, UserDomainCompetencyMatrixModel> competencies =
            completedCompMatrixMap.get(domainCode);
        for (Map.Entry<String, UserDomainCompetencyMatrixModel> entry : competencies.entrySet()) {
          UserDomainCompetencyMatrixModel compModel = entry.getValue();
          int compSeq = compModel.getCompetencySeq();

          if (sequence < compSeq && status < Constants.LPStatus.ASSERTED) {
            allCompetencies.add(model.getCompetencyCode());
          }
        }
      }
    });
    
    return allCompetencies;
  }
}