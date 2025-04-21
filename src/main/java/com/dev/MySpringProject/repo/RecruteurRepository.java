package com.dev.MySpringProject.repo;

import com.dev.MySpringProject.entity.Recruteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruteurRepository extends JpaRepository<Recruteur, Long> {

    // Trouver un recruteur par l'ID de l'utilisateur (hérité de Utilisateur)
    Optional<Recruteur> findById(Long id);

    // Trouver des recruteurs par nom de société
    List<Recruteur> findByNomDeLaSociete(String nomDeLaSociete);

    // Trouver des recruteurs par pays
    List<Recruteur> findByPays(String pays);

    // Trouver des recruteurs par ville
    List<Recruteur> findByVille(String ville);

    // Trouver des recruteurs par domaine d'activité
    List<Recruteur> findByDomainActivite(String domainActivite);

    // Vérifier si un recruteur existe par nom de société
    boolean existsByNomDeLaSociete(String nomDeLaSociete);

    // Ajoutez ces nouvelles méthodes
    Optional<Recruteur> findByEmail(String email);
    boolean existsByEmail(String email);
}