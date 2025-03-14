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

    private String formeJuridique;
    private String domaineActivite;


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

    public String getFormeJuridique() {
        return formeJuridique;
    }

    public void setFormeJuridique(String formeJuridique) {
        this.formeJuridique = formeJuridique;
    }

    public String getDomaineActivite() {
        return domaineActivite;
    }

    public void setDomaineActivite(String domaineActivite) {
        this.domaineActivite = domaineActivite;
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

