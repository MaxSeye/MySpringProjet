package com.dev.MySpringProject.controller;

import com.dev.MySpringProject.dto.FavorisOffreEmploiDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.service.interfac.IFavorisOffreEmploiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des favoris d'offres d'emploi.
 * Permet aux différents rôles (ADMIN, RECRUTEUR, CANDIDAT) de gérer les offres favorites.
 */
@RestController
@RequestMapping("/api/favoris-offres")
public class FavorisOffreEmploiController {

    @Autowired
    private IFavorisOffreEmploiService favorisOffreEmploiService;

    /**
     * Crée un nouveau favori d'offre d'emploi
     * @param favorisDTO DTO contenant les informations du favori à créer
     * @return ResponseEntity contenant le favori créé ou un message d'erreur
     * @apiNote Accessible uniquement par l'ADMIN
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> createFavorisOffreEmploi(@RequestBody FavorisOffreEmploiDTO favorisDTO) {
        Response response = favorisOffreEmploiService.createFavorisOffreEmploi(favorisDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère un favori par son identifiant
     * @param id Identifiant du favori à récupérer
     * @return ResponseEntity contenant le favori ou un message d'erreur
     * @apiNote Accessible par tous les rôles authentifiés (avec restrictions pour CANDIDAT)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getFavorisOffreEmploiById(@PathVariable Long id) {
        Response response = favorisOffreEmploiService.getFavorisOffreEmploiById(id);

        if (hasRole("CANDIDAT") && !isFavorisOwnedByCandidat(id, getAuthenticatedCandidatId())) {
            Response errorResponse = new Response();
            errorResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
            errorResponse.setMessage("Accès non autorisé");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Supprime un favori existant
     * @param id Identifiant du favori à supprimer
     * @return ResponseEntity confirmant la suppression
     * @apiNote Accessible uniquement par l'ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteFavorisOffreEmploi(@PathVariable Long id) {
        Response response = favorisOffreEmploiService.deleteFavorisOffreEmploi(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Associe un candidat à un favori
     * @param favorisId Identifiant du favori
     * @param candidatId Identifiant du candidat à associer
     * @return ResponseEntity confirmant l'association
     * @apiNote Accessible par ADMIN et CANDIDAT (pour son propre compte)
     */
    @PostMapping("/{favorisId}/candidats/{candidatId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public ResponseEntity<Response> addCandidatToFavoris(
            @PathVariable Long favorisId,
            @PathVariable Long candidatId) {

        if (hasRole("CANDIDAT") && !candidatId.equals(getAuthenticatedCandidatId())) {
            Response errorResponse = new Response();
            errorResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
            errorResponse.setMessage("Action non autorisée");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        Response response = favorisOffreEmploiService.addCandidatToFavoris(favorisId, candidatId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Dissocie un candidat d'un favori
     * @param favorisId Identifiant du favori
     * @param candidatId Identifiant du candidat à dissocier
     * @return ResponseEntity confirmant la dissociation
     * @apiNote Accessible par ADMIN et CANDIDAT (pour son propre compte)
     */
    @DeleteMapping("/{favorisId}/candidats/{candidatId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public ResponseEntity<Response> removeCandidatFromFavoris(
            @PathVariable Long favorisId,
            @PathVariable Long candidatId) {

        if (hasRole("CANDIDAT") && !candidatId.equals(getAuthenticatedCandidatId())) {
            Response errorResponse = new Response();
            errorResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
            errorResponse.setMessage("Action non autorisée");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        Response response = favorisOffreEmploiService.removeCandidatFromFavoris(favorisId, candidatId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère la liste des candidats associés à un favori
     * @param favorisId Identifiant du favori
     * @return ResponseEntity contenant la liste des candidats
     * @apiNote Accessible par ADMIN et RECRUTEUR
     */
    @GetMapping("/{favorisId}/candidats")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> getCandidatsByFavoris(@PathVariable Long favorisId) {
        Response response = favorisOffreEmploiService.getCandidatsByFavoris(favorisId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les favoris d'un candidat spécifique
     * @param candidatId Identifiant du candidat
     * @return ResponseEntity contenant la liste des favoris
     * @apiNote Accessible par tous les rôles (avec restrictions pour CANDIDAT)
     */
    @GetMapping("/by-candidat/{candidatId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getFavorisByCandidat(@PathVariable Long candidatId) {

        if (hasRole("CANDIDAT") && !candidatId.equals(getAuthenticatedCandidatId())) {
            Response errorResponse = new Response();
            errorResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
            errorResponse.setMessage("Accès non autorisé");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        Response response = favorisOffreEmploiService.getFavorisByCandidat(candidatId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Vérifie si un favori existe pour un candidat donné
     * @param favorisId Identifiant du favori
     * @param candidatId Identifiant du candidat
     * @return ResponseEntity indiquant l'existence de l'association
     * @apiNote Accessible par tous les rôles (avec restrictions pour CANDIDAT)
     */
    @GetMapping("/exists/{favorisId}/candidat/{candidatId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> checkIfFavorisExistsForCandidat(
            @PathVariable Long favorisId,
            @PathVariable Long candidatId) {

        if (hasRole("CANDIDAT") && !candidatId.equals(getAuthenticatedCandidatId())) {
            Response errorResponse = new Response();
            errorResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
            errorResponse.setMessage("Action non autorisée");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

        Response response = favorisOffreEmploiService.checkIfFavorisExistsForCandidat(favorisId, candidatId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère l'identifiant du candidat authentifié
     * @return Identifiant du candidat
     * @implNote À implémenter selon le système d'authentification
     */
    private Long getAuthenticatedCandidatId() {
        // Implémentation à adapter
        return 1L;
    }

    /**
     * Vérifie si l'utilisateur a un rôle spécifique
     * @param role Rôle à vérifier
     * @return true si l'utilisateur a le rôle
     * @implNote À implémenter selon le système d'authentification
     */
    private boolean hasRole(String role) {
        // Implémentation à adapter
        return true;
    }

    /**
     * Vérifie si un favori appartient à un candidat
     * @param favorisId Identifiant du favori
     * @param candidatId Identifiant du candidat
     * @return true si le favori appartient au candidat
     * @implNote À implémenter selon la logique métier
     */
    private boolean isFavorisOwnedByCandidat(Long favorisId, Long candidatId) {
        // Implémentation à adapter
        return true;
    }
}