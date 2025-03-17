package com.dev.MySpringProject.dto;

import com.dev.MySpringProject.entity.Candidat;
import com.dev.MySpringProject.entity.Notification;
import com.dev.MySpringProject.entity.OffreEmploi;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecruteurDTO {
    private Long id;

    private String formeJuridique;
    private String domaineActivite;

    private List<CandidatDTO> candidats;

    private List<NotificationDTO> notifications;

    private List<OffreEmploiDTO> offresEmploi;
}
