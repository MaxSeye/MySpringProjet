package com.dev.MySpringProject.service.interfac;


import com.dev.MySpringProject.dto.AdministrateurDTO;
import com.dev.MySpringProject.dto.Response;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface IAdministrateurService {
    // CRUD Standard
    Response createAdministrateur(AdministrateurDTO administrateurDTO);
    Response getAdministrateurById(Long id);
    Response getAllAdministrateurs();
    Response updateAdministrateur(Long id, AdministrateurDTO administrateurDTO);
    Response deleteAdministrateur(Long id);

    // Méthodes métiers
    Response loginAdministrateur(String email, String password);
    Response getAdministrateurByEmail(String email);
    Response getAdministrateursWithOffresEmploi();
    Response getOffresEmploiByAdminId(Long adminId);
}
