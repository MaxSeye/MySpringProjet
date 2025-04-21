package com.dev.MySpringProject.service.interfac;

import com.dev.MySpringProject.dto.RecruteurDTO;
import com.dev.MySpringProject.dto.Response;

public interface IRecruteurService {

    // CRUD Standard
    Response registerRecruteur(RecruteurDTO recruteurDTO);
    Response getRecruteurById(Long id);
    Response updateRecruteur(Long id, RecruteurDTO recruteurDTO);
    Response deleteRecruteur(Long id);

    // Authentification
    Response loginRecruteur(String email, String password);

    // Méthodes de recherche
    Response getRecruteursBySociete(String nomSociete);
    Response getRecruteursByPays(String pays);
    Response getRecruteursByVille(String ville);
    Response getRecruteursByDomaineActivite(String domaine);
    Response checkSocieteExists(String nomSociete);

    // Gestion des relations
    Response getOffresByRecruteur(Long recruteurId);
    Response getNotificationsByRecruteur(Long recruteurId);
    Response getCandidatsByRecruteur(Long recruteurId);

    // Statistiques
    Response getRecruteurStats(Long recruteurId);
}