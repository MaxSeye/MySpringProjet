package com.dev.MySpringProject.dto;


import com.dev.MySpringProject.entity.OffreEmploi;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdministrateurDTO extends UtilisateurDTO{

    private Long id;

    private List<OffreEmploiDTO> offresEmploi ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OffreEmploiDTO> getOffresEmploi() {
        return offresEmploi;
    }

    public void setOffresEmploi(List<OffreEmploiDTO> offresEmploi) {
        this.offresEmploi = offresEmploi;
    }
}
