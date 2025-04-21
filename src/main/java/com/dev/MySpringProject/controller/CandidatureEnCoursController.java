package com.dev.MySpringProject.controller;

import com.dev.MySpringProject.dto.CandidatureEnCoursDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.service.interfac.ICandidatureEnCoursService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des candidatures en cours.
 * Permet aux différents rôles (ADMIN, RECRUTEUR, CANDIDAT) de gérer les candidatures.
 */
@RestController
@RequestMapping("/api/candidatures")
public class CandidatureEnCoursController {

    @Autowired
    private ICandidatureEnCoursService candidatureService;

    /**
     * Crée une nouvelle candidature
     * @param candidatureDTO DTO contenant les informations de la candidature
     * @return ResponseEntity contenant la candidature créée
     * @apiNote Accessible par ADMIN et CANDIDAT (pour son propre profil)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public ResponseEntity<Response> createCandidature(@RequestBody CandidatureEnCoursDTO candidatureDTO) {
        // Vérification pour les candidats (doivent créer pour leur propre profil)
        if (hasRole("CANDIDAT") && !isCandidatureForAuthenticatedCandidat(candidatureDTO)) {
            return createForbiddenResponse("Vous ne pouvez créer que vos propres candidatures");
        }

        Response response = candidatureService.createCandidatureEnCours(candidatureDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère une candidature par son ID
     * @param id ID de la candidature
     * @return ResponseEntity contenant la candidature
     * @apiNote Accessible par tous les rôles (avec restrictions pour CANDIDAT)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getCandidatureById(@PathVariable Long id) {
        Response response = candidatureService.getCandidatureEnCoursById(id);

        // Vérification pour les candidats (ne peuvent voir que leurs propres candidatures)
        if (hasRole("CANDIDAT") && !isCandidatureOwnedByCandidat(id, getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Accès non autorisé à cette candidature");
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Met à jour une candidature existante
     * @param id ID de la candidature à mettre à jour
     * @param candidatureDTO DTO contenant les nouvelles informations
     * @return ResponseEntity contenant la candidature mise à jour
     * @apiNote Accessible par ADMIN et RECRUTEUR
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> updateCandidature(
            @PathVariable Long id,
            @RequestBody CandidatureEnCoursDTO candidatureDTO) {

        Response response = candidatureService.updateCandidatureEnCours(id, candidatureDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Supprime une candidature
     * @param id ID de la candidature à supprimer
     * @return ResponseEntity confirmant la suppression
     * @apiNote Accessible uniquement par ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteCandidature(@PathVariable Long id) {
        Response response = candidatureService.deleteCandidatureEnCours(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les candidatures d'un candidat spécifique
     * @param candidatId ID du candidat
     * @return ResponseEntity contenant la liste des candidatures
     * @apiNote Accessible par tous les rôles (avec restrictions pour CANDIDAT)
     */
    @GetMapping("/candidat/{candidatId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getCandidaturesByCandidat(@PathVariable Long candidatId) {
        // Vérification pour les candidats (ne peuvent voir que leurs propres candidatures)
        if (hasRole("CANDIDAT") && !candidatId.equals(getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Vous ne pouvez voir que vos propres candidatures");
        }

        Response response = candidatureService.getCandidaturesByCandidat(candidatId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Met à jour le statut de discussion d'une candidature
     * @param id ID de la candidature
     * @param newStatut Nouveau statut de discussion
     * @return ResponseEntity contenant la candidature mise à jour
     * @apiNote Accessible par RECRUTEUR et ADMIN
     */
    @PatchMapping("/{id}/statut-discussion")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> updateStatutDiscussion(
            @PathVariable Long id,
            @RequestParam String newStatut) {

        Response response = candidatureService.updateStatutDiscussion(id, newStatut);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Met à jour le statut de sélection d'une candidature
     * @param id ID de la candidature
     * @param newStatut Nouveau statut de sélection
     * @return ResponseEntity contenant la candidature mise à jour
     * @apiNote Accessible par RECRUTEUR et ADMIN
     */
    @PatchMapping("/{id}/statut-selection")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> updateStatutSelection(
            @PathVariable Long id,
            @RequestParam String newStatut) {

        Response response = candidatureService.updateStatutSelection(id, newStatut);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Recherche avancée de candidatures
     * @param candidatId ID du candidat (optionnel)
     * @param statutDiscussion Statut de discussion (optionnel)
     * @param statutSelection Statut de sélection (optionnel)
     * @return ResponseEntity contenant les résultats de la recherche
     * @apiNote Accessible par ADMIN et RECRUTEUR
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> searchCandidatures(
            @RequestParam(required = false) Long candidatId,
            @RequestParam(required = false) String statutDiscussion,
            @RequestParam(required = false) String statutSelection) {

        Response response = candidatureService.searchCandidatures(candidatId, statutDiscussion, statutSelection);
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

    private boolean isCandidatureOwnedByCandidat(Long candidatureId, Long candidatId) {
        // Implémentez la logique de vérification
        // Peut nécessiter un appel au repository
        return true; // Valeur factice
    }

    private boolean isCandidatureForAuthenticatedCandidat(CandidatureEnCoursDTO candidatureDTO) {
        // Vérifie si la candidature est pour le candidat authentifié
        return candidatureDTO.getCandidat() != null &&
                candidatureDTO.getCandidat().getId().equals(getAuthenticatedCandidatId());
    }
}