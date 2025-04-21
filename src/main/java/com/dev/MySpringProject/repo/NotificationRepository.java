package com.dev.MySpringProject.repo;


import com.dev.MySpringProject.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Méthode pour trouver des notifications par candidat
    List<Notification> findByCandidatId(Long candidatId);

    // Méthode pour trouver des notifications par recruteur
    List<Notification> findByRecruteurId(Long recruteurId);

    // Méthode pour trouver des notifications par statut
    List<Notification> findByStatut(String statut);

    // Méthode pour trouver des notifications par contenu (recherche insensible à la casse)
    List<Notification> findByContenuContainingIgnoreCase(String contenu);

    // Dans NotificationRepository
    List<Notification> findByCandidatIdAndStatut(Long candidatId, String statut);
    List<Notification> findByRecruteurIdAndStatut(Long recruteurId, String statut);
    List<Notification> findByCandidatIdOrderByIdDesc(Long candidatId);
    List<Notification> findByRecruteurIdOrderByIdDesc(Long recruteurId);
}
