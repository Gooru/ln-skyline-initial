package org.gooru.skylineinitial.infra.services.learnerprofile;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlBatch;

/**
 * @author ashish.
 */

interface LearnerProfilePersisterDao {

  @SqlBatch(
      "INSERT INTO learner_profile_competency_status(tx_subject_code, user_id, gut_code, status, profile_source, created_at, updated_at) "
          + " VALUES(:subjectCode, :userId, :competencies, :status, :profileSource, now(), now()) ON CONFLICT (user_id, gut_code) "
          + " DO UPDATE SET status = :status, profile_source = :profileSource, updated_at = now() WHERE "
          + " learner_profile_competency_status.status <= EXCLUDED.status")
  void persistLearnerProfileCompetencyStatus(@BindBean LearnerProfilePersisterModel model,
      @Bind("competencies") List<String> competencies);

  @SqlBatch(
      "INSERT INTO learner_profile_competency_status_ts(tx_subject_code, user_id, gut_code, status, created_at, updated_at) "
          + "VALUES(:subjectCode, :userId, :competencies, :status, now(), now()) ON CONFLICT (user_id, gut_code, status) DO NOTHING")
  void persistLearnerProfileCompetencyStatusTS(@BindBean LearnerProfilePersisterModel model,
      @Bind("competencies") List<String> competencies);


  @SqlBatch(
      "INSERT INTO learner_profile_competency_evidence(user_id, gut_code, class_id, course_id, unit_id, lesson_id, latest_session_id,"
          + " collection_id, collection_path_id, collection_score, collection_type, content_source, created_at, updated_at) "
          + " VALUES (:userId, :competencies, :classId, null, null, null, :latestSessionId, :collectionId, null, :collectionScore, 'assessment', "
          + " 'diagnostic', now(), now()) ON CONFLICT (user_id, gut_code, collection_id) DO UPDATE SET latest_session_id = :latestSessionId, "
          + " class_id = :classId, course_id = null, unit_id = null, lesson_id = null, collection_path_id = null, collection_score ="
          + " :collectionScore, content_source = 'diagnostic', updated_at = now()")
  void persistLearnerProfileCompetencyEvidence(@BindBean LearnerProfilePersisterModel model,
      @Bind("competencies") List<String> competencies);


  @SqlBatch(
      "INSERT INTO learner_profile_competency_evidence_ts(user_id, gut_code, class_id, course_id, unit_id, lesson_id, latest_session_id,"
          + " collection_id, collection_path_id, collection_score, collection_type, content_source, status, created_at, updated_at) "
          + " VALUES (:userId, :competencies, :classId, null, null, null, :latestSessionId, :collectionId, null, :collectionScore, "
          + " 'assessment', 'diagnostic', :status, now(), now()) ON CONFLICT (user_id, gut_code, collection_id, status) "
          + " DO UPDATE SET latest_session_id = :latestSessionId, collection_path_id = null, collection_score = :collectionScore, "
          + " content_source = 'diagnostic', updated_at = now()")
  void persistLearnerProfileCompetencyEvidenceTS(@BindBean LearnerProfilePersisterModel model,
      @Bind("competencies") List<String> competencies);


}
