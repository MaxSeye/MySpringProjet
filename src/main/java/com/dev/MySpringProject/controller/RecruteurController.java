package com.dev.MySpringProject.controller;

import com.dev.MySpringProject.dto.RecruteurDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.service.interfac.IRecruteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruteurs")
public class RecruteurController {

    @Autowired
    private IRecruteurService recruteurService;

    /**
     * Enregistrement d'un nouveau recruteur
     */
    @PostMapping("/register")
    public ResponseEntity<Response> registerRecruteur(@RequestBody RecruteurDTO recruteurDTO) {
        Response response = recruteurService.registerRecruteur(recruteurDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Authentification d'un recruteur
     */
    @PostMapping("/login")
    public ResponseEntity<Response> loginRecruteur(
            @RequestParam String email,
            @RequestParam String password) {
        Response response = recruteurService.loginRecruteur(email, password);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupérer un recruteur par son ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> getRecruteurById(@PathVariable Long id) {
        Response response = recruteurService.getRecruteurById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Mettre à jour un recruteur
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> updateRecruteur(
            @PathVariable Long id,
            @RequestBody RecruteurDTO recruteurDTO) {
        Response response = recruteurService.updateRecruteur(id, recruteurDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Supprimer un recruteur (réservé aux admins)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteRecruteur(@PathVariable Long id) {
        Response response = recruteurService.deleteRecruteur(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupérer les recruteurs par société
     */
    @GetMapping("/by-societe")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public ResponseEntity<Response> getRecruteursBySociete(
            @RequestParam String nomSociete) {
        Response response = recruteurService.getRecruteursBySociete(nomSociete);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupérer les recruteurs par pays
     */
    @GetMapping("/by-pays")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public ResponseEntity<Response> getRecruteursByPays(
            @RequestParam String pays) {
        Response response = recruteurService.getRecruteursByPays(pays);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupérer les recruteurs par ville
     */
    @GetMapping("/by-ville")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public ResponseEntity<Response> getRecruteursByVille(
            @RequestParam String ville) {
        Response response = recruteurService.getRecruteursByVille(ville);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupérer les recruteurs par domaine d'activité
     */
    @GetMapping("/by-domaine")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public ResponseEntity<Response> getRecruteursByDomaineActivite(
            @RequestParam String domaine) {
        Response response = recruteurService.getRecruteursByDomaineActivite(domaine);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Vérifier si un nom de société existe
     */
    @GetMapping("/check-societe")
    public ResponseEntity<Response> checkSocieteExists(
            @RequestParam String nomSociete) {
        Response response = recruteurService.checkSocieteExists(nomSociete);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupérer les offres d'un recruteur
     */
    @GetMapping("/{id}/offres")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getOffresByRecruteur(
            @PathVariable Long id) {
        Response response = recruteurService.getOffresByRecruteur(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupérer les notifications d'un recruteur
     */
    @GetMapping("/{id}/notifications")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> getNotificationsByRecruteur(
            @PathVariable Long id) {
        Response response = recruteurService.getNotificationsByRecruteur(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupérer les candidats d'un recruteur
     */
    @GetMapping("/{id}/candidats")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> getCandidatsByRecruteur(
            @PathVariable Long id) {
        Response response = recruteurService.getCandidatsByRecruteur(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupérer les statistiques d'un recruteur
     */
    @GetMapping("/{id}/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public ResponseEntity<Response> getRecruteurStats(
            @PathVariable Long id) {
        Response response = recruteurService.getRecruteurStats(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}