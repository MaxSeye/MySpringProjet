package com.dev.MySpringProject.service.impl;

import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.dto.UtilisateurDTO;
import com.dev.MySpringProject.entity.Utilisateur;
import com.dev.MySpringProject.repo.UtilisateurRepository;
import com.dev.MySpringProject.service.interfac.IUtilisateurService;
import com.dev.MySpringProject.utils.JWTUtils;
import com.dev.MySpringProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilisateurService implements IUtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    @Transactional
    public Response registerUtilisateur(Utilisateur utilisateur) {
        Response response = new Response();
        try {
            // Vérifier si l'email existe déjà
            if (utilisateurRepository.existsByEmail(utilisateur.getEmail())) {
                response.setStatusCode(400);
                response.setMessage("Cet email est déjà utilisé");
                return response;
            }

            // Encoder le mot de passe
            utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));

            // Sauvegarder l'utilisateur
            Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);

            // Générer le token JWT
            String token = jwtUtils.generateToken(savedUtilisateur);

            // Préparer la réponse
            response.setStatusCode(200);
            response.setMessage("Inscription réussie");
            response.setToken(token);
            response.setRole(savedUtilisateur.getRole());
            response.setUtilisateur(Utils.mapUtilisateurEntityToUtilisateurDTO(savedUtilisateur));

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de l'inscription: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response authenticateUtilisateur(String email, String password) {
        Response response = new Response();
        try {
            Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);

            if (utilisateurOpt.isEmpty() || !passwordEncoder.matches(password, utilisateurOpt.get().getPassword())) {
                response.setStatusCode(401);
                response.setMessage("Email ou mot de passe incorrect");
                return response;
            }

            Utilisateur utilisateur = utilisateurOpt.get();
            String token = jwtUtils.generateToken(utilisateur);

            response.setStatusCode(200);
            response.setMessage("Connexion réussie");
            response.setToken(token);
            response.setRole(utilisateur.getRole());
            response.setUtilisateur(Utils.mapUtilisateurEntityToUtilisateurDTO(utilisateur));

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la connexion: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUtilisateurs() {
        Response response = new Response();
        try {
            List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
            List<UtilisateurDTO> utilisateurDTOs = utilisateurs.stream()
                    .map(Utils::mapUtilisateurEntityToUtilisateurDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Liste des utilisateurs récupérée");
            response.setUtilisateurList(utilisateurDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la récupération: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUtilisateurById(Long id) {
        Response response = new Response();
        try {
            Utilisateur utilisateur = utilisateurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));

            response.setStatusCode(200);
            response.setMessage("Utilisateur trouvé");
            response.setUtilisateur(Utils.mapUtilisateurEntityToUtilisateurDTO(utilisateur));
        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUtilisateurByEmail(String email) {
        Response response = new Response();
        try {
            Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email: " + email));

            response.setStatusCode(200);
            response.setMessage("Utilisateur trouvé");
            response.setUtilisateur(Utils.mapUtilisateurEntityToUtilisateurDTO(utilisateur));
        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    public Response updateUtilisateur(Utilisateur utilisateur) {
        Response response = new Response();
        try {
            Utilisateur existingUtilisateur = utilisateurRepository.findById(utilisateur.getId())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            // Mettre à jour les champs
            existingUtilisateur.setEmail(utilisateur.getEmail());
            existingUtilisateur.setPrenom(utilisateur.getPrenom());
            existingUtilisateur.setNom(utilisateur.getNom());
            existingUtilisateur.setNumeroDeTelephone(utilisateur.getNumeroDeTelephone());
            existingUtilisateur.setRole(utilisateur.getRole());

            // Si le mot de passe est fourni, l'encoder et le mettre à jour
            if (utilisateur.getPassword() != null && !utilisateur.getPassword().isEmpty()) {
                existingUtilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
            }

            Utilisateur updatedUtilisateur = utilisateurRepository.save(existingUtilisateur);

            response.setStatusCode(200);
            response.setMessage("Utilisateur mis à jour avec succès");
            response.setUtilisateur(Utils.mapUtilisateurEntityToUtilisateurDTO(updatedUtilisateur));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la mise à jour: " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    public Response deleteUtilisateur(Long id) {
        Response response = new Response();
        try {
            Utilisateur utilisateur = utilisateurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + id));

            utilisateurRepository.delete(utilisateur);

            response.setStatusCode(200);
            response.setMessage("Utilisateur supprimé avec succès");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la suppression: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUtilisateursByRole(String role) {
        Response response = new Response();
        try {
            List<Utilisateur> utilisateurs = utilisateurRepository.findByRole(role);
            List<UtilisateurDTO> utilisateurDTOs = utilisateurs.stream()
                    .map(Utils::mapUtilisateurEntityToUtilisateurDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(utilisateurDTOs.isEmpty() ? "Aucun utilisateur trouvé" : "Utilisateurs trouvés");
            response.setUtilisateurList(utilisateurDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la recherche: " + e.getMessage());
        }
        return response;
    }

    @Override
    public boolean emailExists(String email) {
        return utilisateurRepository.existsByEmail(email);
    }

    // Implémentation pour Spring Security
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));
    }
}