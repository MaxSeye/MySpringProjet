package com.dev.MySpringProject.controller;

import com.dev.MySpringProject.dto.OffreEmploiDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.service.interfac.IOffreEmploiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/offres")
public class OffreEmploiController {

    @Autowired
    private IOffreEmploiService offreEmploiService;

    // CRUD Operations
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public Response createOffreEmploi(@RequestBody OffreEmploiDTO offreDTO) {
        return offreEmploiService.createOffreEmploi(offreDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public Response getOffreEmploiById(@PathVariable Long id) {
        return offreEmploiService.getOffreEmploiById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public Response updateOffreEmploi(@PathVariable Long id, @RequestBody OffreEmploiDTO offreDTO) {
        return offreEmploiService.updateOffreEmploi(id, offreDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public Response deleteOffreEmploi(@PathVariable Long id) {
        return offreEmploiService.deleteOffreEmploi(id);
    }

    // Search Methods
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public Response getAllOffresEmploi() {
        return offreEmploiService.getAllOffresEmploi();
    }

    @GetMapping("/titre/{titre}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public Response getOffresByTitre(@PathVariable String titre) {
        return offreEmploiService.getOffresByTitre(titre);
    }

    @GetMapping("/localisation/{localisation}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public Response getOffresByLocalisation(@PathVariable String localisation) {
        return offreEmploiService.getOffresByLocalisation(localisation);
    }

    @GetMapping("/contrat/{contrat}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public Response getOffresByContrat(@PathVariable String contrat) {
        return offreEmploiService.getOffresByContrat(contrat);
    }

    @GetMapping("/recruteur/{recruteurId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public Response getOffresByRecruteur(@PathVariable Long recruteurId) {
        return offreEmploiService.getOffresByRecruteur(recruteurId);
    }

    @GetMapping("/administrateur/{administrateurId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response getOffresByAdministrateur(@PathVariable Long administrateurId) {
        return offreEmploiService.getOffresByAdministrateur(administrateurId);
    }

    // Candidate Management
    @PostMapping("/{offreId}/candidats/{candidatId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public Response addCandidatToOffre(@PathVariable Long offreId, @PathVariable Long candidatId) {
        return offreEmploiService.addCandidatToOffre(offreId, candidatId);
    }

    @DeleteMapping("/{offreId}/candidats/{candidatId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public Response removeCandidatFromOffre(@PathVariable Long offreId, @PathVariable Long candidatId) {
        return offreEmploiService.removeCandidatFromOffre(offreId, candidatId);
    }

    @GetMapping("/{offreId}/candidats")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public Response getCandidatsByOffre(@PathVariable Long offreId) {
        return offreEmploiService.getCandidatsByOffre(offreId);
    }

    // Advanced Methods
    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public Response searchOffres(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String localisation,
            @RequestParam(required = false) String contrat) {
        return offreEmploiService.searchOffres(titre, localisation, contrat);
    }

    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR', 'CANDIDAT')")
    public Response getRecentOffres(@RequestParam(defaultValue = "5") int limit) {
        return offreEmploiService.getRecentOffres(limit);
    }

    @GetMapping("/count/recruteur/{recruteurId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public Response countOffresByRecruteur(@PathVariable Long recruteurId) {
        return offreEmploiService.countOffresByRecruteur(recruteurId);
    }
}