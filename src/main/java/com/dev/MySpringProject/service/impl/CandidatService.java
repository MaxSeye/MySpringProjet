package com.dev.MySpringProject.service.impl;

import com.dev.MySpringProject.dto.*;
import com.dev.MySpringProject.entity.*;
import com.dev.MySpringProject.repo.CandidatRepository;
import com.dev.MySpringProject.service.interfac.ICandidatService;
import com.dev.MySpringProject.utils.JWTUtils;
import com.dev.MySpringProject.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CandidatService implements ICandidatService {

    @Autowired
    private  CandidatRepository candidatRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  AuthenticationManager authenticationManager;
    @Autowired
    private  JWTUtils jwtUtils;

    @Override
    @Transactional
    public Response registerCandidat(CandidatDTO candidatDTO) {
        Response response = new Response();

        if (candidatRepository.existsByEmail(candidatDTO.getEmail())) {
            response.setStatusCode(HttpStatus.CONFLICT.value());
            response.setMessage("Email already exists");
            return response;
        }

        Candidat candidat = new Candidat();
        // Attributs de Utilisateur
        candidat.setEmail(candidatDTO.getEmail());
        candidat.setPassword(passwordEncoder.encode(candidatDTO.getPassword()));
        candidat.setPrenom(candidatDTO.getPrenom());
        candidat.setNom(candidatDTO.getNom());
        candidat.setNumeroDeTelephone(candidatDTO.getNumeroDeTelephone());
        candidat.setRole("CANDIDAT");

        // Attributs spécifiques à Candidat
        candidat.setCivilite(candidatDTO.getCivilite());
        candidat.setDateDeNaissance(candidatDTO.getDateDeNaissance());
        candidat.setPays(candidatDTO.getPays());
        candidat.setVille(candidatDTO.getVille());
        candidat.setDomaineDetude(candidatDTO.getDomainDetude());
        candidat.setTypeDeFormation(candidatDTO.getTypeDeFormation());
        candidat.setNiveauDetude(candidatDTO.getNiveauDetude());

        Candidat savedCandidat = candidatRepository.save(candidat);
        CandidatDTO savedCandidatDTO = Utils.mapCandidatEntityToCandidatDTO(savedCandidat);

        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Candidate registered successfully");
        response.setCandidat(savedCandidatDTO);

        return response;
    }

    @Override
    public Response loginCandidat(String email, String password) {
        Response response = new Response();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtils.generateToken(userDetails);

            Optional<Candidat> candidatOptional = candidatRepository.findByEmail(email);
            if (candidatOptional.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Candidate not found");
                return response;
            }

            CandidatDTO candidatDTO = Utils.mapCandidatEntityToCandidatDTO(candidatOptional.get());

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Login successful");
            response.setToken(token);
            response.setRole(candidatOptional.get().getRole());
            response.setCandidat(candidatDTO);

        } catch (Exception e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("Invalid email or password");
        }

        return response;
    }

    @Override
    public Response getAllCandidats() {
        Response response = new Response();
        List<Candidat> candidats = candidatRepository.findAll();

        if (candidats.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No candidates found");
            return response;
        }

        List<CandidatDTO> candidatDTOs = candidats.stream()
                .map(Utils::mapCandidatEntityToCandidatDTO)
                .collect(Collectors.toList());

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Candidates retrieved successfully");
        response.setCandidatList(candidatDTOs);

        return response;
    }

    @Override
    public Response getCandidatById(Long id) {
        Response response = new Response();
        Optional<Candidat> candidatOptional = candidatRepository.findById(id);

        if (candidatOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Candidate not found");
            return response;
        }

        CandidatDTO candidatDTO = Utils.mapCandidatEntityToCandidatDTO(candidatOptional.get());
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Candidate retrieved successfully");
        response.setCandidat(candidatDTO);

        return response;
    }

    @Override
    @Transactional
    public Response updateCandidat(Long id, CandidatDTO candidatDTO) {
        Response response = new Response();
        Optional<Candidat> candidatOptional = candidatRepository.findById(id);

        if (candidatOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Candidate not found");
            return response;
        }

        Candidat existingCandidat = candidatOptional.get();
        // Mise à jour des attributs de Utilisateur
        existingCandidat.setEmail(candidatDTO.getEmail());
        existingCandidat.setPrenom(candidatDTO.getPrenom());
        existingCandidat.setNom(candidatDTO.getNom());
        existingCandidat.setNumeroDeTelephone(candidatDTO.getNumeroDeTelephone());

        // Mise à jour des attributs spécifiques à Candidat
        existingCandidat.setCivilite(candidatDTO.getCivilite());
        existingCandidat.setDateDeNaissance(candidatDTO.getDateDeNaissance());
        existingCandidat.setPays(candidatDTO.getPays());
        existingCandidat.setVille(candidatDTO.getVille());
        existingCandidat.setDomaineDetude(candidatDTO.getDomainDetude());
        existingCandidat.setTypeDeFormation(candidatDTO.getTypeDeFormation());
        existingCandidat.setNiveauDetude(candidatDTO.getNiveauDetude());

        if (candidatDTO.getPassword() != null && !candidatDTO.getPassword().isEmpty()) {
            existingCandidat.setPassword(passwordEncoder.encode(candidatDTO.getPassword()));
        }

        Candidat updatedCandidat = candidatRepository.save(existingCandidat);
        CandidatDTO updatedCandidatDTO = Utils.mapCandidatEntityToCandidatDTO(updatedCandidat);

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Candidate updated successfully");
        response.setCandidat(updatedCandidatDTO);

        return response;
    }

    @Override
    @Transactional
    public Response deleteCandidat(Long id) {
        Response response = new Response();

        if (!candidatRepository.existsById(id)) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Candidate not found");
            return response;
        }

        candidatRepository.deleteById(id);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Candidate deleted successfully");

        return response;
    }

    @Override
    public Response searchCandidats(String pays, String ville, String domainDetude, String niveauDetude, String typeDeFormation) {
        Response response = new Response();

        // Implémentation basique de la recherche - à adapter selon vos besoins
        List<Candidat> candidats = candidatRepository.findAll();

        List<Candidat> filteredCandidats = candidats.stream()
                .filter(c -> pays == null || c.getPays().equalsIgnoreCase(pays))
                .filter(c -> ville == null || c.getVille().equalsIgnoreCase(ville))
                .filter(c -> domainDetude == null || c.getDomaineDetude().equalsIgnoreCase(domainDetude))
                .filter(c -> niveauDetude == null || c.getNiveauDetude().equalsIgnoreCase(niveauDetude))
                .filter(c -> typeDeFormation == null || c.getTypeDeFormation().equalsIgnoreCase(typeDeFormation))
                .collect(Collectors.toList());

        if (filteredCandidats.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No candidates found with the specified criteria");
            return response;
        }

        List<CandidatDTO> candidatDTOs = filteredCandidats.stream()
                .map(Utils::mapCandidatEntityToCandidatDTO)
                .collect(Collectors.toList());

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Candidates retrieved successfully");
        response.setCandidatList(candidatDTOs);

        return response;
    }

    @Override
    public Response getCandidaturesEnCours(Long candidatId) {
        Response response = new Response();
        Optional<Candidat> candidatOptional = candidatRepository.findById(candidatId);

        if (candidatOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Candidate not found");
            return response;
        }

        CandidatDTO candidatDTO = Utils.mapCandidatEntityToCandidatDTO(candidatOptional.get());
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Ongoing applications retrieved successfully");
        response.setCandidat(candidatDTO); // Les candidatures sont incluses dans le DTO

        return response;
    }

    @Override
    public Response getFavorisOffreEmploi(Long candidatId) {
        Response response = new Response();
        Optional<Candidat> candidatOptional = candidatRepository.findById(candidatId);

        if (candidatOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Candidate not found");
            return response;
        }

        CandidatDTO candidatDTO = Utils.mapCandidatEntityToCandidatDTO(candidatOptional.get());
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Favorite job offers retrieved successfully");
        response.setCandidat(candidatDTO); // Les favoris sont inclus dans le DTO

        return response;
    }

    @Override
    public Response getNotifications(Long candidatId) {
        Response response = new Response();
        Optional<Candidat> candidatOptional = candidatRepository.findById(candidatId);

        if (candidatOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Candidate not found");
            return response;
        }

        CandidatDTO candidatDTO = Utils.mapCandidatEntityToCandidatDTO(candidatOptional.get());
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Notifications retrieved successfully");
        response.setCandidat(candidatDTO); // Les notifications sont incluses dans le DTO

        return response;
    }

    @Override
    public Response getExperiences(Long candidatId) {
        Response response = new Response();
        Optional<Candidat> candidatOptional = candidatRepository.findById(candidatId);

        if (candidatOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Candidate not found");
            return response;
        }

        CandidatDTO candidatDTO = Utils.mapCandidatEntityToCandidatDTO(candidatOptional.get());
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Experiences retrieved successfully");
        response.setCandidat(candidatDTO); // Les expériences sont incluses dans le DTO

        return response;
    }

    @Override
    public Response getFormations(Long candidatId) {
        Response response = new Response();
        Optional<Candidat> candidatOptional = candidatRepository.findById(candidatId);

        if (candidatOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Candidate not found");
            return response;
        }

        CandidatDTO candidatDTO = Utils.mapCandidatEntityToCandidatDTO(candidatOptional.get());
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Formations retrieved successfully");
        response.setCandidat(candidatDTO); // Les formations sont incluses dans le DTO

        return response;
    }

    @Override
    @Transactional
    public Response addFavorisOffreEmploi(Long candidatId, Long offreEmploiId) {
        Response response = new Response();
        // Implémentation à compléter selon votre logique métier
        response.setStatusCode(HttpStatus.NOT_IMPLEMENTED.value());
        response.setMessage("Feature not implemented yet");
        return response;
    }

    @Override
    @Transactional
    public Response removeFavorisOffreEmploi(Long candidatId, Long offreEmploiId) {
        Response response = new Response();
        // Implémentation à compléter selon votre logique métier
        response.setStatusCode(HttpStatus.NOT_IMPLEMENTED.value());
        response.setMessage("Feature not implemented yet");
        return response;
    }

    @Override
    @Transactional
    public Response postulerOffreEmploi(Long candidatId, Long offreEmploiId, CandidatDTO candidatureInfo) {
        Response response = new Response();
        // Implémentation à compléter selon votre logique métier
        response.setStatusCode(HttpStatus.NOT_IMPLEMENTED.value());
        response.setMessage("Feature not implemented yet");
        return response;
    }

    @Override
    @Transactional
    public Response markNotificationAsRead(Long candidatId, Long notificationId) {
        Response response = new Response();
        // Implémentation à compléter selon votre logique métier
        response.setStatusCode(HttpStatus.NOT_IMPLEMENTED.value());
        response.setMessage("Feature not implemented yet");
        return response;
    }
}