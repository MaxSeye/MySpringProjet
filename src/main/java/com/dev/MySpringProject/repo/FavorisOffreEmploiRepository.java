package com.dev.MySpringProject.repo;


import com.dev.MySpringProject.entity.FavorisOffreEmploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavorisOffreEmploiRepository extends JpaRepository<FavorisOffreEmploi, Long> {

    // Méthode pour trouver un favorisOffreEmploi par son ID
    // Cette méthode est déjà fournie par JpaRepository (findById)

    // Méthode pour vérifier si un favorisOffreEmploi existe par son ID
    // Cette méthode est déjà fournie par JpaRepository (existsById)

    // Méthode pour supprimer un favorisOffreEmploi par son ID
    // Cette méthode est déjà fournie par JpaRepository (deleteById)
}
