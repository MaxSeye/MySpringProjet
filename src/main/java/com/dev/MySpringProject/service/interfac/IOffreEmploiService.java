package com.dev.MySpringProject.service.interfac;

import com.dev.MySpringProject.dto.OffreEmploiDTO;
import com.dev.MySpringProject.dto.Response;

public interface IOffreEmploiService {

    // CRUD Standard
    Response createOffreEmploi(OffreEmploiDTO offreDTO);
    Response getOffreEmploiById(Long id);
    Response updateOffreEmploi(Long id, OffreEmploiDTO offreDTO);
    Response deleteOffreEmploi(Long id);

    // Méthodes de recherche
    Response getAllOffresEmploi();
    Response getOffresByTitre(String titre);
    Response getOffresByLocalisation(String localisation);
    Response getOffresByContrat(String contrat);
    Response getOffresByRecruteur(Long recruteurId);
    Response getOffresByAdministrateur(Long administrateurId);

    // Gestion des candidats
    Response addCandidatToOffre(Long offreId, Long candidatId);
    Response removeCandidatFromOffre(Long offreId, Long candidatId);
    Response getCandidatsByOffre(Long offreId);

    // Méthodes avancées
    Response searchOffres(String titre, String localisation, String contrat);
    Response getRecentOffres(int limit);
    Response countOffresByRecruteur(Long recruteurId);
}