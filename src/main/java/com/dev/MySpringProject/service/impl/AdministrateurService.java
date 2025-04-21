package com.dev.MySpringProject.service.impl;

import com.dev.MySpringProject.dto.AdministrateurDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.entity.Administrateur;
import com.dev.MySpringProject.repo.AdministrateurRepository;
import com.dev.MySpringProject.service.interfac.IAdministrateurService;
import com.dev.MySpringProject.utils.JWTUtils;
import com.dev.MySpringProject.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdministrateurService implements IAdministrateurService {

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public Response createAdministrateur(AdministrateurDTO administrateurDTO) {
        Response response = new Response();

        if (administrateurRepository.existsByEmail(administrateurDTO.getEmail())) {
            response.setStatusCode(HttpStatus.CONFLICT.value());
            response.setMessage("Email already exists");
            return response;
        }

        Administrateur administrateur = new Administrateur();
        administrateur.setEmail(administrateurDTO.getEmail());
        administrateur.setPassword(passwordEncoder.encode(administrateurDTO.getPassword()));
        administrateur.setPrenom(administrateurDTO.getPrenom());
        administrateur.setNom(administrateurDTO.getNom());
        administrateur.setNumeroDeTelephone(administrateurDTO.getNumeroDeTelephone());
        administrateur.setRole("ADMIN");

        Administrateur savedAdmin = administrateurRepository.save(administrateur);
        AdministrateurDTO savedAdminDTO = Utils.mapAdministrateurEntityToAdministrateurDTO(savedAdmin);

        response.setStatusCode(HttpStatus.CREATED.value());
        response.setMessage("Admin created successfully");
        response.setAdministrateur(savedAdminDTO);

        return response;
    }

    @Override
    public Response getAdministrateurById(Long id) {
        Response response = new Response();
        Optional<Administrateur> adminOptional = administrateurRepository.findById(id);

        if (adminOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Admin not found");
            return response;
        }

        AdministrateurDTO adminDTO = Utils.mapAdministrateurEntityToAdministrateurDTO(adminOptional.get());
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Admin retrieved successfully");
        response.setAdministrateur(adminDTO);

        return response;
    }

    @Override
    public Response getAllAdministrateurs() {
        Response response = new Response();
        List<Administrateur> admins = administrateurRepository.findAll();

        if (admins.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No admins found");
            return response;
        }

        List<AdministrateurDTO> adminDTOs = admins.stream()
                .map(Utils::mapAdministrateurEntityToAdministrateurDTO)
                .collect(Collectors.toList());

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Admins retrieved successfully");
        response.setAdministrateurList(adminDTOs);

        return response;
    }

    @Override
    public Response updateAdministrateur(Long id, AdministrateurDTO administrateurDTO) {
        Response response = new Response();
        Optional<Administrateur> adminOptional = administrateurRepository.findById(id);

        if (adminOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Admin not found");
            return response;
        }

        Administrateur existingAdmin = adminOptional.get();
        existingAdmin.setEmail(administrateurDTO.getEmail());
        existingAdmin.setPrenom(administrateurDTO.getPrenom());
        existingAdmin.setNom(administrateurDTO.getNom());
        existingAdmin.setNumeroDeTelephone(administrateurDTO.getNumeroDeTelephone());

        if (administrateurDTO.getPassword() != null && !administrateurDTO.getPassword().isEmpty()) {
            existingAdmin.setPassword(passwordEncoder.encode(administrateurDTO.getPassword()));
        }

        Administrateur updatedAdmin = administrateurRepository.save(existingAdmin);
        AdministrateurDTO updatedAdminDTO = Utils.mapAdministrateurEntityToAdministrateurDTO(updatedAdmin);

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Admin updated successfully");
        response.setAdministrateur(updatedAdminDTO);

        return response;
    }

    @Override
    public Response deleteAdministrateur(Long id) {
        Response response = new Response();

        if (!administrateurRepository.existsById(id)) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Admin not found");
            return response;
        }

        administrateurRepository.deleteById(id);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Admin deleted successfully");

        return response;
    }

    @Override
    public Response loginAdministrateur(String email, String password) {
        Response response = new Response();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtils.generateToken(userDetails);

            Optional<Administrateur> adminOptional = administrateurRepository.findByEmail(email);
            if (adminOptional.isEmpty()) {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Admin not found");
                return response;
            }

            Administrateur admin = adminOptional.get();
            AdministrateurDTO adminDTO = Utils.mapAdministrateurEntityToAdministrateurDTO(admin);

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Login successful");
            response.setToken(token);
            response.setRole(admin.getRole());
            response.setAdministrateur(adminDTO);

        } catch (Exception e) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            response.setMessage("Invalid email or password");
        }

        return response;
    }

    @Override
    public Response getAdministrateurByEmail(String email) {
        Response response = new Response();
        Optional<Administrateur> adminOptional = administrateurRepository.findByEmail(email);

        if (adminOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Admin not found");
            return response;
        }

        AdministrateurDTO adminDTO = Utils.mapAdministrateurEntityToAdministrateurDTO(adminOptional.get());
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Admin retrieved successfully");
        response.setAdministrateur(adminDTO);

        return response;
    }

    @Override
    public Response getAdministrateursWithOffresEmploi() {
        Response response = new Response();
        List<Administrateur> admins = administrateurRepository.findAll();

        if (admins.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("No admins found");
            return response;
        }

        List<AdministrateurDTO> adminDTOs = admins.stream()
                .map(admin -> {
                    AdministrateurDTO dto = Utils.mapAdministrateurEntityToAdministrateurDTO(admin);
                    return dto;
                })
                .collect(Collectors.toList());

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Admins with job offers retrieved successfully");
        response.setAdministrateurList(adminDTOs);

        return response;
    }

    @Override
    public Response getOffresEmploiByAdminId(Long adminId) {
        Response response = new Response();
        Optional<Administrateur> adminOptional = administrateurRepository.findById(adminId);

        if (adminOptional.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Admin not found");
            return response;
        }

        AdministrateurDTO adminDTO = Utils.mapAdministrateurEntityToAdministrateurDTO(adminOptional.get());

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Job offers by admin retrieved successfully");
        response.setAdministrateur(adminDTO);

        return response;
    }
}