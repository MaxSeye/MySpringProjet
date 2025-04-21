package com.dev.MySpringProject.utils;

import com.dev.MySpringProject.dto.*;
import com.dev.MySpringProject.entity.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQURSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();

    // Génère un code de confirmation aléatoire
    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    // Convertit une entité Utilisateur en UtilisateurDTO
    public static UtilisateurDTO mapUtilisateurEntityToUtilisateurDTO(Utilisateur utilisateur) {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setId(utilisateur.getId());
        utilisateurDTO.setEmail(utilisateur.getEmail());
        utilisateurDTO.setPassword(utilisateur.getPassword());
        utilisateurDTO.setPrenom(utilisateur.getPrenom());
        utilisateurDTO.setNom(utilisateur.getNom());
        utilisateurDTO.setNumeroDeTelephone(utilisateur.getNumeroDeTelephone());
        utilisateurDTO.setRole(utilisateur.getRole());
        return utilisateurDTO;
    }

    // Convertit une entité Administrateur en AdministrateurDTO
    public static AdministrateurDTO mapAdministrateurEntityToAdministrateurDTO(Administrateur administrateur) {
        if (administrateur == null) {
            return null;
        }
        AdministrateurDTO administrateurDTO = new AdministrateurDTO();
        administrateurDTO.setId(administrateur.getId());
        administrateurDTO.setEmail(administrateur.getEmail());
        administrateurDTO.setPassword(administrateur.getPassword());
        administrateurDTO.setPrenom(administrateur.getPrenom());
        administrateurDTO.setNom(administrateur.getNom());
        administrateurDTO.setNumeroDeTelephone(administrateur.getNumeroDeTelephone());
        administrateurDTO.setRole(administrateur.getRole());


        if (administrateur.getOffresEmploi() != null) {
            administrateurDTO.setOffresEmploi(administrateur.getOffresEmploi().stream()
                    .map(Utils::mapOffreEmploiEntityToOffreEmploiDTO)
                    .collect(Collectors.toList()));
        }

        return administrateurDTO;
    }

    public static CandidatDTO mapCandidatEntityToCandidatDTO(Candidat candidat) {
        if (candidat == null) {
            return null;
        }

        CandidatDTO candidatDTO = new CandidatDTO();
        candidatDTO.setId(candidat.getId());
        candidatDTO.setCivilite(candidat.getCivilite());
        candidatDTO.setDateDeNaissance(candidat.getDateDeNaissance());
        candidatDTO.setPays(candidat.getPays());
        candidatDTO.setVille(candidat.getVille());
        candidatDTO.setDomainDetude(candidat.getDomaineDetude());
        candidatDTO.setTypeDeFormation(candidat.getTypeDeFormation());
        candidatDTO.setNiveauDetude(candidat.getNiveauDetude());



        if (candidat.getExperiences() != null) {
            candidatDTO.setExperiences(candidat.getExperiences().stream()
                    .map(Utils::mapExperienceEntityToExperienceDTO)
                    .collect(Collectors.toList()));
        }

        if (candidat.getFormations() != null) {
            candidatDTO.setFormations(candidat.getFormations().stream()
                    .map(Utils::mapFormationEntityToFormationDTO)
                    .collect(Collectors.toList()));
        }

        if (candidat.getFavorisOffreEmploi() != null) {
            candidatDTO.setFavorisOffreEmploi(candidat.getFavorisOffreEmploi().stream()
                    .map(Utils::mapFavorisOffreEmploiEntityToFavorisOffreEmploiDTO)
                    .collect(Collectors.toList()));
        }

        if (candidat.getNotifications() != null) {
            candidatDTO.setNotifications(candidat.getNotifications().stream()
                    .map(Utils::mapNotificationEntityToNotificationDTO)
                    .collect(Collectors.toList()));
        }

        if (candidat.getRecruteur() != null) {
            candidatDTO.setRecruteur(mapRecruteurEntityToRecruteurDTO(candidat.getRecruteur()));
        }

        return candidatDTO;
    }

    public static CandidatureEnCoursDTO mapCandidatureEnCoursEntityToCandidatureEnCoursDTO(CandidatureEnCours candidature) {
        if (candidature == null) {
            return null;
        }

        CandidatureEnCoursDTO candidatureDTO = new CandidatureEnCoursDTO();
        candidatureDTO.setId(candidature.getId());
        candidatureDTO.setEnCours(candidature.getEnCours());
        candidatureDTO.setStatutDiscussion(candidature.getStatutDiscussion());
        candidatureDTO.setStatutSelection(candidature.getStatutSelection());

        if (candidature.getCandidat() != null) {
            candidatureDTO.setCandidat(mapCandidatEntityToCandidatDTO(candidature.getCandidat()));
        }

        return candidatureDTO;
    }

    public static ExperienceDTO mapExperienceEntityToExperienceDTO(Experience experience) {
        if (experience == null) {
            return null;
        }

        ExperienceDTO experienceDTO = new ExperienceDTO();
        experienceDTO.setId(experience.getId());
        experienceDTO.setPoste(experience.getPoste());
        experienceDTO.setDateDebut(experience.getDateDebut());
        experienceDTO.setDateFin(experience.getDateFin());
        experienceDTO.setDescription(experience.getDescription());
        experienceDTO.setEntreprise(experience.getEntreprise());

        if (experience.getCandidat() != null) {
            experienceDTO.setCandidat(mapCandidatEntityToCandidatDTO(experience.getCandidat()));
        }

        return experienceDTO;
    }

    public static FavorisOffreEmploiDTO mapFavorisOffreEmploiEntityToFavorisOffreEmploiDTO(FavorisOffreEmploi favoris) {
        if (favoris == null) {
            return null;
        }

        FavorisOffreEmploiDTO favorisDTO = new FavorisOffreEmploiDTO();
        favorisDTO.setId(favoris.getId());

        if (favoris.getCandidats() != null) {
            favorisDTO.setCandidats(favoris.getCandidats().stream()
                    .map(Utils::mapCandidatEntityToCandidatDTO)
                    .collect(Collectors.toList()));
        }

        return favorisDTO;
    }

    public static FormationDTO mapFormationEntityToFormationDTO(Formation formation) {
        if (formation == null) {
            return null;
        }

        FormationDTO formationDTO = new FormationDTO();
        formationDTO.setId(formation.getId());
        formationDTO.setEcole(formation.getEcole());
        formationDTO.setDiplome(formation.getDiplome());
        formationDTO.setAnneeScolaire(formation.getAnneeScolaire());

        if (formation.getCandidat() != null) {
            formationDTO.setCandidat(mapCandidatEntityToCandidatDTO(formation.getCandidat()));
        }

        return formationDTO;
    }

    public static NotificationDTO mapNotificationEntityToNotificationDTO(Notification notification) {
        if (notification == null) {
            return null;
        }

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setContenu(notification.getContenu());
        notificationDTO.setStatut(notification.getStatut());
        notificationDTO.setHeure(notification.getHeure());

        if (notification.getCandidat() != null) {
            notificationDTO.setCandidat(mapCandidatEntityToCandidatDTO(notification.getCandidat()));
        }

        if (notification.getRecruteur() != null) {
            notificationDTO.setRecruteur(mapRecruteurEntityToRecruteurDTO(notification.getRecruteur()));
        }

        return notificationDTO;
    }

    public static OffreEmploiDTO mapOffreEmploiEntityToOffreEmploiDTO(OffreEmploi offre) {
        if (offre == null) {
            return null;
        }

        OffreEmploiDTO offreDTO = new OffreEmploiDTO();
        offreDTO.setId(offre.getId());
        offreDTO.setTitre(offre.getTitre());
        offreDTO.setDescription(offre.getDescription());
        offreDTO.setSalaire(offre.getSalaire());
        offreDTO.setLocalisation(offre.getLocalisation());
        offreDTO.setContrat(offre.getContrat());
        offreDTO.setAutres(offre.getAutres());

        if (offre.getCandidats() != null) {
            offreDTO.setCandidats(offre.getCandidats().stream()
                    .map(Utils::mapCandidatEntityToCandidatDTO)
                    .collect(Collectors.toList()));
        }

        if (offre.getRecruteur() != null) {
            offreDTO.setRecruteur(mapRecruteurEntityToRecruteurDTO(offre.getRecruteur()));
        }

        if (offre.getAdministrateur() != null) {
            offreDTO.setAdministrateur(mapAdministrateurEntityToAdministrateurDTO(offre.getAdministrateur()));
        }

        return offreDTO;
    }

    public static RecruteurDTO mapRecruteurEntityToRecruteurDTO(Recruteur recruteur) {
        if (recruteur == null) {
            return null;
        }

        RecruteurDTO recruteurDTO = new RecruteurDTO();
        recruteurDTO.setId(recruteur.getId());
        recruteurDTO.setCivilite(recruteur.getCivilite());
        recruteurDTO.setNomDeLaSociete(recruteur.getNomDeLaSociete());
        recruteurDTO.setDateDeCreation(recruteur.getDateDeCreation());
        recruteurDTO.setPays(recruteur.getPays());
        recruteurDTO.setVille(recruteur.getVille());
        recruteurDTO.setFormeJuridique(recruteur.getFormeJuridique());
        recruteurDTO.setTypSociete(recruteur.getTypSociete());
        recruteurDTO.setDomainActivite(recruteur.getDomainActivite());


        if (recruteur.getCandidats() != null) {
            recruteurDTO.setCandidats(recruteur.getCandidats().stream()
                    .map(Utils::mapCandidatEntityToCandidatDTO)
                    .collect(Collectors.toList()));
        }

        if (recruteur.getNotifications() != null) {
            recruteurDTO.setNotifications(recruteur.getNotifications().stream()
                    .map(Utils::mapNotificationEntityToNotificationDTO)
                    .collect(Collectors.toList()));
        }

        if (recruteur.getOffresEmploi() != null) {
            recruteurDTO.setOffresEmploi(recruteur.getOffresEmploi().stream()
                    .map(Utils::mapOffreEmploiEntityToOffreEmploiDTO)
                    .collect(Collectors.toList()));
        }

        return recruteurDTO;
    }
}