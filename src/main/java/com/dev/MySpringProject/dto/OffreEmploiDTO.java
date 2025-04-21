package com.dev.MySpringProject.dto;

import com.dev.MySpringProject.entity.Administrateur;
import com.dev.MySpringProject.entity.Candidat;
import com.dev.MySpringProject.entity.Recruteur;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OffreEmploiDTO {
    private Long id;

    private String titre;
    private String description;
    private String salaire;
    private String localisation;
    private String contrat;
    private String autres;

    private List<CandidatDTO> candidats;


    private RecruteurDTO recruteur;


    private AdministrateurDTO administrateur;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSalaire() {
        return salaire;
    }

    public void setSalaire(String salaire) {
        this.salaire = salaire;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getContrat() {
        return contrat;
    }

    public void setContrat(String contrat) {
        this.contrat = contrat;
    }

    public String getAutres() {
        return autres;
    }

    public void setAutres(String autres) {
        this.autres = autres;
    }

    public List<CandidatDTO> getCandidats() {
        return candidats;
    }

    public void setCandidats(List<CandidatDTO> candidats) {
        this.candidats = candidats;
    }

    public RecruteurDTO getRecruteur() {
        return recruteur;
    }

    public void setRecruteur(RecruteurDTO recruteur) {
        this.recruteur = recruteur;
    }

    public AdministrateurDTO getAdministrateur() {
        return administrateur;
    }

    public void setAdministrateur(AdministrateurDTO administrateur) {
        this.administrateur = administrateur;
    }
}
