package com.dev.MySpringProject.dto;


import com.dev.MySpringProject.entity.OffreEmploi;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdministrateurDTO {

    private Long id;

    private List<OffreEmploiDTO> offresEmploi ;


}
