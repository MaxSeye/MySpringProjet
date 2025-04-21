package com.dev.MySpringProject.controller;

import com.dev.MySpringProject.dto.ExperienceDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.service.interfac.IExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des expériences professionnelles.
 * Permet aux différents rôles (ADMIN, RECRUTEUR, CANDIDAT) de gérer les expériences.
 */
@RestController
@RequestMapping("/api/experiences")
public class ExperienceController {

    @Autowired
    private IExperienceService experienceService;

    /**
     * Crée une nouvelle expérience professionnelle
     * @param experienceDTO DTO contenant les informations de l'expérience
     * @return ResponseEntity contenant l'expérience créée
     * @apiNote Accessible par ADMIN et CANDIDAT (pour son propre profil)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public ResponseEntity<Response> createExperience(@RequestBody ExperienceDTO experienceDTO) {
        // Vérification pour les candidats (doivent créer pour leur propre profil)
        if (hasRole("CANDIDAT") && !isExperienceForAuthenticatedCandidat(experienceDTO)) {
            return createForbiddenResponse("Vous ne pouvez créer que vos propres expériences");
        }

        Response response = experienceService.createExperience(experienceDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère une expérience par son ID
     * @param id ID de l'expérience
     * @return ResponseEntity contenant l'expérience
     * @apiNote Accessible par tous les rôles (avec restrictions pour CANDIDAT)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getExperienceById(@PathVariable Long id) {
        Response response = experienceService.getExperienceById(id);

        // Vérification pour les candidats (ne peuvent voir que leurs propres expériences)
        if (hasRole("CANDIDAT") && !isExperienceOwnedByCandidat(id, getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Accès non autorisé à cette expérience");
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Met à jour une expérience existante
     * @param id ID de l'expérience à mettre à jour
     * @param experienceDTO DTO contenant les nouvelles informations
     * @return ResponseEntity contenant l'expérience mise à jour
     * @apiNote Accessible par ADMIN et CANDIDAT (pour son propre profil)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public ResponseEntity<Response> updateExperience(
            @PathVariable Long id,
            @RequestBody ExperienceDTO experienceDTO) {

        // Vérification pour les candidats (doivent mettre à jour leurs propres expériences)
        if (hasRole("CANDIDAT")) {
            if (!isExperienceOwnedByCandidat(id, getAuthenticatedCandidatId())) {
                return createForbiddenResponse("Vous ne pouvez modifier que vos propres expériences");
            }
            if (!isExperienceForAuthenticatedCandidat(experienceDTO)) {
                return createForbiddenResponse("Vous ne pouvez associer qu'à votre propre profil");
            }
        }

        Response response = experienceService.updateExperience(id, experienceDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Supprime une expérience
     * @param id ID de l'expérience à supprimer
     * @return ResponseEntity confirmant la suppression
     * @apiNote Accessible par ADMIN et CANDIDAT (pour son propre profil)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public ResponseEntity<Response> deleteExperience(@PathVariable Long id) {
        // Vérification pour les candidats (doivent supprimer leurs propres expériences)
        if (hasRole("CANDIDAT") && !isExperienceOwnedByCandidat(id, getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Vous ne pouvez supprimer que vos propres expériences");
        }

        Response response = experienceService.deleteExperience(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les expériences d'un candidat spécifique
     * @param candidatId ID du candidat
     * @return ResponseEntity contenant la liste des expériences
     * @apiNote Accessible par tous les rôles (avec restrictions pour CANDIDAT)
     */
    @GetMapping("/candidat/{candidatId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getExperiencesByCandidat(@PathVariable Long candidatId) {
        // Vérification pour les candidats (ne peuvent voir que leurs propres expériences)
        if (hasRole("CANDIDAT") && !candidatId.equals(getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Vous ne pouvez voir que vos propres expériences");
        }

        Response response = experienceService.getExperiencesByCandidat(candidatId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les expériences par poste
     * @param poste Poste recherché
     * @return ResponseEntity contenant la liste des expériences
     * @apiNote Accessible par ADMIN et RECRUTEUR
     */
    @GetMapping("/poste/{poste}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> getExperiencesByPoste(@PathVariable String poste) {
        Response response = experienceService.getExperiencesByPoste(poste);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les expériences par entreprise
     * @param entreprise Entreprise recherchée
     * @return ResponseEntity contenant la liste des expériences
     * @apiNote Accessible par ADMIN et RECRUTEUR
     */
    @GetMapping("/entreprise/{entreprise}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> getExperiencesByEntreprise(@PathVariable String entreprise) {
        Response response = experienceService.getExperiencesByEntreprise(entreprise);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les expériences par poste et entreprise
     * @param poste Poste recherché
     * @param entreprise Entreprise recherchée
     * @return ResponseEntity contenant la liste des expériences
     * @apiNote Accessible par ADMIN et RECRUTEUR
     */
    @GetMapping("/poste/{poste}/entreprise/{entreprise}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> getExperiencesByPosteAndEntreprise(
            @PathVariable String poste,
            @PathVariable String entreprise) {
        Response response = experienceService.getExperiencesByPosteAndEntreprise(poste, entreprise);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Recherche avancée d'expériences avec filtres optionnels
     * @param candidatId ID du candidat (optionnel)
     * @param poste Poste recherché (optionnel)
     * @param entreprise Entreprise recherchée (optionnel)
     * @param dateDebut Date de début (optionnel)
     * @param dateFin Date de fin (optionnel)
     * @return ResponseEntity contenant les résultats de la recherche
     * @apiNote Accessible par ADMIN et RECRUTEUR
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> searchExperiences(
            @RequestParam(required = false) Long candidatId,
            @RequestParam(required = false) String poste,
            @RequestParam(required = false) String entreprise,
            @RequestParam(required = false) String dateDebut,
            @RequestParam(required = false) String dateFin) {

        Response response = experienceService.searchExperiences(candidatId, poste, entreprise, dateDebut, dateFin);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les expériences en cours d'un candidat
     * @param candidatId ID du candidat
     * @return ResponseEntity contenant la liste des expériences en cours
     * @apiNote Accessible par tous les rôles (avec restrictions pour CANDIDAT)
     */
    @GetMapping("/candidat/{candidatId}/current")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getCurrentExperiences(@PathVariable Long candidatId) {
        // Vérification pour les candidats (ne peuvent voir que leurs propres expériences)
        if (hasRole("CANDIDAT") && !candidatId.equals(getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Vous ne pouvez voir que vos propres expériences");
        }

        Response response = experienceService.getCurrentExperiences(candidatId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Méthodes utilitaires privées
    private ResponseEntity<Response> createForbiddenResponse(String message) {
        Response response = new Response();
        response.setStatusCode(HttpStatus.FORBIDDEN.value());
        response.setMessage(message);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    private Long getAuthenticatedCandidatId() {
        // Implémentez selon votre système d'authentification
        // Ex: return ((UserDetailsImpl) SecurityContextHolder...).getId();
        return 1L; // Valeur factice
    }

    private boolean hasRole(String role) {
        // Implémentez selon votre système d'authentification
        // Ex: return SecurityContextHolder...getAuthorities().stream()...
        return true; // Valeur factice
    }

    private boolean isExperienceOwnedByCandidat(Long experienceId, Long candidatId) {
        // Implémentez la logique de vérification
        // Peut nécessiter un appel au repository
        return true; // Valeur factice
    }

    private boolean isExperienceForAuthenticatedCandidat(ExperienceDTO experienceDTO) {
        // Vérifie si l'expérience est pour le candidat authentifié
        return experienceDTO.getCandidat() != null &&
                experienceDTO.getCandidat().getId().equals(getAuthenticatedCandidatId());
    }
}