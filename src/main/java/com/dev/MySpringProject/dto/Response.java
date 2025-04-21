package com.dev.MySpringProject.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)

    public class Response {

        private int statusCode; // Code HTTP de la réponse
        private String message; // Message descriptif

        // Informationss d'authentification
        private String token; // Token JWT pour l'authentification
        private String role; // Rôle de l'utilisateur (ex: "ADMIN", "CANDIDAT", "RECRUTEUR")
        private String expirationTime; // Date d'expiration du token

        // Objets DTO pour les entités principales
        private AdministrateurDTO administrateur;
        private CandidatDTO candidat;
        private RecruteurDTO recruteur;
        private UtilisateurDTO utilisateur;
        private OffreEmploiDTO offreEmploi;
        private CandidatureEnCoursDTO candidatureEnCours;
        private ExperienceDTO experience;
        private FormationDTO formation;
        private NotificationDTO notification;
        private FavorisOffreEmploiDTO favorisOffreEmploi;

        // Listes d'objets DTO pour les réponses multiples
        private List<AdministrateurDTO> administrateurList;
        private List<CandidatDTO> candidatList;
        private List<RecruteurDTO> recruteurList;
        private List<UtilisateurDTO> utilisateurList;
        private List<OffreEmploiDTO> offreEmploiList;
        private List<CandidatureEnCoursDTO> candidatureEnCoursList;
        private List<ExperienceDTO> experienceList;
        private List<FormationDTO> formationList;
        private List<NotificationDTO> notificationList;
        private List<FavorisOffreEmploiDTO> favorisOffreEmploiList;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }

    public AdministrateurDTO getAdministrateur() {
        return administrateur;
    }

    public void setAdministrateur(AdministrateurDTO administrateur) {
        this.administrateur = administrateur;
    }

    public CandidatDTO getCandidat() {
        return candidat;
    }

    public void setCandidat(CandidatDTO candidat) {
        this.candidat = candidat;
    }

    public RecruteurDTO getRecruteur() {
        return recruteur;
    }

    public void setRecruteur(RecruteurDTO recruteur) {
        this.recruteur = recruteur;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    public OffreEmploiDTO getOffreEmploi() {
        return offreEmploi;
    }

    public void setOffreEmploi(OffreEmploiDTO offreEmploi) {
        this.offreEmploi = offreEmploi;
    }

    public CandidatureEnCoursDTO getCandidatureEnCours() {
        return candidatureEnCours;
    }

    public void setCandidatureEnCours(CandidatureEnCoursDTO candidatureEnCours) {
        this.candidatureEnCours = candidatureEnCours;
    }

    public ExperienceDTO getExperience() {
        return experience;
    }

    public void setExperience(ExperienceDTO experience) {
        this.experience = experience;
    }

    public FormationDTO getFormation() {
        return formation;
    }

    public void setFormation(FormationDTO formation) {
        this.formation = formation;
    }

    public NotificationDTO getNotification() {
        return notification;
    }

    public void setNotification(NotificationDTO notification) {
        this.notification = notification;
    }

    public FavorisOffreEmploiDTO getFavorisOffreEmploi() {
        return favorisOffreEmploi;
    }

    public void setFavorisOffreEmploi(FavorisOffreEmploiDTO favorisOffreEmploi) {
        this.favorisOffreEmploi = favorisOffreEmploi;
    }

    public List<AdministrateurDTO> getAdministrateurList() {
        return administrateurList;
    }

    public void setAdministrateurList(List<AdministrateurDTO> administrateurList) {
        this.administrateurList = administrateurList;
    }

    public List<CandidatDTO> getCandidatList() {
        return candidatList;
    }

    public void setCandidatList(List<CandidatDTO> candidatList) {
        this.candidatList = candidatList;
    }

    public List<RecruteurDTO> getRecruteurList() {
        return recruteurList;
    }

    public void setRecruteurList(List<RecruteurDTO> recruteurList) {
        this.recruteurList = recruteurList;
    }

    public List<UtilisateurDTO> getUtilisateurList() {
        return utilisateurList;
    }

    public void setUtilisateurList(List<UtilisateurDTO> utilisateurList) {
        this.utilisateurList = utilisateurList;
    }

    public List<OffreEmploiDTO> getOffreEmploiList() {
        return offreEmploiList;
    }

    public void setOffreEmploiList(List<OffreEmploiDTO> offreEmploiList) {
        this.offreEmploiList = offreEmploiList;
    }

    public List<CandidatureEnCoursDTO> getCandidatureEnCoursList() {
        return candidatureEnCoursList;
    }

    public void setCandidatureEnCoursList(List<CandidatureEnCoursDTO> candidatureEnCoursList) {
        this.candidatureEnCoursList = candidatureEnCoursList;
    }

    public List<ExperienceDTO> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(List<ExperienceDTO> experienceList) {
        this.experienceList = experienceList;
    }

    public List<FormationDTO> getFormationList() {
        return formationList;
    }

    public void setFormationList(List<FormationDTO> formationList) {
        this.formationList = formationList;
    }

    public List<NotificationDTO> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<NotificationDTO> notificationList) {
        this.notificationList = notificationList;
    }

    public List<FavorisOffreEmploiDTO> getFavorisOffreEmploiList() {
        return favorisOffreEmploiList;
    }

    public void setFavorisOffreEmploiList(List<FavorisOffreEmploiDTO> favorisOffreEmploiList) {
        this.favorisOffreEmploiList = favorisOffreEmploiList;
    }
}
