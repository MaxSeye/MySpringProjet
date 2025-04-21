package com.dev.MySpringProject.service.interfac;

import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.entity.Utilisateur;

import java.util.List;

public interface IUtilisateurService {

    // Enregistrer un nouvel utilisateur
    Response registerUtilisateur(Utilisateur utilisateur);

    // Authentifier un utilisateur
    Response authenticateUtilisateur(String email, String password);

    // Récupérer tous les utilisateurs
    Response getAllUtilisateurs();

    // Récupérer un utilisateur par son ID
    Response getUtilisateurById(Long id);

    // Récupérer un utilisateur par son email
    Response getUtilisateurByEmail(String email);

    // Mettre à jour un utilisateur
    Response updateUtilisateur(Utilisateur utilisateur);

    // Supprimer un utilisateur par son ID
    Response deleteUtilisateur(Long id);

    // Récupérer les utilisateurs par rôle
    Response getUtilisateursByRole(String role);

    // Vérifier si un email existe déjà
    boolean emailExists(String email);
}