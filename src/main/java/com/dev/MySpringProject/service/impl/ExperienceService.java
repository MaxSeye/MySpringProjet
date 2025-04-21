package com.dev.MySpringProject.service.impl;

import com.dev.MySpringProject.dto.ExperienceDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.entity.Candidat;
import com.dev.MySpringProject.entity.Experience;
import com.dev.MySpringProject.repo.ExperienceRepository;
import com.dev.MySpringProject.service.interfac.IExperienceService;
import com.dev.MySpringProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExperienceService implements IExperienceService {

    private final ExperienceRepository experienceRepository;

    @Autowired
    public ExperienceService(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    @Override
    public Response createExperience(ExperienceDTO experienceDTO) {
        try {
            Experience experience = new Experience();
            mapExperienceDTOToEntity(experienceDTO, experience);

            Experience savedExperience = experienceRepository.save(experience);

            Response response = new Response();
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setMessage("Expérience créée avec succès");
            response.setExperience(Utils.mapExperienceEntityToExperienceDTO(savedExperience));

            return response;
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Erreur lors de la création de l'expérience: " + e.getMessage());
        }
    }

    @Override
    public Response getExperienceById(Long id) {
        Optional<Experience> experienceOptional = experienceRepository.findById(id);
        if (experienceOptional.isPresent()) {
            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Expérience trouvée");
            response.setExperience(Utils.mapExperienceEntityToExperienceDTO(experienceOptional.get()));
            return response;
        }
        return createErrorResponse(HttpStatus.NOT_FOUND, "Aucune expérience trouvée avec l'ID: " + id);
    }

    @Override
    public Response updateExperience(Long id, ExperienceDTO experienceDTO) {
        try {
            Optional<Experience> experienceOptional = experienceRepository.findById(id);
            if (experienceOptional.isPresent()) {
                Experience experience = experienceOptional.get();
                mapExperienceDTOToEntity(experienceDTO, experience);

                Experience updatedExperience = experienceRepository.save(experience);

                Response response = new Response();
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Expérience mise à jour avec succès");
                response.setExperience(Utils.mapExperienceEntityToExperienceDTO(updatedExperience));
                return response;
            }
            return createErrorResponse(HttpStatus.NOT_FOUND, "Aucune expérience trouvée avec l'ID: " + id);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Erreur lors de la mise à jour de l'expérience: " + e.getMessage());
        }
    }

    @Override
    public Response deleteExperience(Long id) {
        try {
            Optional<Experience> experienceOptional = experienceRepository.findById(id);
            if (experienceOptional.isPresent()) {
                experienceRepository.deleteById(id);

                Response response = new Response();
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Expérience supprimée avec succès");
                return response;
            }
            return createErrorResponse(HttpStatus.NOT_FOUND, "Aucune expérience trouvée avec l'ID: " + id);
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la suppression de l'expérience: " + e.getMessage());
        }
    }

    @Override
    public Response getExperiencesByCandidat(Long candidatId) {
        try {
            List<Experience> experiences = experienceRepository.findByCandidatId(candidatId);

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Expériences trouvées pour le candidat ID: " + candidatId);
            response.setExperienceList(experiences.stream()
                    .map(Utils::mapExperienceEntityToExperienceDTO)
                    .collect(Collectors.toList()));

            return response;
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la récupération des expériences: " + e.getMessage());
        }
    }

    @Override
    public Response getExperiencesByPoste(String poste) {
        try {
            List<Experience> experiences = experienceRepository.findByPoste(poste);

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Expériences trouvées pour le poste: " + poste);
            response.setExperienceList(experiences.stream()
                    .map(Utils::mapExperienceEntityToExperienceDTO)
                    .collect(Collectors.toList()));

            return response;
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la récupération des expériences: " + e.getMessage());
        }
    }

    @Override
    public Response getExperiencesByEntreprise(String entreprise) {
        try {
            List<Experience> experiences = experienceRepository.findByEntreprise(entreprise);

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Expériences trouvées pour l'entreprise: " + entreprise);
            response.setExperienceList(experiences.stream()
                    .map(Utils::mapExperienceEntityToExperienceDTO)
                    .collect(Collectors.toList()));

            return response;
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la récupération des expériences: " + e.getMessage());
        }
    }

    @Override
    public Response getExperiencesByPosteAndEntreprise(String poste, String entreprise) {
        try {
            List<Experience> experiences = experienceRepository.findByPosteAndEntreprise(poste, entreprise);

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Expériences trouvées pour le poste: " + poste + " et l'entreprise: " + entreprise);
            response.setExperienceList(experiences.stream()
                    .map(Utils::mapExperienceEntityToExperienceDTO)
                    .collect(Collectors.toList()));

            return response;
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la récupération des expériences: " + e.getMessage());
        }
    }

    @Override
    public Response getExperiencesByDateDebut(String dateDebut) {
        try {
            List<Experience> experiences = experienceRepository.findByDateDebut(dateDebut);

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Expériences trouvées pour la date de début: " + dateDebut);
            response.setExperienceList(experiences.stream()
                    .map(Utils::mapExperienceEntityToExperienceDTO)
                    .collect(Collectors.toList()));

            return response;
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la récupération des expériences: " + e.getMessage());
        }
    }

    @Override
    public Response getExperiencesByDateFin(String dateFin) {
        try {
            List<Experience> experiences = experienceRepository.findByDateFin(dateFin);

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Expériences trouvées pour la date de fin: " + dateFin);
            response.setExperienceList(experiences.stream()
                    .map(Utils::mapExperienceEntityToExperienceDTO)
                    .collect(Collectors.toList()));

            return response;
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la récupération des expériences: " + e.getMessage());
        }
    }

    @Override
    public Response searchExperiences(Long candidatId, String poste, String entreprise, String dateDebut, String dateFin) {
        try {
            List<Experience> experiences;

            if (candidatId != null) {
                experiences = experienceRepository.findByCandidatId(candidatId);
            } else {
                experiences = experienceRepository.findAll();
            }

            // Filtrage supplémentaire
            List<Experience> filteredExperiences = experiences.stream()
                    .filter(exp -> poste == null || exp.getPoste().equalsIgnoreCase(poste))
                    .filter(exp -> entreprise == null || exp.getEntreprise().equalsIgnoreCase(entreprise))
                    .filter(exp -> dateDebut == null || exp.getDateDebut().equals(dateDebut))
                    .filter(exp -> dateFin == null || exp.getDateFin().equals(dateFin))
                    .collect(Collectors.toList());

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Résultats de la recherche");
            response.setExperienceList(filteredExperiences.stream()
                    .map(Utils::mapExperienceEntityToExperienceDTO)
                    .collect(Collectors.toList()));

            return response;
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la recherche des expériences: " + e.getMessage());
        }
    }

    @Override
    public Response getCurrentExperiences(Long candidatId) {
        try {
            List<Experience> experiences = experienceRepository.findByCandidatId(candidatId);
            String currentDate = LocalDate.now().toString();

            List<Experience> currentExperiences = experiences.stream()
                    .filter(exp -> exp.getDateFin() == null || exp.getDateFin().compareTo(currentDate) >= 0)
                    .collect(Collectors.toList());

            Response response = new Response();
            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Expériences en cours");
            response.setExperienceList(currentExperiences.stream()
                    .map(Utils::mapExperienceEntityToExperienceDTO)
                    .collect(Collectors.toList()));

            return response;
        } catch (Exception e) {
            return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la récupération des expériences en cours: " + e.getMessage());
        }
    }

    private void mapExperienceDTOToEntity(ExperienceDTO dto, Experience entity) {
        entity.setPoste(dto.getPoste());
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFin());
        entity.setDescription(dto.getDescription());
        entity.setEntreprise(dto.getEntreprise());

        if (dto.getCandidat() != null) {
            Candidat candidat = new Candidat();
            candidat.setId(dto.getCandidat().getId());
            entity.setCandidat(candidat);
        }
    }

    private Response createErrorResponse(HttpStatus status, String message) {
        Response response = new Response();
        response.setStatusCode(status.value());
        response.setMessage(message);
        return response;
    }
}