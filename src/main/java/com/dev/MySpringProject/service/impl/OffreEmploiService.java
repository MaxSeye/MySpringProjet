package com.dev.MySpringProject.service.impl;

import com.dev.MySpringProject.dto.*;
import com.dev.MySpringProject.entity.*;
import com.dev.MySpringProject.repo.CandidatRepository;
import com.dev.MySpringProject.repo.OffreEmploiRepository;
import com.dev.MySpringProject.repo.RecruteurRepository;
import com.dev.MySpringProject.service.interfac.IOffreEmploiService;
import com.dev.MySpringProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OffreEmploiService implements IOffreEmploiService {

    @Autowired
    private OffreEmploiRepository offreEmploiRepository;

    @Autowired
    private CandidatRepository candidatRepository;

    @Autowired
    private RecruteurRepository recruteurRepository;

    // CRUD Operations
    @Override
    @Transactional
    public Response createOffreEmploi(OffreEmploiDTO offreDTO) {
        Response response = new Response();
        try {
            OffreEmploi offre = new OffreEmploi();
            mapOffreDTOToEntity(offreDTO, offre);

            // Set recruteur
            if (offreDTO.getRecruteur() != null && offreDTO.getRecruteur().getId() != null) {
                Recruteur recruteur = recruteurRepository.findById(offreDTO.getRecruteur().getId())
                        .orElseThrow(() -> new RuntimeException("Recruteur non trouvé"));
                offre.setRecruteur(recruteur);
            }

            OffreEmploi savedOffre = offreEmploiRepository.save(offre);
            response.setStatusCode(200);
            response.setMessage("Offre créée avec succès");
            response.setOffreEmploi(Utils.mapOffreEmploiEntityToOffreEmploiDTO(savedOffre));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la création de l'offre: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getOffreEmploiById(Long id) {
        Response response = new Response();
        try {
            OffreEmploi offre = offreEmploiRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'ID: " + id));

            OffreEmploiDTO offreDTO = Utils.mapOffreEmploiEntityToOffreEmploiDTO(offre);
            response.setStatusCode(200);
            response.setMessage("Offre récupérée avec succès");
            response.setOffreEmploi(offreDTO);
        } catch (Exception e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    public Response updateOffreEmploi(Long id, OffreEmploiDTO offreDTO) {
        Response response = new Response();
        try {
            OffreEmploi existingOffre = offreEmploiRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'ID: " + id));

            mapOffreDTOToEntity(offreDTO, existingOffre);

            // Update recruteur if changed
            if (offreDTO.getRecruteur() != null && offreDTO.getRecruteur().getId() != null
                    && !offreDTO.getRecruteur().getId().equals(existingOffre.getRecruteur().getId())) {
                Recruteur recruteur = recruteurRepository.findById(offreDTO.getRecruteur().getId())
                        .orElseThrow(() -> new RuntimeException("Recruteur non trouvé"));
                existingOffre.setRecruteur(recruteur);
            }

            OffreEmploi updatedOffre = offreEmploiRepository.save(existingOffre);
            response.setStatusCode(200);
            response.setMessage("Offre mise à jour avec succès");
            response.setOffreEmploi(Utils.mapOffreEmploiEntityToOffreEmploiDTO(updatedOffre));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la mise à jour: " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    public Response deleteOffreEmploi(Long id) {
        Response response = new Response();
        try {
            OffreEmploi offre = offreEmploiRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'ID: " + id));

            // Clear relationships
            for (Candidat candidat : offre.getCandidats()) {
                candidat.getOffresEmploi().remove(offre);
            }
            offre.getCandidats().clear();

            offreEmploiRepository.delete(offre);
            response.setStatusCode(200);
            response.setMessage("Offre supprimée avec succès");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la suppression: " + e.getMessage());
        }
        return response;
    }

    // Search Methods
    @Override
    public Response getAllOffresEmploi() {
        Response response = new Response();
        try {
            List<OffreEmploi> offres = offreEmploiRepository.findAll();
            List<OffreEmploiDTO> offreDTOs = offres.stream()
                    .map(Utils::mapOffreEmploiEntityToOffreEmploiDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Liste des offres récupérée avec succès");
            response.setOffreEmploiList(offreDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la récupération: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getOffresByTitre(String titre) {
        Response response = new Response();
        try {
            List<OffreEmploi> offres = offreEmploiRepository.findByTitreContainingIgnoreCase(titre);
            List<OffreEmploiDTO> offreDTOs = offres.stream()
                    .map(Utils::mapOffreEmploiEntityToOffreEmploiDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(offreDTOs.isEmpty() ? "Aucune offre trouvée" : "Offres trouvées par titre");
            response.setOffreEmploiList(offreDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la recherche: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getOffresByLocalisation(String localisation) {
        Response response = new Response();
        try {
            List<OffreEmploi> offres = offreEmploiRepository.findByLocalisation(localisation);
            List<OffreEmploiDTO> offreDTOs = offres.stream()
                    .map(Utils::mapOffreEmploiEntityToOffreEmploiDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(offreDTOs.isEmpty() ? "Aucune offre trouvée" : "Offres trouvées par localisation");
            response.setOffreEmploiList(offreDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la recherche: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getOffresByContrat(String contrat) {
        Response response = new Response();
        try {
            List<OffreEmploi> offres = offreEmploiRepository.findByContrat(contrat);
            List<OffreEmploiDTO> offreDTOs = offres.stream()
                    .map(Utils::mapOffreEmploiEntityToOffreEmploiDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(offreDTOs.isEmpty() ? "Aucune offre trouvée" : "Offres trouvées par type de contrat");
            response.setOffreEmploiList(offreDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la recherche: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getOffresByRecruteur(Long recruteurId) {
        Response response = new Response();
        try {
            List<OffreEmploi> offres = offreEmploiRepository.findByRecruteurId(recruteurId);
            List<OffreEmploiDTO> offreDTOs = offres.stream()
                    .map(Utils::mapOffreEmploiEntityToOffreEmploiDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(offreDTOs.isEmpty() ? "Aucune offre trouvée" : "Offres trouvées pour le recruteur");
            response.setOffreEmploiList(offreDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la recherche: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getOffresByAdministrateur(Long administrateurId) {
        Response response = new Response();
        try {
            List<OffreEmploi> offres = offreEmploiRepository.findByAdministrateurId(administrateurId);
            List<OffreEmploiDTO> offreDTOs = offres.stream()
                    .map(Utils::mapOffreEmploiEntityToOffreEmploiDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(offreDTOs.isEmpty() ? "Aucune offre trouvée" : "Offres trouvées pour l'administrateur");
            response.setOffreEmploiList(offreDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la recherche: " + e.getMessage());
        }
        return response;
    }

    // Candidate Management
    @Override
    @Transactional
    public Response addCandidatToOffre(Long offreId, Long candidatId) {
        Response response = new Response();
        try {
            OffreEmploi offre = offreEmploiRepository.findById(offreId)
                    .orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'ID: " + offreId));

            Candidat candidat = candidatRepository.findById(candidatId)
                    .orElseThrow(() -> new RuntimeException("Candidat non trouvé avec l'ID: " + candidatId));

            if (offre.getCandidats().contains(candidat)) {
                response.setStatusCode(400);
                response.setMessage("Ce candidat a déjà postulé à cette offre");
                return response;
            }

            offre.getCandidats().add(candidat);
            candidat.getOffresEmploi().add(offre);

            offreEmploiRepository.save(offre);
            candidatRepository.save(candidat);

            response.setStatusCode(200);
            response.setMessage("Candidat ajouté à l'offre avec succès");
            response.setOffreEmploi(Utils.mapOffreEmploiEntityToOffreEmploiDTO(offre));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de l'ajout du candidat: " + e.getMessage());
        }
        return response;
    }

    @Override
    @Transactional
    public Response removeCandidatFromOffre(Long offreId, Long candidatId) {
        Response response = new Response();
        try {
            OffreEmploi offre = offreEmploiRepository.findById(offreId)
                    .orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'ID: " + offreId));

            Candidat candidat = candidatRepository.findById(candidatId)
                    .orElseThrow(() -> new RuntimeException("Candidat non trouvé avec l'ID: " + candidatId));

            if (!offre.getCandidats().contains(candidat)) {
                response.setStatusCode(400);
                response.setMessage("Ce candidat n'a pas postulé à cette offre");
                return response;
            }

            offre.getCandidats().remove(candidat);
            candidat.getOffresEmploi().remove(offre);

            offreEmploiRepository.save(offre);
            candidatRepository.save(candidat);

            response.setStatusCode(200);
            response.setMessage("Candidat retiré de l'offre avec succès");
            response.setOffreEmploi(Utils.mapOffreEmploiEntityToOffreEmploiDTO(offre));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors du retrait du candidat: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getCandidatsByOffre(Long offreId) {
        Response response = new Response();
        try {
            OffreEmploi offre = offreEmploiRepository.findById(offreId)
                    .orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'ID: " + offreId));

            List<CandidatDTO> candidatDTOs = offre.getCandidats().stream()
                    .map(Utils::mapCandidatEntityToCandidatDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(candidatDTOs.isEmpty() ? "Aucun candidat pour cette offre" : "Candidats récupérés avec succès");
            response.setCandidatList(candidatDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la récupération des candidats: " + e.getMessage());
        }
        return response;
    }

    // Advanced Methods
    @Override
    public Response searchOffres(String titre, String localisation, String contrat) {
        Response response = new Response();
        try {
            List<OffreEmploi> offres;

            // Vérifier qu'au moins un critère est fourni
            if (titre == null && localisation == null && contrat == null) {
                response.setStatusCode(400);
                response.setMessage("Au moins un critère de recherche doit être fourni");
                return response;
            }

            // Recherche avec combinaison de critères
            if (titre != null && localisation != null && contrat != null) {
                offres = offreEmploiRepository.findByTitreContainingIgnoreCaseAndLocalisationAndContrat(
                        titre, localisation, contrat);
            } else if (titre != null && localisation != null) {
                offres = offreEmploiRepository.findByTitreContainingIgnoreCaseAndLocalisation(
                        titre, localisation);
            } else if (titre != null && contrat != null) {
                offres = offreEmploiRepository.findByTitreContainingIgnoreCaseAndContrat(
                        titre, contrat);
            } else if (localisation != null && contrat != null) {
                offres = offreEmploiRepository.findByLocalisationAndContrat(
                        localisation, contrat);
            } else if (titre != null) {
                offres = offreEmploiRepository.findByTitreContainingIgnoreCase(titre);
            } else if (localisation != null) {
                offres = offreEmploiRepository.findByLocalisation(localisation);
            } else {
                offres = offreEmploiRepository.findByContrat(contrat);
            }

            List<OffreEmploiDTO> offreDTOs = offres.stream()
                    .map(Utils::mapOffreEmploiEntityToOffreEmploiDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage(offreDTOs.isEmpty() ? "Aucune offre trouvée" : "Résultats de la recherche");
            response.setOffreEmploiList(offreDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la recherche: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRecentOffres(int limit) {
        Response response = new Response();
        try {
            Pageable pageable = PageRequest.of(0, limit, Sort.by("id").descending());
            Page<OffreEmploi> page = offreEmploiRepository.findAll(pageable);

            List<OffreEmploiDTO> offreDTOs = page.getContent().stream()
                    .map(Utils::mapOffreEmploiEntityToOffreEmploiDTO)
                    .collect(Collectors.toList());

            response.setStatusCode(200);
            response.setMessage("Offres récentes récupérées");
            response.setOffreEmploiList(offreDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors de la récupération: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response countOffresByRecruteur(Long recruteurId) {
        Response response = new Response();
        try {
            long count = offreEmploiRepository.countByRecruteurId(recruteurId);
            response.setStatusCode(200);
            response.setMessage("Nombre d'offres pour le recruteur");
            response.setMessage(String.valueOf(count));
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Erreur lors du comptage: " + e.getMessage());
        }
        return response;
    }

    // Helper method
    private void mapOffreDTOToEntity(OffreEmploiDTO dto, OffreEmploi entity) {
        entity.setTitre(dto.getTitre());
        entity.setDescription(dto.getDescription());
        entity.setSalaire(dto.getSalaire());
        entity.setLocalisation(dto.getLocalisation());
        entity.setContrat(dto.getContrat());
        entity.setAutres(dto.getAutres());
    }
}