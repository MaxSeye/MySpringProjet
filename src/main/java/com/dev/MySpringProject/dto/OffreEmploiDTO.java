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
}
