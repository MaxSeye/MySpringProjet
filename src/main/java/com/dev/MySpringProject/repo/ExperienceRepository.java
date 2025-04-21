package com.dev.MySpringProject.repo;


import com.dev.MySpringProject.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    // Méthode pour trouver des expériences par candidat
    List<Experience> findByCandidatId(Long candidatId);

    // Méthode pour trouver des expériences par poste
    List<Experience> findByPoste(String poste);

    // Méthode pour trouver des expériences par entreprise
    List<Experience> findByEntreprise(String entreprise);

    // Méthode pour trouver des expériences par poste et entreprise
    List<Experience> findByPosteAndEntreprise(String poste, String entreprise);

    // Méthode pour trouver des expériences par date de début
    List<Experience> findByDateDebut(String dateDebut);

    // Méthode pour trouver des expériences par date de fin
    List<Experience> findByDateFin(String dateFin);
}
