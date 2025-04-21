package com.dev.MySpringProject.controller;

import com.dev.MySpringProject.dto.AdministrateurDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.service.interfac.IAdministrateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdministrateurController {

    private final IAdministrateurService administrateurService;

    public AdministrateurController(IAdministrateurService administrateurService) {
        this.administrateurService = administrateurService;
    }

    /**
     * Crée un nouvel administrateur (accessible uniquement par un super admin)
     * @param administrateurDTO DTO contenant les informations de l'administrateur
     * @return Réponse avec le statut et l'administrateur créé
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Response> createAdmin(@RequestBody AdministrateurDTO administrateurDTO) {
        Response response = administrateurService.createAdministrateur(administrateurDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère un administrateur par son ID (accessible par les admins et super admins)
     * @param id ID de l'administrateur
     * @return Réponse avec le statut et l'administrateur trouvé
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> getAdminById(@PathVariable Long id) {
        Response response = administrateurService.getAdministrateurById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère tous les administrateurs (accessible uniquement par les super admins)
     * @return Réponse avec le statut et la liste des administrateurs
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Response> getAllAdmins() {
        Response response = administrateurService.getAllAdministrateurs();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Met à jour un administrateur (accessible par l'admin lui-même ou un super admin)
     * @param id ID de l'administrateur à mettre à jour
     * @param administrateurDTO DTO contenant les nouvelles informations
     * @return Réponse avec le statut et l'administrateur mis à jour
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> updateAdmin(
            @PathVariable Long id,
            @RequestBody AdministrateurDTO administrateurDTO) {
        Response response = administrateurService.updateAdministrateur(id, administrateurDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Supprime un administrateur (accessible uniquement par un super admin)
     * @param id ID de l'administrateur à supprimer
     * @return Réponse avec le statut de l'opération
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Response> deleteAdmin(@PathVariable Long id) {
        Response response = administrateurService.deleteAdministrateur(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Connexion d'un administrateur
     * @param email Email de l'administrateur
     * @param password Mot de passe de l'administrateur
     * @return Réponse avec le statut, le token JWT et les informations de l'admin
     */
    @PostMapping("/login")
    public ResponseEntity<Response> loginAdmin(
            @RequestParam String email,
            @RequestParam String password) {
        Response response = administrateurService.loginAdministrateur(email, password);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère un administrateur par son email (accessible par les admins et super admins)
     * @param email Email de l'administrateur
     * @return Réponse avec le statut et l'administrateur trouvé
     */
    @GetMapping("/email/{email}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> getAdminByEmail(@PathVariable String email) {
        Response response = administrateurService.getAdministrateurByEmail(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère tous les administrateurs avec leurs offres d'emploi (accessible par les recruteurs et admins)
     * @return Réponse avec le statut et la liste des administrateurs avec leurs offres
     */
    @GetMapping("/with-offers")
    @PreAuthorize("hasAnyAuthority('RECRUTEUR', 'ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Response> getAdminsWithOffers() {
        Response response = administrateurService.getAdministrateursWithOffresEmploi();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    /**
     * Récupère les offres d'emploi d'un administrateur spécifique (accessible par tous les utilisateurs authentifiés)
     * @param adminId ID de l'administrateur
     * @return Réponse avec le statut et les offres d'emploi
     */
    @GetMapping("/{adminId}/offers")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response> getOffersByAdminId(@PathVariable Long adminId) {
        Response response = administrateurService.getOffresEmploiByAdminId(adminId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}