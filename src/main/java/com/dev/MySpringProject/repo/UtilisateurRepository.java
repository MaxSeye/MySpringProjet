package com.dev.MySpringProject.repo;


import com.dev.MySpringProject.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    // Méthode pour trouver un utilisateur par son email
    Optional<Utilisateur> findByEmail(String email);

    // Méthode pour vérifier si un utilisateur existe avec un email donné
    boolean existsByEmail(String email);

    // Trouver des utilisateurs par rôle
    List<Utilisateur> findByRole(String role);
}
