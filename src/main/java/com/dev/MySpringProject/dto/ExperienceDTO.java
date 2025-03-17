package com.dev.MySpringProject.dto;

import com.dev.MySpringProject.entity.Candidat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperienceDTO {
    private Long id;

    private String poste;
    private String dateDebut;
    private String dateFin;
    private String description;
    private String entreprise;

    private CandidatDTO candidat;

}
