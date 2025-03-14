package com.dev.MySpringProject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "candidatureEnCours")
public class CandidatureEnCours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String enCours;
    private String statutDiscussion;
    private String statutSelection;

    @ManyToOne
    @JoinColumn(name = "candidat_id", nullable = false)
    private Candidat candidat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnCours() {
        return enCours;
    }

    public void setEnCours(String enCours) {
        this.enCours = enCours;
    }

    public String getStatutDiscussion() {
        return statutDiscussion;
    }

    public void setStatutDiscussion(String statutDiscussion) {
        this.statutDiscussion = statutDiscussion;
    }

    public String getStatutSelection() {
        return statutSelection;
    }

    public void setStatutSelection(String statutSelection) {
        this.statutSelection = statutSelection;
    }

    public Candidat getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }
}
