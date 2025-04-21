package com.dev.MySpringProject.repo;


import com.dev.MySpringProject.entity.Candidat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long> {

    // Trouver un candidat par email (hérité de Utilisateur)
    Optional<Candidat> findByEmail(String email);

    // Vérifier si l'email existe déjà (pour l'inscription)
    boolean existsByEmail(String email);

    // Trouver des candidats par pays
    List<Candidat> findByPays(String pays);

    // Trouver des candidats par ville
    List<Candidat> findByVille(String ville);

    // Trouver des candidats par domaine d'étude
    List<Candidat> findByDomaineDetude(String domaineDetude);

    // Trouver des candidats par niveau d'étude
    List<Candidat> findByNiveauDetude(String niveauDetude);

    // Trouver des candidats par type de formation
    List<Candidat> findByTypeDeFormation(String typeDeFormation);
}
