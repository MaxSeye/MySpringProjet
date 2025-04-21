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

    public CandidatDTO getCandidat() {
        return candidat;
    }

    public void setCandidat(CandidatDTO candidat) {
        this.candidat = candidat;
    }
}
