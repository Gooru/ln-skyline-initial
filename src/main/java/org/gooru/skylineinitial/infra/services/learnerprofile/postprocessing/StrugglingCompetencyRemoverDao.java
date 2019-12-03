
package org.gooru.skylineinitial.infra.services.learnerprofile.postprocessing;

import java.sql.Timestamp;
import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author szgooru Created On 19-Nov-2019
 */
public interface StrugglingCompetencyRemoverDao {   

  @Mapper(UserDomainCompetencyMatrixModelMapper.class)
  @SqlQuery("select distinct(cm.tx_comp_code), cm.tx_domain_code, cm.tx_comp_name, cm.tx_comp_desc, cm.tx_comp_student_desc, cm.tx_comp_seq,"
      + " (SELECT DISTINCT ON (lpcs.gut_code) FIRST_VALUE(lpcs.status) OVER (PARTITION BY lpcs.gut_code ORDER BY lpcs.updated_at desc) as status"
      + " FROM learner_profile_competency_status_ts lpcs where lpcs.user_id = :user and lpcs.gut_code = ucm.gut_code and lpcs.updated_at < :toDate)"
      + " as status from domain_competency_matrix cm left join learner_profile_competency_status_ts"
      + " ucm on cm.tx_subject_code = ucm.tx_subject_code and cm.tx_comp_code = ucm.gut_code and ucm.user_id = :user and "
      + " ucm.updated_at < :toDate where cm.tx_subject_code = :subject order by cm.tx_domain_code,"
      + " cm.tx_comp_seq asc")
  public List<UserDomainCompetencyMatrixModel> fetchUserSkyline(
      @Bind("user") String user, @Bind("subject") String subject, @Bind("toDate") Timestamp toDate);
  
  @SqlUpdate("DELETE FROM struggling_competencies_details WHERE user_id = :user AND tx_comp_code = :competency")
  public void removeStruggling(@Bind("user") String user, @Bind("competency") String competency);
  
}
