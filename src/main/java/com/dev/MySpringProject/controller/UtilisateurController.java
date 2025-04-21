package com.dev.MySpringProject.controller;

import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.entity.Utilisateur;
import com.dev.MySpringProject.service.impl.UtilisateurService;
import com.dev.MySpringProject.service.interfac.IUtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des utilisateurs
 * Tous les endpoints sont préfixés par /api/utilisateurs
 */
@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    // Injection du service utilisateur
    @Autowired
    private IUtilisateurService utilisateurService;
    /**
     * Endpoint pour l'inscription d'un nouvel utilisateur
     * @param utilisateur Les données de l'utilisateur à enregistrer (envoyées en JSON dans le corps de la requête)
     * @return Réponse contenant le statut, un message et éventuellement un token JWT
     */
    @PostMapping("/register")
    public ResponseEntity<Response> registerUtilisateur(@RequestBody Utilisateur utilisateur) {
        Response response = utilisateurService.registerUtilisateur(utilisateur);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Endpoint pour l'authentification d'un utilisateur
     * @param email L'email de l'utilisateur (paramètre de requête)
     * @param password Le mot de passe (paramètre de requête)
     * @return Réponse contenant le statut, un message et un token JWT si succès
     */
    @PostMapping("/authenticate")
    public ResponseEntity<Response> authenticateUtilisateur(@RequestParam String email,
                                                            @RequestParam String password) {
        Response response = utilisateurService.authenticateUtilisateur(email, password);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Endpoint pour récupérer tous les utilisateurs
     * Accès restreint aux ADMIN uniquement
     * @return Liste de tous les utilisateurs avec leurs informations
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getAllUtilisateurs() {
        Response response = utilisateurService.getAllUtilisateurs();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Endpoint pour récupérer un utilisateur par son ID
     * Accès autorisé aux ADMIN, RECRUTEUR et CANDIDAT

     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getUtilisateurById(@PathVariable Long id) {
        Response response = utilisateurService.getUtilisateurById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Endpoint pour récupérer un utilisateur par son email
     * Accès autorisé aux ADMIN, RECRUTEUR et CANDIDAT
     * @param email L'email de l'utilisateur (dans le chemin de l'URL)
     * @return Les informations de l'utilisateur demandé
     */
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> getUtilisateurByEmail(@PathVariable String email) {
        Response response = utilisateurService.getUtilisateurByEmail(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Endpoint pour mettre à jour un utilisateur
     * Accès autorisé aux ADMIN, RECRUTEUR et CANDIDAT
     * @param utilisateur Les nouvelles données de l'utilisateur (envoyées en JSON dans le corps de la requête)
     * @return Réponse avec statut et message de confirmation
     */
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public ResponseEntity<Response> updateUtilisateur(@RequestBody Utilisateur utilisateur) {
        Response response = utilisateurService.updateUtilisateur(utilisateur);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Endpoint pour supprimer un utilisateur
     * Accès restreint aux ADMIN uniquement
     * @param id L'identifiant de l'utilisateur à supprimer (dans le chemin de l'URL)
     * @return Réponse avec statut et message de confirmation
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> deleteUtilisateur(@PathVariable Long id) {
        Response response = utilisateurService.deleteUtilisateur(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Endpoint pour récupérer les utilisateurs par rôle
     * Accès restreint aux ADMIN uniquement
     * @param role Le rôle des utilisateurs à rechercher (dans le chemin de l'URL)
     * @return Liste des utilisateurs ayant le rôle spécifié
     */
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Response> getUtilisateursByRole(@PathVariable String role) {
        Response response = utilisateurService.getUtilisateursByRole(role);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Endpoint pour vérifier si un email existe déjà
     * Accès public (pas de restriction)
     * @param email L'email à vérifier (dans le chemin de l'URL)
     * @return true si l'email existe, false sinon
     */
    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> emailExists(@PathVariable String email) {
        boolean exists = utilisateurService.emailExists(email);
        return ResponseEntity.ok(exists);
    }
}