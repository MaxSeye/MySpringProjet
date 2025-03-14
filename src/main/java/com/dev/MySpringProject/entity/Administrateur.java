package com.dev.MySpringProject.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "administrateur")
public class Administrateur extends Utilisateur{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "administrateur", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OffreEmploi> offresEmploi ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OffreEmploi> getOffresEmploi() {
        return offresEmploi;
    }

    public void setOffresEmploi(List<OffreEmploi> offresEmploi) {
        this.offresEmploi = offresEmploi;
    }
}
