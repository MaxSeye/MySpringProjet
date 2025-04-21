package com.dev.MySpringProject.service.impl;

import com.dev.MySpringProject.dto.CandidatDTO;
import com.dev.MySpringProject.dto.CandidatureEnCoursDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.entity.Candidat;
import com.dev.MySpringProject.entity.CandidatureEnCours;
import com.dev.MySpringProject.repo.CandidatRepository;
import com.dev.MySpringProject.repo.CandidatureEnCoursRepository;
import com.dev.MySpringProject.service.interfac.ICandidatureEnCoursService;
import com.dev.MySpringProject.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CandidatureEnCoursService implements ICandidatureEnCoursService {

    @Autowired
    private  CandidatureEnCoursRepository candidatureRepository;
    @Autowired
    private  CandidatRepository candidatRepository;

    @Override
    @Transactional
    public Response createCandidatureEnCours(CandidatureEnCoursDTO candidatureDTO) {
        Response response = new Response();

        // Vérification du candidat associé
        if (candidatureDTO.getCandidat() == null || candidatureDTO.getCandidat().getId() == null) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Candidate ID is required");
            return response;
        }

        Optional<Candidat> candidatOptional = candidatRepository.findById(candidatureDTO.getCandidat().getId());
        if (candidatOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Candidate not found");
            return response;
        }

        // Validation des statuts
        if (!isValidStatut(candidatureDTO.getStatutDiscussion()) || !isValidStatut(candidatureDTO.getStatutSelection())) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid status value");
            return response;
        }

        CandidatureEnCours candidature = new CandidatureEnCours();
        candidature.setEnCours(candidatureDTO.getEnCours());
        candidature.setStatutDiscussion(candidatureDTO.getStatutDiscussion());
        candidature.setStatutSelection(candidatureDTO.getStatutSelection());
        candidature.setCandidat(candidatOptional.get());

        CandidatureEnCours savedCandidature = candidatureRepository.save(candidature);
        CandidatureEnCoursDTO savedCandidatureDTO = Utils.mapCandidatureEnCoursEntityToCandidatureEnCoursDTO(savedCandidature);

        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Application created successfully");
        response.setCandidatureEnCours(savedCandidatureDTO);

        return response;
    }

    private boolean isValidStatut(String statut) {
        // Implémentez votre logique de validation des statuts
        return statut != null && !statut.trim().isEmpty();
    }

    @Override
    public Response getCandidatureEnCoursById(Long id) {
        Response response = new Response();
        Optional<CandidatureEnCours> candidatureOptional = candidatureRepository.findById(id);

        if (candidatureOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Application not found");
            return response;
        }

        CandidatureEnCoursDTO candidatureDTO = Utils.mapCandidatureEnCoursEntityToCandidatureEnCoursDTO(candidatureOptional.get());
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Application retrieved successfully");
        response.setCandidatureEnCours(candidatureDTO);

        return response;
    }

    @Override
    @Transactional
    public Response updateCandidatureEnCours(Long id, CandidatureEnCoursDTO candidatureDTO) {
        Response response = new Response();
        Optional<CandidatureEnCours> candidatureOptional = candidatureRepository.findById(id);

        if (candidatureOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Application not found");
            return response;
        }

        // Validation des statuts
        if (candidatureDTO.getStatutDiscussion() != null && !isValidStatut(candidatureDTO.getStatutDiscussion())) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid discussion status");
            return response;
        }

        if (candidatureDTO.getStatutSelection() != null && !isValidStatut(candidatureDTO.getStatutSelection())) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid selection status");
            return response;
        }

        CandidatureEnCours existingCandidature = candidatureOptional.get();

        // Mise à jour des champs
        if (candidatureDTO.getEnCours() != null) {
            existingCandidature.setEnCours(candidatureDTO.getEnCours());
        }
        if (candidatureDTO.getStatutDiscussion() != null) {
            existingCandidature.setStatutDiscussion(candidatureDTO.getStatutDiscussion());
        }
        if (candidatureDTO.getStatutSelection() != null) {
            existingCandidature.setStatutSelection(candidatureDTO.getStatutSelection());
        }

        // Mise à jour du candidat si nécessaire
        if (candidatureDTO.getCandidat() != null && candidatureDTO.getCandidat().getId() != null
                && !existingCandidature.getCandidat().getId().equals(candidatureDTO.getCandidat().getId())) {
            Optional<Candidat> newCandidat = candidatRepository.findById(candidatureDTO.getCandidat().getId());
            if (newCandidat.isPresent()) {
                existingCandidature.setCandidat(newCandidat.get());
            }
        }

        CandidatureEnCours updatedCandidature = candidatureRepository.save(existingCandidature);
        CandidatureEnCoursDTO updatedCandidatureDTO = Utils.mapCandidatureEnCoursEntityToCandidatureEnCoursDTO(updatedCandidature);

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Application updated successfully");
        response.setCandidatureEnCours(updatedCandidatureDTO);

        return response;
    }

    @Override
    @Transactional
    public Response deleteCandidatureEnCours(Long id) {
        Response response = new Response();

        if (!candidatureRepository.existsById(id)) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Application not found");
            return response;
        }

        candidatureRepository.deleteById(id);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Application deleted successfully");

        return response;
    }

    @Override
    public Response getCandidaturesByCandidat(Long candidatId) {
        Response response = new Response();

        if (!candidatRepository.existsById(candidatId)) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Candidate not found");
            return response;
        }

        List<CandidatureEnCours> candidatures = candidatureRepository.findByCandidatId(candidatId);

        if (candidatures.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No applications found for this candidate");
            return response;
        }

        List<CandidatureEnCoursDTO> candidatureDTOs = candidatures.stream()
                .map(Utils::mapCandidatureEnCoursEntityToCandidatureEnCoursDTO)
                .collect(Collectors.toList());

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Applications retrieved successfully");
        response.setCandidatureEnCoursList(candidatureDTOs);

        return response;
    }

    @Override
    public Response getCandidaturesByStatutDiscussion(String statutDiscussion) {
        Response response = new Response();

        if (!isValidStatut(statutDiscussion)) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid status value");
            return response;
        }

        List<CandidatureEnCours> candidatures = candidatureRepository.findByStatutDiscussion(statutDiscussion);

        if (candidatures.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No applications found with this discussion status");
            return response;
        }

        List<CandidatureEnCoursDTO> candidatureDTOs = candidatures.stream()
                .map(Utils::mapCandidatureEnCoursEntityToCandidatureEnCoursDTO)
                .collect(Collectors.toList());

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Applications retrieved successfully");
        response.setCandidatureEnCoursList(candidatureDTOs);

        return response;
    }

    @Override
    public Response getCandidaturesByStatutSelection(String statutSelection) {
        Response response = new Response();

        if (!isValidStatut(statutSelection)) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid status value");
            return response;
        }

        List<CandidatureEnCours> candidatures = candidatureRepository.findByStatutSelection(statutSelection);

        if (candidatures.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No applications found with this selection status");
            return response;
        }

        List<CandidatureEnCoursDTO> candidatureDTOs = candidatures.stream()
                .map(Utils::mapCandidatureEnCoursEntityToCandidatureEnCoursDTO)
                .collect(Collectors.toList());

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Applications retrieved successfully");
        response.setCandidatureEnCoursList(candidatureDTOs);

        return response;
    }

    @Override
    public Response getCandidaturesByStatuts(String statutDiscussion, String statutSelection) {
        Response response = new Response();

        if (!isValidStatut(statutDiscussion) || !isValidStatut(statutSelection)) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid status values");
            return response;
        }

        List<CandidatureEnCours> candidatures = candidatureRepository.findByStatutDiscussionAndStatutSelection(statutDiscussion, statutSelection);

        if (candidatures.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No applications found with these statuses");
            return response;
        }

        List<CandidatureEnCoursDTO> candidatureDTOs = candidatures.stream()
                .map(Utils::mapCandidatureEnCoursEntityToCandidatureEnCoursDTO)
                .collect(Collectors.toList());

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Applications retrieved successfully");
        response.setCandidatureEnCoursList(candidatureDTOs);

        return response;
    }

    @Override
    @Transactional
    public Response updateStatutDiscussion(Long id, String newStatut) {
        Response response = new Response();

        if (!isValidStatut(newStatut)) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid status value");
            return response;
        }

        Optional<CandidatureEnCours> candidatureOptional = candidatureRepository.findById(id);
        if (candidatureOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Application not found");
            return response;
        }

        CandidatureEnCours candidature = candidatureOptional.get();
        candidature.setStatutDiscussion(newStatut);
        candidatureRepository.save(candidature);

        CandidatureEnCoursDTO candidatureDTO = Utils.mapCandidatureEnCoursEntityToCandidatureEnCoursDTO(candidature);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Discussion status updated successfully");
        response.setCandidatureEnCours(candidatureDTO);

        return response;
    }

    @Override
    @Transactional
    public Response updateStatutSelection(Long id, String newStatut) {
        Response response = new Response();

        if (!isValidStatut(newStatut)) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid status value");
            return response;
        }

        Optional<CandidatureEnCours> candidatureOptional = candidatureRepository.findById(id);
        if (candidatureOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Application not found");
            return response;
        }

        CandidatureEnCours candidature = candidatureOptional.get();
        candidature.setStatutSelection(newStatut);
        candidatureRepository.save(candidature);

        CandidatureEnCoursDTO candidatureDTO = Utils.mapCandidatureEnCoursEntityToCandidatureEnCoursDTO(candidature);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Selection status updated successfully");
        response.setCandidatureEnCours(candidatureDTO);

        return response;
    }

    @Override
    public Response searchCandidatures(Long candidatId, String statutDiscussion, String statutSelection) {
        Response response = new Response();

        // Validation des paramètres
        if (candidatId != null && !candidatRepository.existsById(candidatId)) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Candidate not found");
            return response;
        }

        if (statutDiscussion != null && !isValidStatut(statutDiscussion)) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid discussion status");
            return response;
        }

        if (statutSelection != null && !isValidStatut(statutSelection)) {
            response.setStatusCode(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Invalid selection status");
            return response;
        }

        List<CandidatureEnCours> candidatures;

        if (candidatId != null && statutDiscussion != null && statutSelection != null) {
            candidatures = candidatureRepository.findByCandidatIdAndStatutDiscussionAndStatutSelection(
                    candidatId, statutDiscussion, statutSelection);
        } else if (candidatId != null && statutDiscussion != null) {
            candidatures = candidatureRepository.findByCandidatIdAndStatutDiscussion(candidatId, statutDiscussion);
        } else if (candidatId != null && statutSelection != null) {
            candidatures = candidatureRepository.findByCandidatIdAndStatutSelection(candidatId, statutSelection);
        } else if (candidatId != null) {
            candidatures = candidatureRepository.findByCandidatId(candidatId);
        } else if (statutDiscussion != null && statutSelection != null) {
            candidatures = candidatureRepository.findByStatutDiscussionAndStatutSelection(statutDiscussion, statutSelection);
        } else if (statutDiscussion != null) {
            candidatures = candidatureRepository.findByStatutDiscussion(statutDiscussion);
        } else if (statutSelection != null) {
            candidatures = candidatureRepository.findByStatutSelection(statutSelection);
        } else {
            candidatures = candidatureRepository.findAll();
        }

        if (candidatures.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No applications found with the specified criteria");
            return response;
        }

        List<CandidatureEnCoursDTO> candidatureDTOs = candidatures.stream()
                .map(Utils::mapCandidatureEnCoursEntityToCandidatureEnCoursDTO)
                .collect(Collectors.toList());

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Applications retrieved successfully");
        response.setCandidatureEnCoursList(candidatureDTOs);

        return response;
    }
}