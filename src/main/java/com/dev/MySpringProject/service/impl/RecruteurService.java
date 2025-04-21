package com.dev.MySpringProject.service.impl;

import com.dev.MySpringProject.dto.*;
import com.dev.MySpringProject.entity.Recruteur;
import com.dev.MySpringProject.repo.RecruteurRepository;
import com.dev.MySpringProject.service.interfac.IRecruteurService;
import com.dev.MySpringProject.utils.JWTUtils;
import com.dev.MySpringProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecruteurService implements IRecruteurService {

    @Autowired
    private RecruteurRepository recruteurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    @Transactional
    public Response registerRecruteur(RecruteurDTO recruteurDTO) {
        Response response = new Response();
        try {
            // Vérifier si l'email existe déjà
            if (recruteurRepository.findByEmail(recruteurDTO.getEmail()).isPresent()) {
                response.setStatusCode(400);
                response.setMessage("Cet email est déjà utilisé");
                return response;
            }

            // Vérifier si le nom de société existe déjà
            if (recruteurRepository.existsByNomDeLaSociete(recruteurDTO.getNomDeLaSociete())) {
                response.setStatusCode(400);
                response.setMessage("Ce nom de société est déjà utilisé");
                return response;
            }

            // Convertir DTO en entité
            Recruteur recruteur = new Recruteur();
            mapRecruteurDTOToEntity(recruteurDTO, recruteur);

            // Encoder le mot de passe
            recruteur.setPassword(passwordEncoder.encode(recruteurDTO.getPassword()));

            // Définir le rôle
            recruteur.setRole("RECRUTEUR");

            // Sauvegarder le recruteur
            Recruteur savedRecruteur = recruteurRepository.save(recruteur);

            // Générer le token JWT
            String token = jwtUtils.generateToken(savedRecruteur);

            // Préparer la réponse
            response.setStatusCode(200);
            response.setMessage("Inscription réussie");
            response.setToken(token);
            response.setRole(savedRecruteur.getRole());
            response.setRecruteur(Utils.mapRecruteurEntityToRecruteurDTO(savedRecruteur));

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de l'inscription: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response loginRecruteur(String email, String password) {
        Response response = new Response();
        try {
            Optional<Recruteur> recruteurOpt = recruteurRepository.findByEmail(email);

            if (recruteurOpt.isEmpty() || !passwordEncoder.matches(password, recruteurOpt.get().getPassword())) {
                response.setStatusCode(401);
                response.setMessage("Email ou mot de passe incorrect");
                return response;
            }

            Recruteur recruteur = recruteurOpt.get();
            String token = jwtUtils.generateToken(recruteur);

            response.setStatusCode(200);
            response.setMessage("Connexion réussie");
            response.setToken(token);
            response.setRole(recruteur.getRole());
            response.setRecruteur(Utils.mapRecruteurEntityToRecruteurDTO(recruteur));

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la connexion: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRecruteurById(Long id) {
        Response response = new Response();
        try {
            Recruteur recruteur = recruteurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Recruteur non trouvé avec l'ID: " + id));

            response.setStatusCode(200);
            response.setMessage("Recruteur trouvé");
            response.setRecruteur(Utils.mapRecruteurEntityToRecruteurDTO(recruteur));
        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    public Response updateRecruteur(Long id, RecruteurDTO recruteurDTO) {
        Response response = new Response();
        try {
            Recruteur existingRecruteur = recruteurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Recruteur non trouvé avec l'ID: " + id));

            // Mettre à jour les champs
            existingRecruteur.setCivilite(recruteurDTO.getCivilite());
            existingRecruteur.setPrenom(recruteurDTO.getPrenom());
            existingRecruteur.setNom(recruteurDTO.getNom());
            existingRecruteur.setNumeroDeTelephone(recruteurDTO.getNumeroDeTelephone());
            existingRecruteur.setNomDeLaSociete(recruteurDTO.getNomDeLaSociete());
            existingRecruteur.setDateDeCreation(recruteurDTO.getDateDeCreation());
            existingRecruteur.setPays(recruteurDTO.getPays());
            existingRecruteur.setVille(recruteurDTO.getVille());
            existingRecruteur.setFormeJuridique(recruteurDTO.getFormeJuridique());
            existingRecruteur.setTypSociete(recruteurDTO.getTypSociete());
            existingRecruteur.setDomainActivite(recruteurDTO.getDomainActivite());

            // Si le mot de passe est fourni, l'encoder et le mettre à jour
            if (recruteurDTO.getPassword() != null && !recruteurDTO.getPassword().isEmpty()) {
                existingRecruteur.setPassword(passwordEncoder.encode(recruteurDTO.getPassword()));
            }

            Recruteur updatedRecruteur = recruteurRepository.save(existingRecruteur);

            response.setStatusCode(200);
            response.setMessage("Recruteur mis à jour avec succès");
            response.setRecruteur(Utils.mapRecruteurEntityToRecruteurDTO(updatedRecruteur));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la mise à jour: " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    public Response deleteRecruteur(Long id) {
        Response response = new Response();
        try {
            Recruteur recruteur = recruteurRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Recruteur non trouvé avec l'ID: " + id));

            // Supprimer les relations (si nécessaire)
            // ...

            recruteurRepository.delete(recruteur);

            response.setStatusCode(200);
            response.setMessage("Recruteur supprimé avec succès");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la suppression: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRecruteursBySociete(String nomSociete) {
        Response response = new Response();
        try {
            List<Recruteur> recruteurs = recruteurRepository.findByNomDeLaSociete(nomSociete);
            List<RecruteurDTO> recruteurDTOs = recruteurs.stream()
                    .map(Utils::mapRecruteurEntityToRecruteurDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(recruteurDTOs.isEmpty() ? "Aucun recruteur trouvé" : "Recruteurs trouvés");
            response.setRecruteurList(recruteurDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la recherche: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRecruteursByPays(String pays) {
        Response response = new Response();
        try {
            List<Recruteur> recruteurs = recruteurRepository.findByPays(pays);
            List<RecruteurDTO> recruteurDTOs = recruteurs.stream()
                    .map(Utils::mapRecruteurEntityToRecruteurDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(recruteurDTOs.isEmpty() ? "Aucun recruteur trouvé" : "Recruteurs trouvés");
            response.setRecruteurList(recruteurDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la recherche: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRecruteursByVille(String ville) {
        Response response = new Response();
        try {
            List<Recruteur> recruteurs = recruteurRepository.findByVille(ville);
            List<RecruteurDTO> recruteurDTOs = recruteurs.stream()
                    .map(Utils::mapRecruteurEntityToRecruteurDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(recruteurDTOs.isEmpty() ? "Aucun recruteur trouvé" : "Recruteurs trouvés");
            response.setRecruteurList(recruteurDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la recherche: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRecruteursByDomaineActivite(String domaine) {
        Response response = new Response();
        try {
            List<Recruteur> recruteurs = recruteurRepository.findByDomainActivite(domaine);
            List<RecruteurDTO> recruteurDTOs = recruteurs.stream()
                    .map(Utils::mapRecruteurEntityToRecruteurDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(recruteurDTOs.isEmpty() ? "Aucun recruteur trouvé" : "Recruteurs trouvés");
            response.setRecruteurList(recruteurDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la recherche: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response checkSocieteExists(String nomSociete) {
        Response response = new Response();
        try {
            boolean exists = recruteurRepository.existsByNomDeLaSociete(nomSociete);
            response.setStatusCode(200);
            response.setMessage(exists ? "Le nom de société existe déjà" : "Nom de société disponible");
            response.setMessage(String.valueOf(exists));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la vérification: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getOffresByRecruteur(Long recruteurId) {
        Response response = new Response();
        try {
            Recruteur recruteur = recruteurRepository.findById(recruteurId)
                    .orElseThrow(() -> new RuntimeException("Recruteur non trouvé avec l'ID: " + recruteurId));

            List<OffreEmploiDTO> offreDTOs = recruteur.getOffresEmploi().stream()
                    .map(Utils::mapOffreEmploiEntityToOffreEmploiDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(offreDTOs.isEmpty() ? "Aucune offre trouvée" : "Offres trouvées");
            response.setOffreEmploiList(offreDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la récupération des offres: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getNotificationsByRecruteur(Long recruteurId) {
        Response response = new Response();
        try {
            Recruteur recruteur = recruteurRepository.findById(recruteurId)
                    .orElseThrow(() -> new RuntimeException("Recruteur non trouvé avec l'ID: " + recruteurId));

            List<NotificationDTO> notificationDTOs = recruteur.getNotifications().stream()
                    .map(Utils::mapNotificationEntityToNotificationDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(notificationDTOs.isEmpty() ? "Aucune notification trouvée" : "Notifications trouvées");
            response.setNotificationList(notificationDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la récupération des notifications: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getCandidatsByRecruteur(Long recruteurId) {
        Response response = new Response();
        try {
            Recruteur recruteur = recruteurRepository.findById(recruteurId)
                    .orElseThrow(() -> new RuntimeException("Recruteur non trouvé avec l'ID: " + recruteurId));

            List<CandidatDTO> candidatDTOs = recruteur.getCandidats().stream()
                    .map(Utils::mapCandidatEntityToCandidatDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(candidatDTOs.isEmpty() ? "Aucun candidat trouvé" : "Candidats trouvés");
            response.setCandidatList(candidatDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la récupération des candidats: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRecruteurStats(Long recruteurId) {
        Response response = new Response();
        try {
            Recruteur recruteur = recruteurRepository.findById(recruteurId)
                    .orElseThrow(() -> new RuntimeException("Recruteur non trouvé avec l'ID: " + recruteurId));

            // Statistiques basiques
            int nombreOffres = recruteur.getOffresEmploi().size();
            int nombreCandidats = recruteur.getCandidats().size();
            int nombreNotifications = recruteur.getNotifications().size();

            // Vous pouvez ajouter d'autres statistiques ici
            String stats = String.format("Offres: %d, Candidats: %d, Notifications: %d",
                    nombreOffres, nombreCandidats, nombreNotifications);

            response.setStatusCode(200);
            response.setMessage("Statistiques du recruteur");
            response.setMessage(stats);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors du calcul des statistiques: " + e.getMessage());
        }
        return response;
    }

    // Méthode utilitaire pour mapper DTO vers Entity
    private void mapRecruteurDTOToEntity(RecruteurDTO dto, Recruteur entity) {
        entity.setCivilite(dto.getCivilite());
        entity.setPrenom(dto.getPrenom());
        entity.setNom(dto.getNom());
        entity.setEmail(dto.getEmail());
        entity.setNumeroDeTelephone(dto.getNumeroDeTelephone());
        entity.setNomDeLaSociete(dto.getNomDeLaSociete());
        entity.setDateDeCreation(dto.getDateDeCreation());
        entity.setPays(dto.getPays());
        entity.setVille(dto.getVille());
        entity.setFormeJuridique(dto.getFormeJuridique());
        entity.setTypSociete(dto.getTypSociete());
        entity.setDomainActivite(dto.getDomainActivite());
    }
}