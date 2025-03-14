package com.dev.MySpringProject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "candidat")
public class Candidat extends Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String typeFormation;
    private String niveauEtudes;
    private String divers;
    private String langues;
    private String competences;

    @ManyToOne
    @JoinColumn(name = "recruteur_id")
    private Recruteur recruteur;

    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences;

    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Formation> formations;

    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CandidatureEnCours> candidaturesEnCours;

    @ManyToMany
    @JoinTable(
            name = "candidat_offre_emploi", // Nom de la table de jointure
            joinColumns = @JoinColumn(name = "candidat_id"), // Clé étrangère vers Candidat
            inverseJoinColumns = @JoinColumn(name = "offre_emploi_id")
    )
    private List<OffreEmploi> offresEmploi;

    @ManyToMany
    @JoinTable(
            name = "candidat_favoris_offre_emploi", // Nom de la table de jointure
            joinColumns = @JoinColumn(name = "candidat_id"), // Clé étrangère vers Candidat
            inverseJoinColumns = @JoinColumn(name = "favoris_offre_emploi_id") // Clé étrangère vers FavorisOffreEmploi
    )
    private List<FavorisOffreEmploi> favorisOffreEmploi;

    @OneToMany(mappedBy = "candidat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeFormation() {
        return typeFormation;
    }

    public void setTypeFormation(String typeFormation) {
        this.typeFormation = typeFormation;
    }

    public String getNiveauEtudes() {
        return niveauEtudes;
    }

    public void setNiveauEtudes(String niveauEtudes) {
        this.niveauEtudes = niveauEtudes;
    }

    public String getDivers() {
        return divers;
    }

    public void setDivers(String divers) {
        this.divers = divers;
    }

    public String getLangues() {
        return langues;
    }

    public void setLangues(String langues) {
        this.langues = langues;
    }

    public String getCompetences() {
        return competences;
    }

    public void setCompetences(String competences) {
        this.competences = competences;
    }

    public Recruteur getRecruteur() {
        return recruteur;
    }

    public void setRecruteur(Recruteur recruteur) {
        this.recruteur = recruteur;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public List<Formation> getFormations() {
        return formations;
    }

    public void setFormations(List<Formation> formations) {
        this.formations = formations;
    }

    public List<CandidatureEnCours> getCandidaturesEnCours() {
        return candidaturesEnCours;
    }

    public void setCandidaturesEnCours(List<CandidatureEnCours> candidaturesEnCours) {
        this.candidaturesEnCours = candidaturesEnCours;
    }

    public List<OffreEmploi> getOffresEmploi() {
        return offresEmploi;
    }

    public void setOffresEmploi(List<OffreEmploi> offresEmploi) {
        this.offresEmploi = offresEmploi;
    }

    public List<FavorisOffreEmploi> getFavorisOffreEmploi() {
        return favorisOffreEmploi;
    }

    public void setFavorisOffreEmploi(List<FavorisOffreEmploi> favorisOffreEmploi) {
        this.favorisOffreEmploi = favorisOffreEmploi;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    // Pour maintenir la cohérence de la relation bidirectionnelle, vous pouvez ajouter des méthodes utilitaires dans la classe Candidat pour ajouter ou supprimer des expériences :
//    public void addExperience(Experience experience) {
//        experiences.add(experience);
//        experience.setCandidat(this);
//    }
//
//    public void removeExperience(Experience experience) {
//        experiences.remove(experience);
//        experience.setCandidat(null);
//    }


//    Pour maintenir la cohérence de la relation bidirectionnelle, vous pouvez ajouter des méthodes utilitaires dans la classe Candidat pour ajouter ou supprimer des formations
//    public void addFormation(Formation formation) {
//        formations.add(formation);
//        formation.setCandidat(this);
//    }
//
//    public void removeFormation(Formation formation) {
//        formations.remove(formation);
//        formation.setCandidat(null);
//    }

//public void addCandidatureEnCours(CandidatureEnCours candidatureEnCours) {
//    candidaturesEnCours.add(candidatureEnCours);
//    candidatureEnCours.setCandidat(this);
//}
//
//    public void removeCandidatureEnCours(CandidatureEnCours candidatureEnCours) {
//        candidaturesEnCours.remove(candidatureEnCours);
//        candidatureEnCours.setCandidat(null);
//    }

//    public void addOffreEmploi(OffreEmploi offreEmploi) {
//        offresEmploi.add(offreEmploi);
//        offreEmploi.getCandidats().add(this);
//    }
//
//    public void removeOffreEmploi(OffreEmploi offreEmploi) {
//        offresEmploi.remove(offreEmploi);
//        offreEmploi.getCandidats().remove(this);
//    }

//    public void addFavorisOffreEmploi(FavorisOffreEmploi favorisOffreEmploi) {
//        favorisOffreEmploi.add(favorisOffreEmploi);
//        favorisOffreEmploi.getCandidats().add(this);
//    }
//
//    public void removeFavorisOffreEmploi(FavorisOffreEmploi favorisOffreEmploi) {
//        favorisOffreEmploi.remove(favorisOffreEmploi);
//        favorisOffreEmploi.getCandidats().remove(this);
//    }

//    public void addNotification(Notification notification) {
//        notifications.add(notification);
//        notification.setCandidat(this);
//    }
//
//    public void removeNotification(Notification notification) {
//        notifications.remove(notification);
//        notification.setCandidat(null);
//    }

}
