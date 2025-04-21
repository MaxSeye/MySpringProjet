package com.dev.MySpringProject.dto;

import com.dev.MySpringProject.entity.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidatDTO extends UtilisateurDTO {

    private Long id;

    private String civilite;
    private String DateDeNaissance;
    private String pays;
    private String ville;
    private String DomainDetude;
    private String TypeDeFormation;
    private String NiveauDetude;


    private RecruteurDTO recruteur;

    private List<ExperienceDTO> experiences;

    private List<FormationDTO> formations;

    private List<CandidatureEnCours> candidaturesEnCours;


    private List<OffreEmploiDTO> offresEmploi;


    private List<FavorisOffreEmploiDTO> favorisOffreEmploi;

    private List<NotificationDTO> notifications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCivilite() {
        return civilite;
    }

    public void setCivilite(String civilite) {
        this.civilite = civilite;
    }

    public String getDateDeNaissance() {
        return DateDeNaissance;
    }

    public void setDateDeNaissance(String dateDeNaissance) {
        DateDeNaissance = dateDeNaissance;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDomainDetude() {
        return DomainDetude;
    }

    public void setDomainDetude(String domainDetude) {
        DomainDetude = domainDetude;
    }

    public String getTypeDeFormation() {
        return TypeDeFormation;
    }

    public void setTypeDeFormation(String typeDeFormation) {
        TypeDeFormation = typeDeFormation;
    }

    public String getNiveauDetude() {
        return NiveauDetude;
    }

    public void setNiveauDetude(String niveauDetude) {
        NiveauDetude = niveauDetude;
    }

    public RecruteurDTO getRecruteur() {
        return recruteur;
    }

    public void setRecruteur(RecruteurDTO recruteur) {
        this.recruteur = recruteur;
    }

    public List<ExperienceDTO> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ExperienceDTO> experiences) {
        this.experiences = experiences;
    }

    public List<FormationDTO> getFormations() {
        return formations;
    }

    public void setFormations(List<FormationDTO> formations) {
        this.formations = formations;
    }

    public List<CandidatureEnCours> getCandidaturesEnCours() {
        return candidaturesEnCours;
    }

    public void setCandidaturesEnCours(List<CandidatureEnCours> candidaturesEnCours) {
        this.candidaturesEnCours = candidaturesEnCours;
    }

    public List<OffreEmploiDTO> getOffresEmploi() {
        return offresEmploi;
    }

    public void setOffresEmploi(List<OffreEmploiDTO> offresEmploi) {
        this.offresEmploi = offresEmploi;
    }

    public List<FavorisOffreEmploiDTO> getFavorisOffreEmploi() {
        return favorisOffreEmploi;
    }

    public void setFavorisOffreEmploi(List<FavorisOffreEmploiDTO> favorisOffreEmploi) {
        this.favorisOffreEmploi = favorisOffreEmploi;
    }

    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }
}
