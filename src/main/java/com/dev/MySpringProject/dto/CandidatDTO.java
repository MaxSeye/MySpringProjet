package com.dev.MySpringProject.dto;

import com.dev.MySpringProject.entity.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidatDTO {

    private Long id;

    private String typeFormation;
    private String niveauEtudes;
    private String divers;
    private String langues;
    private String competences;


    private Recruteur recruteur;

    private List<Experience> experiences;

    private List<Formation> formations;

    private List<CandidatureEnCours> candidaturesEnCours;


    private List<OffreEmploiDTO> offresEmploi;


    private List<FavorisOffreEmploiDTO> favorisOffreEmploi;

    private List<NotificationDTO> notifications;
}
