package com.dev.MySpringProject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "recruteur")
public class Recruteur extends Utilisateur{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String civilite;
    private String nomDeLaSociete;
    private String dateDeCreation;
    private String pays;
    private String ville;
    private String formeJuridique;
    private String typSociete;
    private String domainActivite;


    @OneToMany(mappedBy = "recruteur")
    private List<Candidat> candidats;

    @OneToMany(mappedBy = "recruteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "recruteur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OffreEmploi> offresEmploi;

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

    public String getNomDeLaSociete() {
        return nomDeLaSociete;
    }

    public void setNomDeLaSociete(String nomDeLaSociete) {
        this.nomDeLaSociete = nomDeLaSociete;
    }

    public String getDateDeCreation() {
        return dateDeCreation;
    }

    public void setDateDeCreation(String dateDeCreation) {
        this.dateDeCreation = dateDeCreation;
    }

    public String getTypSociete() {
        return typSociete;
    }

    public void setTypSociete(String typSociete) {
        this.typSociete = typSociete;
    }

    public String getDomainActivite() {
        return domainActivite;
    }

    public void setDomainActivite(String domainActivite) {
        this.domainActivite = domainActivite;
    }

    public List<Candidat> getCandidats() {
        return candidats;
    }

    public void setCandidats(List<Candidat> candidats) {
        this.candidats = candidats;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<OffreEmploi> getOffresEmploi() {
        return offresEmploi;
    }

    public void setOffresEmploi(List<OffreEmploi> offresEmploi) {
        this.offresEmploi = offresEmploi;
    }

    //    public void addNotification(Notification notification) {
//        notifications.add(notification);
//        notification.setRecruteur(this);
//    }
//
//    public void removeNotification(Notification notification) {
//        notifications.remove(notification);
//        notification.setRecruteur(null);
//    }
}

