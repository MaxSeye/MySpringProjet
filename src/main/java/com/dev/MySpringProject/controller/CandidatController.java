package com.dev.MySpringProject.controller;

import com.dev.MySpringProject.dto.CandidatDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.service.interfac.ICandidatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des candidats.
 * Permet aux différents rôles (ADMIN, RECRUTEUR, CANDIDAT) de gérer les profils candidats.
 */
@RestController
@RequestMapping("/api/candidats")
public class CandidatController {

    @Autowired
    private ICandidatService candidatService;

    /**
     * Enregistre un nouveau candidat
     * @param candidatDTO DTO contenant les informations du candidat
     * @return ResponseEntity contenant le candidat créé
     * @apiNote Accessible sans authentification
     */
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody CandidatDTO candidatDTO) {
        Response response = candidatService.registerCandidat(candidatDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Connecte un candidat
     * @param email Email du candidat
     * @param password Mot de passe du candidat
     * @return ResponseEntity contenant le token JWT et les infos du candidat
     * @apiNote Accessible sans authentification
     */
    @PostMapping("/login")
    public ResponseEntity<Response> login(
            @RequestParam String email,
            @RequestParam String password) {
        Response response = candidatService.loginCandidat(email, password);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère tous les candidats
     * @return ResponseEntity contenant la liste des candidats
     * @apiNote Accessible par ADMIN et RECRUTEUR
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> getAllCandidats() {
        Response response = candidatService.getAllCandidats();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère un candidat par son ID
     * @param id ID du candidat
     * @return ResponseEntity contenant le candidat
     * @apiNote Accessible par tous les rôles (avec restrictions pour CANDIDAT)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getCandidatById(@PathVariable Long id) {
        // Vérification pour les candidats (ne peuvent voir que leur propre profil)
        if (hasRole("CANDIDAT") && !id.equals(getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Vous ne pouvez accéder qu'à votre propre profil");
        }

        Response response = candidatService.getCandidatById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Met à jour un candidat
     * @param id ID du candidat à mettre à jour
     * @param candidatDTO DTO contenant les nouvelles informations
     * @return ResponseEntity contenant le candidat mis à jour
     * @apiNote Accessible par ADMIN et CANDIDAT (pour son propre profil)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public ResponseEntity<Response> updateCandidat(
            @PathVariable Long id,
            @RequestBody CandidatDTO candidatDTO) {

        // Vérification pour les candidats (ne peuvent modifier que leur propre profil)
        if (hasRole("CANDIDAT") && !id.equals(getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Vous ne pouvez modifier que votre propre profil");
        }

        Response response = candidatService.updateCandidat(id, candidatDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Supprime un candidat
     * @param id ID du candidat à supprimer
     * @return ResponseEntity confirmant la suppression
     * @apiNote Accessible uniquement par ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteCandidat(@PathVariable Long id) {
        Response response = candidatService.deleteCandidat(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Recherche des candidats par critères
     * @param pays Pays de résidence (optionnel)
     * @param ville Ville de résidence (optionnel)
     * @param domainDetude Domaine d'étude (optionnel)
     * @param niveauDetude Niveau d'étude (optionnel)
     * @param typeDeFormation Type de formation (optionnel)
     * @return ResponseEntity contenant les candidats correspondants
     * @apiNote Accessible par ADMIN et RECRUTEUR
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> searchCandidats(
            @RequestParam(required = false) String pays,
            @RequestParam(required = false) String ville,
            @RequestParam(required = false) String domainDetude,
            @RequestParam(required = false) String niveauDetude,
            @RequestParam(required = false) String typeDeFormation) {

        Response response = candidatService.searchCandidats(pays, ville, domainDetude, niveauDetude, typeDeFormation);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les candidatures en cours d'un candidat
     * @param candidatId ID du candidat
     * @return ResponseEntity contenant les candidatures
     * @apiNote Accessible par tous les rôles (avec restrictions pour CANDIDAT)
     */
    @GetMapping("/{candidatId}/candidatures")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getCandidaturesEnCours(@PathVariable Long candidatId) {
        // Vérification pour les candidats (ne peuvent voir que leurs propres candidatures)
        if (hasRole("CANDIDAT") && !candidatId.equals(getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Vous ne pouvez voir que vos propres candidatures");
        }

        Response response = candidatService.getCandidaturesEnCours(candidatId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les offres favorites d'un candidat
     * @param candidatId ID du candidat
     * @return ResponseEntity contenant les offres favorites
     * @apiNote Accessible par tous les rôles (avec restrictions pour CANDIDAT)
     */
    @GetMapping("/{candidatId}/favoris")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getFavorisOffreEmploi(@PathVariable Long candidatId) {
        // Vérification pour les candidats (ne peuvent voir que leurs propres favoris)
        if (hasRole("CANDIDAT") && !candidatId.equals(getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Vous ne pouvez voir que vos propres favoris");
        }

        Response response = candidatService.getFavorisOffreEmploi(candidatId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Ajoute une offre aux favoris d'un candidat
     * @param candidatId ID du candidat
     * @param offreEmploiId ID de l'offre à ajouter
     * @return ResponseEntity confirmant l'ajout
     * @apiNote Accessible par CANDIDAT (pour son propre profil) et ADMIN
     */
    @PostMapping("/{candidatId}/favoris/{offreEmploiId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public ResponseEntity<Response> addFavorisOffreEmploi(
            @PathVariable Long candidatId,
            @PathVariable Long offreEmploiId) {

        // Vérification pour les candidats (ne peuvent ajouter qu'à leurs propres favoris)
        if (hasRole("CANDIDAT") && !candidatId.equals(getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Vous ne pouvez ajouter qu'à vos propres favoris");
        }

        Response response = candidatService.addFavorisOffreEmploi(candidatId, offreEmploiId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Supprime une offre des favoris d'un candidat
     * @param candidatId ID du candidat
     * @param offreEmploiId ID de l'offre à supprimer
     * @return ResponseEntity confirmant la suppression
     * @apiNote Accessible par CANDIDAT (pour son propre profil) et ADMIN
     */
    @DeleteMapping("/{candidatId}/favoris/{offreEmploiId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public ResponseEntity<Response> removeFavorisOffreEmploi(
            @PathVariable Long candidatId,
            @PathVariable Long offreEmploiId) {

        // Vérification pour les candidats (ne peuvent supprimer que de leurs propres favoris)
        if (hasRole("CANDIDAT") && !candidatId.equals(getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Vous ne pouvez supprimer que de vos propres favoris");
        }

        Response response = candidatService.removeFavorisOffreEmploi(candidatId, offreEmploiId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les expériences d'un candidat
     * @param candidatId ID du candidat
     * @return ResponseEntity contenant les expériences
     * @apiNote Accessible par tous les rôles (avec restrictions pour CANDIDAT)
     */
    @GetMapping("/{candidatId}/experiences")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getExperiences(@PathVariable Long candidatId) {
        // Vérification pour les candidats (ne peuvent voir que leurs propres expériences)
        if (hasRole("CANDIDAT") && !candidatId.equals(getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Vous ne pouvez voir que vos propres expériences");
        }

        Response response = candidatService.getExperiences(candidatId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les formations d'un candidat
     * @param candidatId ID du candidat
     * @return ResponseEntity contenant les formations
     * @apiNote Accessible par tous les rôles (avec restrictions pour CANDIDAT)
     */
    @GetMapping("/{candidatId}/formations")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getFormations(@PathVariable Long candidatId) {
        // Vérification pour les candidats (ne peuvent voir que leurs propres formations)
        if (hasRole("CANDIDAT") && !candidatId.equals(getAuthenticatedCandidatId())) {
            return createForbiddenResponse("Vous ne pouvez voir que vos propres formations");
        }

        Response response = candidatService.getFormations(candidatId);
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
}