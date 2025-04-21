package com.dev.MySpringProject.repo;


import com.dev.MySpringProject.entity.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {

    // Méthode pour trouver un administrateur par son email (hérité de Utilisateur)
    Optional<Administrateur> findByEmail(String email);

    // Méthode pour vérifier si un administrateur existe avec un email donné (hérité de Utilisateur)
    boolean existsByEmail(String email);
}