package com.dev.MySpringProject.service.interfac;

import com.dev.MySpringProject.dto.FavorisOffreEmploiDTO;
import com.dev.MySpringProject.dto.Response;

public interface IFavorisOffreEmploiService {

    // CRUD Standard
    Response createFavorisOffreEmploi(FavorisOffreEmploiDTO favorisDTO);
    Response getFavorisOffreEmploiById(Long id);
    Response deleteFavorisOffreEmploi(Long id);

    // Gestion des relations avec Candidat
    Response addCandidatToFavoris(Long favorisId, Long candidatId);
    Response removeCandidatFromFavoris(Long favorisId, Long candidatId);
    Response getCandidatsByFavoris(Long favorisId);

    // Méthodes spécifiques
    Response getFavorisByCandidat(Long candidatId);
    Response checkIfFavorisExistsForCandidat(Long favorisId, Long candidatId);
}