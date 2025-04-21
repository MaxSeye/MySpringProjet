package com.dev.MySpringProject.service.interfac;

import com.dev.MySpringProject.dto.ExperienceDTO;
import com.dev.MySpringProject.dto.Response;

public interface IExperienceService {

    // CRUD Standard
    Response createExperience(ExperienceDTO experienceDTO);
    Response getExperienceById(Long id);
    Response updateExperience(Long id, ExperienceDTO experienceDTO);
    Response deleteExperience(Long id);

    // Méthodes de recherche
    Response getExperiencesByCandidat(Long candidatId);
    Response getExperiencesByPoste(String poste);
    Response getExperiencesByEntreprise(String entreprise);
    Response getExperiencesByPosteAndEntreprise(String poste, String entreprise);
    Response getExperiencesByDateDebut(String dateDebut);
    Response getExperiencesByDateFin(String dateFin);

    // Méthodes avancées
    Response searchExperiences(Long candidatId, String poste, String entreprise, String dateDebut, String dateFin);
    Response getCurrentExperiences(Long candidatId); // Expériences en cours (dateFin null ou future)
}