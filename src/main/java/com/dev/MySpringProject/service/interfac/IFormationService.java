package com.dev.MySpringProject.service.interfac;

import com.dev.MySpringProject.dto.FormationDTO;
import com.dev.MySpringProject.dto.Response;

public interface IFormationService {

    // CRUD Standard
    Response createFormation(FormationDTO formationDTO);
    Response getFormationById(Long id);
    Response updateFormation(Long id, FormationDTO formationDTO);
    Response deleteFormation(Long id);

    // Méthodes de recherche
    Response getFormationsByCandidat(Long candidatId);
    Response getFormationsByEcole(String ecole);
    Response getFormationsByDiplome(String diplome);
    Response getFormationsByAnneeScolaire(String anneeScolaire);
    Response getFormationsByEcoleAndDiplome(String ecole, String diplome);

    // Méthodes avancées
    Response searchFormations(Long candidatId, String ecole, String diplome, String anneeScolaire);
}