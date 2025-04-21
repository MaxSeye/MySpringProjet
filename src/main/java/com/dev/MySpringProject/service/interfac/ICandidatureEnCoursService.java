package com.dev.MySpringProject.service.interfac;

import com.dev.MySpringProject.dto.CandidatureEnCoursDTO;
import com.dev.MySpringProject.dto.Response;

import java.util.List;

public interface ICandidatureEnCoursService {

    // CRUD Standard
    Response createCandidatureEnCours(CandidatureEnCoursDTO candidatureDTO);
    Response getCandidatureEnCoursById(Long id);
    Response updateCandidatureEnCours(Long id, CandidatureEnCoursDTO candidatureDTO);
    Response deleteCandidatureEnCours(Long id);

    // Méthodes spécifiques
    Response getCandidaturesByCandidat(Long candidatId);
    Response getCandidaturesByStatutDiscussion(String statutDiscussion);
    Response getCandidaturesByStatutSelection(String statutSelection);
    Response getCandidaturesByStatuts(String statutDiscussion, String statutSelection);

    // Méthodes de gestion des statuts
    Response updateStatutDiscussion(Long id, String newStatut);
    Response updateStatutSelection(Long id, String newStatut);

    // Méthodes de recherche avancée
    Response searchCandidatures(Long candidatId, String statutDiscussion, String statutSelection);
}