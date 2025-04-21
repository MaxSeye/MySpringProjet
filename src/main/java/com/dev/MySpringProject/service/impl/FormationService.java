package com.dev.MySpringProject.service.impl;

import com.dev.MySpringProject.dto.FormationDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.entity.Candidat;
import com.dev.MySpringProject.entity.Formation;
import com.dev.MySpringProject.repo.CandidatRepository;
import com.dev.MySpringProject.repo.FormationRepository;
import com.dev.MySpringProject.service.interfac.IFormationService;
import com.dev.MySpringProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FormationService implements IFormationService {

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private CandidatRepository candidatRepository;

    @Override
    public Response createFormation(FormationDTO formationDTO) {
        try {
            Optional<Candidat> candidatOptional = candidatRepository.findById(formationDTO.getCandidat().getId());
            if (candidatOptional.isEmpty()) {
                return getNotFoundResponse("Candidat non trouvé");
            }

            Formation formation = new Formation();
            formation.setEcole(formationDTO.getEcole());
            formation.setDiplome(formationDTO.getDiplome());
            formation.setAnneeScolaire(formationDTO.getAnneeScolaire());
            formation.setCandidat(candidatOptional.get());

            Formation savedFormation = formationRepository.save(formation);

            Response response = new Response();
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setMessage("Formation créée avec succès");
            response.setFormation(Utils.mapFormationEntityToFormationDTO(savedFormation));
            return response;
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response getFormationById(Long id) {
        try {
            Optional<Formation> formationOptional = formationRepository.findById(id);
            if (formationOptional.isPresent()) {
                Response response = new Response();
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Formation trouvée");
                response.setFormation(Utils.mapFormationEntityToFormationDTO(formationOptional.get()));
                return response;
            } else {
                return getNotFoundResponse("Formation non trouvée");
            }
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response updateFormation(Long id, FormationDTO formationDTO) {
        try {
            Optional<Formation> formationOptional = formationRepository.findById(id);
            if (formationOptional.isEmpty()) {
                return getNotFoundResponse("Formation non trouvée");
            }

            Optional<Candidat> candidatOptional = candidatRepository.findById(formationDTO.getCandidat().getId());
            if (candidatOptional.isEmpty()) {
                return getNotFoundResponse("Candidat non trouvé");
            }

            Formation formation = formationOptional.get();
            formation.setEcole(formationDTO.getEcole());
            formation.setDiplome(formationDTO.getDiplome());
            formation.setAnneeScolaire(formationDTO.getAnneeScolaire());
            formation.setCandidat(candidatOptional.get());

            Formation updatedFormation = formationRepository.save(formation);

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Formation mise à jour avec succès");
            response.setFormation(Utils.mapFormationEntityToFormationDTO(updatedFormation));
            return response;
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response deleteFormation(Long id) {
        try {
            Optional<Formation> formationOptional = formationRepository.findById(id);
            if (formationOptional.isPresent()) {
                formationRepository.deleteById(id);
                Response response = new Response();
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Formation supprimée avec succès");
                return response;
            } else {
                return getNotFoundResponse("Formation non trouvée");
            }
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response getFormationsByCandidat(Long candidatId) {
        try {
            List<Formation> formations = formationRepository.findByCandidatId(candidatId);
            List<FormationDTO> formationsDTO = formations.stream()
                    .map(Utils::mapFormationEntityToFormationDTO)
                    .collect(Collectors.toList());

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Liste des formations du candidat");
            response.setFormationList(formationsDTO);
            return response;
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response getFormationsByEcole(String ecole) {
        try {
            List<Formation> formations = formationRepository.findByEcole(ecole);
            List<FormationDTO> formationsDTO = formations.stream()
                    .map(Utils::mapFormationEntityToFormationDTO)
                    .collect(Collectors.toList());

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Liste des formations par école");
            response.setFormationList(formationsDTO);
            return response;
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response getFormationsByDiplome(String diplome) {
        try {
            List<Formation> formations = formationRepository.findByDiplome(diplome);
            List<FormationDTO> formationsDTO = formations.stream()
                    .map(Utils::mapFormationEntityToFormationDTO)
                    .collect(Collectors.toList());

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Liste des formations par diplôme");
            response.setFormationList(formationsDTO);
            return response;
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response getFormationsByAnneeScolaire(String anneeScolaire) {
        try {
            List<Formation> formations = formationRepository.findByAnneeScolaire(anneeScolaire);
            List<FormationDTO> formationsDTO = formations.stream()
                    .map(Utils::mapFormationEntityToFormationDTO)
                    .collect(Collectors.toList());

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Liste des formations par année scolaire");
            response.setFormationList(formationsDTO);
            return response;
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response getFormationsByEcoleAndDiplome(String ecole, String diplome) {
        try {
            List<Formation> formations = formationRepository.findByEcoleAndDiplome(ecole, diplome);
            List<FormationDTO> formationsDTO = formations.stream()
                    .map(Utils::mapFormationEntityToFormationDTO)
                    .collect(Collectors.toList());

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Liste des formations par école et diplôme");
            response.setFormationList(formationsDTO);
            return response;
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response searchFormations(Long candidatId, String ecole, String diplome, String anneeScolaire) {
        try {
            List<Formation> formations;

            if (candidatId != null) {
                formations = formationRepository.findByCandidatId(candidatId);
            } else {
                formations = formationRepository.findAll();
            }

            // Filtrer par les autres critères si fournis
            List<Formation> filteredFormations = formations.stream()
                    .filter(formation -> ecole == null || formation.getEcole().contains(ecole))
                    .filter(formation -> diplome == null || formation.getDiplome().contains(diplome))
                    .filter(formation -> anneeScolaire == null || formation.getAnneeScolaire().contains(anneeScolaire))
                    .collect(Collectors.toList());

            List<FormationDTO> formationsDTO = filteredFormations.stream()
                    .map(Utils::mapFormationEntityToFormationDTO)
                    .collect(Collectors.toList());

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Résultats de la recherche");
            response.setFormationList(formationsDTO);
            return response;
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    private Response getErrorResponse(Exception e) {
        Response response = new Response();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Erreur lors du traitement: " + e.getMessage());
        return response;
    }

    private Response getNotFoundResponse(String message) {
        Response response = new Response();
        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setMessage(message);
        return response;
    }
}