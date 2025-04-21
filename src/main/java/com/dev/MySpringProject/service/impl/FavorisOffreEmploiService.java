package com.dev.MySpringProject.service.impl;

import com.dev.MySpringProject.dto.CandidatDTO;
import com.dev.MySpringProject.dto.FavorisOffreEmploiDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.entity.Candidat;
import com.dev.MySpringProject.entity.FavorisOffreEmploi;
import com.dev.MySpringProject.repo.CandidatRepository;
import com.dev.MySpringProject.repo.FavorisOffreEmploiRepository;
import com.dev.MySpringProject.service.interfac.IFavorisOffreEmploiService;
import com.dev.MySpringProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavorisOffreEmploiService implements IFavorisOffreEmploiService {

    @Autowired
    private FavorisOffreEmploiRepository favorisOffreEmploiRepository;

    @Autowired
    private CandidatRepository candidatRepository;

    @Override
    public Response createFavorisOffreEmploi(FavorisOffreEmploiDTO favorisDTO) {
        try {
            FavorisOffreEmploi favoris = new FavorisOffreEmploi();
            // Vous pouvez ajouter d'autres propriétés si nécessaire

            FavorisOffreEmploi savedFavoris = favorisOffreEmploiRepository.save(favoris);

            Response response = new Response();
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setMessage("Favoris créé avec succès");
            response.setFavorisOffreEmploi(Utils.mapFavorisOffreEmploiEntityToFavorisOffreEmploiDTO(savedFavoris));
            return response;
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response getFavorisOffreEmploiById(Long id) {
        try {
            Optional<FavorisOffreEmploi> favorisOptional = favorisOffreEmploiRepository.findById(id);
            if (favorisOptional.isPresent()) {
                Response response = new Response();
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Favoris trouvé");
                response.setFavorisOffreEmploi(Utils.mapFavorisOffreEmploiEntityToFavorisOffreEmploiDTO(favorisOptional.get()));
                return response;
            } else {
                return getNotFoundResponse();
            }
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response deleteFavorisOffreEmploi(Long id) {
        try {
            Optional<FavorisOffreEmploi> favorisOptional = favorisOffreEmploiRepository.findById(id);
            if (favorisOptional.isPresent()) {
                favorisOffreEmploiRepository.deleteById(id);
                Response response = new Response();
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Favoris supprimé avec succès");
                return response;
            } else {
                return getNotFoundResponse();
            }
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response addCandidatToFavoris(Long favorisId, Long candidatId) {
        try {
            Optional<FavorisOffreEmploi> favorisOptional = favorisOffreEmploiRepository.findById(favorisId);
            Optional<Candidat> candidatOptional = candidatRepository.findById(candidatId);

            if (favorisOptional.isPresent() && candidatOptional.isPresent()) {
                FavorisOffreEmploi favoris = favorisOptional.get();
                Candidat candidat = candidatOptional.get();

                if (!favoris.getCandidats().contains(candidat)) {
                    favoris.getCandidats().add(candidat);
                    candidat.getFavorisOffreEmploi().add(favoris);

                    favorisOffreEmploiRepository.save(favoris);
                    candidatRepository.save(candidat);

                    Response response = new Response();
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setMessage("Candidat ajouté aux favoris avec succès");
                    return response;
                } else {
                    Response response = new Response();
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setMessage("Ce candidat est déjà associé à ces favoris");
                    return response;
                }
            } else {
                return getNotFoundResponse();
            }
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response removeCandidatFromFavoris(Long favorisId, Long candidatId) {
        try {
            Optional<FavorisOffreEmploi> favorisOptional = favorisOffreEmploiRepository.findById(favorisId);
            Optional<Candidat> candidatOptional = candidatRepository.findById(candidatId);

            if (favorisOptional.isPresent() && candidatOptional.isPresent()) {
                FavorisOffreEmploi favoris = favorisOptional.get();
                Candidat candidat = candidatOptional.get();

                if (favoris.getCandidats().contains(candidat)) {
                    favoris.getCandidats().remove(candidat);
                    candidat.getFavorisOffreEmploi().remove(favoris);

                    favorisOffreEmploiRepository.save(favoris);
                    candidatRepository.save(candidat);

                    Response response = new Response();
                    response.setStatusCode(HttpStatus.OK.value());
                    response.setMessage("Candidat retiré des favoris avec succès");
                    return response;
                } else {
                    Response response = new Response();
                    response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                    response.setMessage("Ce candidat n'est pas associé à ces favoris");
                    return response;
                }
            } else {
                return getNotFoundResponse();
            }
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response getCandidatsByFavoris(Long favorisId) {
        try {
            Optional<FavorisOffreEmploi> favorisOptional = favorisOffreEmploiRepository.findById(favorisId);
            if (favorisOptional.isPresent()) {
                List<CandidatDTO> candidatsDTO = favorisOptional.get().getCandidats().stream()
                        .map(Utils::mapCandidatEntityToCandidatDTO)
                        .collect(Collectors.toList());

                Response response = new Response();
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Liste des candidats associés à ces favoris");
                response.setCandidatList(candidatsDTO);
                return response;
            } else {
                return getNotFoundResponse();
            }
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response getFavorisByCandidat(Long candidatId) {
        try {
            Optional<Candidat> candidatOptional = candidatRepository.findById(candidatId);
            if (candidatOptional.isPresent()) {
                List<FavorisOffreEmploiDTO> favorisDTO = candidatOptional.get().getFavorisOffreEmploi().stream()
                        .map(Utils::mapFavorisOffreEmploiEntityToFavorisOffreEmploiDTO)
                        .collect(Collectors.toList());

                Response response = new Response();
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Liste des favoris du candidat");
                response.setFavorisOffreEmploiList(favorisDTO);
                return response;
            } else {
                return getNotFoundResponse();
            }
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    @Override
    public Response checkIfFavorisExistsForCandidat(Long favorisId, Long candidatId) {
        try {
            Optional<FavorisOffreEmploi> favorisOptional = favorisOffreEmploiRepository.findById(favorisId);
            Optional<Candidat> candidatOptional = candidatRepository.findById(candidatId);

            if (favorisOptional.isPresent() && candidatOptional.isPresent()) {
                boolean exists = favorisOptional.get().getCandidats().contains(candidatOptional.get());

                Response response = new Response();
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage(exists ? "Le favoris existe pour ce candidat" : "Le favoris n'existe pas pour ce candidat");
                return response;
            } else {
                return getNotFoundResponse();
            }
        } catch (Exception e) {
            return getErrorResponse(e);
        }
    }

    private Response getErrorResponse(Exception e) {
        Response response = new Response();
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("Erreur lors du traitement: " + e.getMessage());
        return response;
    }

    private Response getNotFoundResponse() {
        Response response = new Response();
        response.setStatusCode(HttpStatus.NOT_FOUND.value());
        response.setMessage("Élément non trouvé");
        return response;
    }
}