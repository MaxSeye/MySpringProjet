package com.dev.MySpringProject.dto;

import com.dev.MySpringProject.entity.Candidat;
import com.dev.MySpringProject.entity.Notification;
import com.dev.MySpringProject.entity.OffreEmploi;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecruteurDTO extends UtilisateurDTO {
    private Long id;

    private String civilite;
    private String NomDeLaSociete;
    private String DateDeCreation;
    private String pays;
    private String ville;
    private String formeJuridique;
    private String TypSociete;
    private String DomainActivite;

    private List<CandidatDTO> candidats;

    private List<NotificationDTO> notifications;

    private List<OffreEmploiDTO> offresEmploi;

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

    public String getNomDeLaSociete() {
        return NomDeLaSociete;
    }

    public void setNomDeLaSociete(String nomDeLaSociete) {
        NomDeLaSociete = nomDeLaSociete;
    }

    public String getDateDeCreation() {
        return DateDeCreation;
    }

    public void setDateDeCreation(String dateDeCreation) {
        DateDeCreation = dateDeCreation;
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

    public String getFormeJuridique() {
        return formeJuridique;
    }

    public void setFormeJuridique(String formeJuridique) {
        this.formeJuridique = formeJuridique;
    }

    public String getTypSociete() {
        return TypSociete;
    }

    public void setTypSociete(String typSociete) {
        TypSociete = typSociete;
    }

    public String getDomainActivite() {
        return DomainActivite;
    }

    public void setDomainActivite(String domainActivite) {
        DomainActivite = domainActivite;
    }

    public List<CandidatDTO> getCandidats() {
        return candidats;
    }

    public void setCandidats(List<CandidatDTO> candidats) {
        this.candidats = candidats;
    }

    public List<NotificationDTO> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDTO> notifications) {
        this.notifications = notifications;
    }

    public List<OffreEmploiDTO> getOffresEmploi() {
        return offresEmploi;
    }

    public void setOffresEmploi(List<OffreEmploiDTO> offresEmploi) {
        this.offresEmploi = offresEmploi;
    }
}
