package com.dev.MySpringProject.dto;

import com.dev.MySpringProject.entity.Candidat;
import com.dev.MySpringProject.entity.Recruteur;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO {
    private Long id;

    private String contenu;
    private String statut;
    private String heure;


    private CandidatDTO candidat;


    private RecruteurDTO recruteur;
}
