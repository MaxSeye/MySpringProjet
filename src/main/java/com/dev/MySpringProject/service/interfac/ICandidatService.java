package com.dev.MySpringProject.service.interfac;

import com.dev.MySpringProject.dto.CandidatDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.entity.Candidat;

import java.util.List;

public interface ICandidatService {

    // Enregistrement d'un nouveau candidat
    Response registerCandidat(CandidatDTO candidatDTO);

    // Connexion d'un candidat
    Response loginCandidat(String email, String password);

    // Récupérer tous les candidats
    Response getAllCandidats();

    // Récupérer un candidat par son ID
    Response getCandidatById(Long id);

    // Mettre à jour les informations d'un candidat
    Response updateCandidat(Long id, CandidatDTO candidatDTO);

    // Supprimer un candidat
    Response deleteCandidat(Long id);

    // Recherche de candidats par critères
    Response searchCandidats(String pays, String ville, String domainDetude, String niveauDetude, String typeDeFormation);

    // Récupérer les candidatures en cours d'un candidat
    Response getCandidaturesEnCours(Long candidatId);

    // Récupérer les offres d'emploi favorites d'un candidat
    Response getFavorisOffreEmploi(Long candidatId);

    // Récupérer les notifications d'un candidat
    Response getNotifications(Long candidatId);

    // Récupérer les expériences d'un candidat
    Response getExperiences(Long candidatId);

    // Récupérer les formations d'un candidat
    Response getFormations(Long candidatId);

    // Ajouter une offre d'emploi aux favoris
    Response addFavorisOffreEmploi(Long candidatId, Long offreEmploiId);

    // Supprimer une offre d'emploi des favoris
    Response removeFavorisOffreEmploi(Long candidatId, Long offreEmploiId);

    // Postuler à une offre d'emploi
    Response postulerOffreEmploi(Long candidatId, Long offreEmploiId, CandidatDTO candidatureInfo);

    // Marquer une notification comme lue
    Response markNotificationAsRead(Long candidatId, Long notificationId);
}