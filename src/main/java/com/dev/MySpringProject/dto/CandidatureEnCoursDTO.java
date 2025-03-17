package com.dev.MySpringProject.dto;

import com.dev.MySpringProject.entity.Candidat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidatureEnCoursDTO {

    private Long id;

    private String enCours;
    private String statutDiscussion;
    private String statutSelection;

    private CandidatDTO candidat;

}
