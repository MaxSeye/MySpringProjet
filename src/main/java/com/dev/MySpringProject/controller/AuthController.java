package com.dev.MySpringProject.controller;

import com.dev.MySpringProject.dto.LoginRequest;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.entity.Utilisateur;
import com.dev.MySpringProject.service.interfac.IUtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur pour gérer les opérations d'authentification (inscription et connexion).
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUtilisateurService utilisateurService;

    /**
     * Endpoint pour l'inscription d'un nouvel utilisateur
     * @param utilisateur Les données de l'utilisateur à enregistrer
     * @return ResponseEntity contenant la réponse de l'opération
     */
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody Utilisateur utilisateur) {
        Response response = utilisateurService.registerUtilisateur(utilisateur);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Endpoint pour la connexion d'un utilisateur
     * @param loginRequest Les credentials de connexion (email et mot de passe)
     * @return ResponseEntity contenant la réponse de l'opération
     */
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = utilisateurService.authenticateUtilisateur(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}