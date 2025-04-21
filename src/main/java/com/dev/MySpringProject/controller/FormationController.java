package com.dev.MySpringProject.controller;

import com.dev.MySpringProject.dto.FormationDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.service.interfac.IFormationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour la gestion des formations académiques.
 * Gère les opérations CRUD et les recherches de formations avec sécurisation par rôle.
 */
@RestController
@RequestMapping("/api/formations")
public class FormationController {

    @Autowired
    private IFormationService formationService;

    /**
     * Crée une nouvelle formation.
     * Accessible uniquement par les administrateurs.
     *
     * @param formationDTO DTO contenant les informations de la formation à créer
     * @return ResponseEntity<Response> Réponse contenant la formation créée ou un message d'erreur
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> createFormation(@RequestBody FormationDTO formationDTO) {
        Response response = formationService.createFormation(formationDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Met à jour une formation existante.
     * Accessible uniquement par les administrateurs.
     *
     * @param id ID de la formation à mettre à jour
     * @param formationDTO DTO contenant les nouvelles informations de la formation
     * @return ResponseEntity<Response> Réponse contenant la formation mise à jour ou un message d'erreur
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> updateFormation(
            @PathVariable Long id,
            @RequestBody FormationDTO formationDTO) {
        Response response = formationService.updateFormation(id, formationDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Supprime une formation existante.
     * Accessible uniquement par les administrateurs.
     *
     * @param id ID de la formation à supprimer
     * @return ResponseEntity<Response> Réponse confirmant la suppression ou un message d'erreur
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteFormation(@PathVariable Long id) {
        Response response = formationService.deleteFormation(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les formations du candidat authentifié.
     * Accessible uniquement par les candidats (pour leurs propres formations).
     *
     * @return ResponseEntity<Response> Réponse contenant la liste des formations du candidat
     */
    @GetMapping("/my-formations")
    @PreAuthorize("hasRole('CANDIDAT')")
    public ResponseEntity<Response> getMyFormations() {
        Long candidatId = getAuthenticatedCandidatId();
        Response response = formationService.getFormationsByCandidat(candidatId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les formations d'un candidat spécifique.
     * Accessible uniquement par les recruteurs (pour consulter les profils).
     *
     * @param candidatId ID du candidat dont on veut voir les formations
     * @return ResponseEntity<Response> Réponse contenant la liste des formations du candidat
     */
    @GetMapping("/candidat/{candidatId}")
    @PreAuthorize("hasRole('RECRUTEUR')")
    public ResponseEntity<Response> getFormationsByCandidatForRecruiter(@PathVariable Long candidatId) {
        Response response = formationService.getFormationsByCandidat(candidatId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère une formation spécifique par son ID.
     * Accessible par tous les rôles authentifiés, avec restriction pour les candidats
     * (un candidat ne peut voir que ses propres formations).
     *
     * @param id ID de la formation à récupérer
     * @return ResponseEntity<Response> Réponse contenant la formation demandée ou un message d'erreur
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getFormationById(@PathVariable Long id) {
        Response response = formationService.getFormationById(id);

        if (hasRole("CANDIDAT") && response.getFormation() != null
                && !response.getFormation().getCandidat().getId().equals(getAuthenticatedCandidatId())) {
            Response forbiddenResponse = new Response();
            forbiddenResponse.setStatusCode(HttpStatus.FORBIDDEN.value());
            forbiddenResponse.setMessage("Accès refusé");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(forbiddenResponse);
        }

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Recherche des formations par établissement scolaire.
     * Accessible par les administrateurs et recruteurs.
     *
     * @param ecole Nom de l'établissement scolaire à filtrer
     * @return ResponseEntity<Response> Réponse contenant la liste des formations correspondantes
     */
    @GetMapping("/ecole")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> getFormationsByEcole(@RequestParam String ecole) {
        Response response = formationService.getFormationsByEcole(ecole);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Recherche des formations par diplôme obtenu.
     * Accessible par les administrateurs et recruteurs.
     *
     * @param diplome Intitulé du diplôme à filtrer
     * @return ResponseEntity<Response> Réponse contenant la liste des formations correspondantes
     */
    @GetMapping("/diplome")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> getFormationsByDiplome(@RequestParam String diplome) {
        Response response = formationService.getFormationsByDiplome(diplome);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Recherche des formations par année scolaire.
     * Accessible par les administrateurs et recruteurs.
     *
     * @param anneeScolaire Année scolaire au format "YYYY-YYYY"
     * @return ResponseEntity<Response> Réponse contenant la liste des formations correspondantes
     */
    @GetMapping("/annee-scolaire")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> getFormationsByAnneeScolaire(@RequestParam String anneeScolaire) {
        Response response = formationService.getFormationsByAnneeScolaire(anneeScolaire);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Recherche des formations par combinaison établissement et diplôme.
     * Accessible par les administrateurs et recruteurs.
     *
     * @param ecole Nom de l'établissement scolaire
     * @param diplome Intitulé du diplôme
     * @return ResponseEntity<Response> Réponse contenant la liste des formations correspondantes
     */
    @GetMapping("/ecole-diplome")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> getFormationsByEcoleAndDiplome(
            @RequestParam String ecole,
            @RequestParam String diplome) {
        Response response = formationService.getFormationsByEcoleAndDiplome(ecole, diplome);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Recherche avancée de formations avec plusieurs critères optionnels.
     * Accessible par les administrateurs et recruteurs.
     *
     * @param candidatId ID du candidat (optionnel)
     * @param ecole Nom de l'établissement (optionnel)
     * @param diplome Intitulé du diplôme (optionnel)
     * @param anneeScolaire Année scolaire (optionnel)
     * @return ResponseEntity<Response> Réponse contenant la liste des formations correspondant aux critères
     */
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> searchFormations(
            @RequestParam(required = false) Long candidatId,
            @RequestParam(required = false) String ecole,
            @RequestParam(required = false) String diplome,
            @RequestParam(required = false) String anneeScolaire) {
        Response response = formationService.searchFormations(candidatId, ecole, diplome, anneeScolaire);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // Méthodes utilitaires privées

    /**
     * Récupère l'ID du candidat authentifié à partir du contexte de sécurité.
     *
     * @return Long ID du candidat
     */
    private Long getAuthenticatedCandidatId() {
        // Implémentation à adapter selon votre système d'authentification
        // Exemple: return ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return 1L; // Valeur factice pour l'exemple
    }

    /**
     * Vérifie si l'utilisateur authentifié a un rôle spécifique.
     *
     * @param role Nom du rôle à vérifier (sans le préfixe "ROLE_")
     * @return boolean true si l'utilisateur a le rôle, false sinon
     */
    private boolean hasRole(String role) {
        // Implémentation à adapter selon votre système d'authentification
        // Exemple: return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
        //          .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
        return true; // Valeur factice pour l'exemple
    }
}