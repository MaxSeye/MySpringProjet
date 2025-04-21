package com.dev.MySpringProject.repo;


import com.dev.MySpringProject.entity.CandidatureEnCours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatureEnCoursRepository extends JpaRepository<CandidatureEnCours, Long> {

    // Méthode pour trouver des candidatures en cours par candidat
    List<CandidatureEnCours> findByCandidatId(Long candidatId);

    // Méthode pour trouver des candidatures en cours par statut de discussion
    List<CandidatureEnCours> findByStatutDiscussion(String statutDiscussion);

    // Méthode pour trouver des candidatures en cours par statut de sélection
    List<CandidatureEnCours> findByStatutSelection(String statutSelection);

    // Méthode pour trouver des candidatures en cours par statut de discussion et de sélection
    List<CandidatureEnCours> findByStatutDiscussionAndStatutSelection(String statutDiscussion, String statutSelection);

    List<CandidatureEnCours> findByCandidatIdAndStatutDiscussion(Long candidatId, String statutDiscussion);

    List<CandidatureEnCours> findByCandidatIdAndStatutSelection(Long candidatId, String statutSelection);

    List<CandidatureEnCours> findByCandidatIdAndStatutDiscussionAndStatutSelection(
            Long candidatId, String statutDiscussion, String statutSelection);
}
