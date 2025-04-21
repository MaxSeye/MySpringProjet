package com.dev.MySpringProject.repo;


import com.dev.MySpringProject.entity.OffreEmploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreEmploiRepository extends JpaRepository<OffreEmploi, Long> {

    // Méthode pour trouver des offres d'emploi par titre
    List<OffreEmploi> findByTitreContainingIgnoreCase(String titre);

    // Méthode pour trouver des offres d'emploi par localisation
    List<OffreEmploi> findByLocalisation(String localisation);

    // Méthode pour trouver des offres d'emploi par type de contrat
    List<OffreEmploi> findByContrat(String contrat);

    // Méthode pour trouver des offres d'emploi par recruteur
    List<OffreEmploi> findByRecruteurId(Long recruteurId);

    // Méthode pour trouver des offres d'emploi par administrateur
    List<OffreEmploi> findByAdministrateurId(Long administrateurId);

    // Méthodes de recherche combinées
    List<OffreEmploi> findByTitreContainingIgnoreCaseAndLocalisation(String titre, String localisation);
    List<OffreEmploi> findByTitreContainingIgnoreCaseAndContrat(String titre, String contrat);
    List<OffreEmploi> findByLocalisationAndContrat(String localisation, String contrat);
    List<OffreEmploi> findByTitreContainingIgnoreCaseAndLocalisationAndContrat(String titre, String localisation, String contrat);

    long countByRecruteurId(Long recruteurId);

}
