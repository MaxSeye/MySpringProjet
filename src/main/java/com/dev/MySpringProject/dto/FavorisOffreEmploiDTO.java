package com.dev.MySpringProject.dto;

import com.dev.MySpringProject.entity.Candidat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavorisOffreEmploiDTO {
    private Long id;

    private List<CandidatDTO> candidats;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CandidatDTO> getCandidats() {
        return candidats;
    }

    public void setCandidats(List<CandidatDTO> candidats) {
        this.candidats = candidats;
    }
}
