package com.dev.MySpringProject.repo;


import com.dev.MySpringProject.entity.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormationRepository extends JpaRepository<Formation, Long> {

    // Méthode pour trouver des formations par candidat
    List<Formation> findByCandidatId(Long candidatId);

    // Méthode pour trouver des formations par école
    List<Formation> findByEcole(String ecole);

    // Méthode pour trouver des formations par diplôme
    List<Formation> findByDiplome(String diplome);

    // Méthode pour trouver des formations par année scolaire
    List<Formation> findByAnneeScolaire(String anneeScolaire);

    // Méthode pour trouver des formations par école et diplôme
    List<Formation> findByEcoleAndDiplome(String ecole, String diplome);
}