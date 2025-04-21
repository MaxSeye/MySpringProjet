package com.dev.MySpringProject.service.impl;

import com.dev.MySpringProject.dto.NotificationDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.entity.Candidat;
import com.dev.MySpringProject.entity.Notification;
import com.dev.MySpringProject.entity.Recruteur;
import com.dev.MySpringProject.repo.NotificationRepository;
import com.dev.MySpringProject.service.interfac.INotificationService;
import com.dev.MySpringProject.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService implements INotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Response createNotification(NotificationDTO notificationDTO) {
        Response response = new Response();
        try {
            Notification notification = new Notification();
            notification.setContenu(notificationDTO.getContenu());
            notification.setStatut("non_lu"); // Par défaut, la notification est non lue

            // Formatage de l'heure actuelle
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            notification.setHeure(LocalDateTime.now().format(formatter));

            if (notificationDTO.getCandidat() != null && notificationDTO.getCandidat().getId() != null) {
                Candidat candidat = new Candidat();
                candidat.setId(notificationDTO.getCandidat().getId());
                notification.setCandidat(candidat);
            }

            if (notificationDTO.getRecruteur() != null && notificationDTO.getRecruteur().getId() != null) {
                Recruteur recruteur = new Recruteur();
                recruteur.setId(notificationDTO.getRecruteur().getId());
                notification.setRecruteur(recruteur);
            }

            Notification savedNotification = notificationRepository.save(notification);
            response.setStatusCode(HttpStatus.CREATED.value());
            response.setMessage("Notification créée avec succès");
            response.setNotification(Utils.mapNotificationEntityToNotificationDTO(savedNotification));
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur lors de la création de la notification: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getNotificationById(Long id) {
        Response response = new Response();
        try {
            Optional<Notification> notificationOptional = notificationRepository.findById(id);
            if (notificationOptional.isPresent()) {
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Notification trouvée");
                response.setNotification(Utils.mapNotificationEntityToNotificationDTO(notificationOptional.get()));
            } else {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Notification non trouvée avec l'ID: " + id);
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur lors de la récupération de la notification: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateNotification(Long id, NotificationDTO notificationDTO) {
        Response response = new Response();
        try {
            Optional<Notification> notificationOptional = notificationRepository.findById(id);
            if (notificationOptional.isPresent()) {
                Notification notification = notificationOptional.get();
                notification.setContenu(notificationDTO.getContenu());
                notification.setStatut(notificationDTO.getStatut());

                Notification updatedNotification = notificationRepository.save(notification);
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Notification mise à jour avec succès");
                response.setNotification(Utils.mapNotificationEntityToNotificationDTO(updatedNotification));
            } else {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Notification non trouvée avec l'ID: " + id);
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur lors de la mise à jour de la notification: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteNotification(Long id) {
        Response response = new Response();
        try {
            Optional<Notification> notificationOptional = notificationRepository.findById(id);
            if (notificationOptional.isPresent()) {
                notificationRepository.deleteById(id);
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Notification supprimée avec succès");
            } else {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Notification non trouvée avec l'ID: " + id);
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur lors de la suppression de la notification: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getNotificationsByCandidat(Long candidatId) {
        Response response = new Response();
        try {
            List<Notification> notifications = notificationRepository.findByCandidatId(candidatId);
            if (!notifications.isEmpty()) {
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Notifications trouvées pour le candidat");
                response.setNotificationList(
                        notifications.stream()
                                .map(Utils::mapNotificationEntityToNotificationDTO)
                                .collect(Collectors.toList())
                );
            } else {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Aucune notification trouvée pour le candidat avec l'ID: " + candidatId);
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur lors de la récupération des notifications: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getNotificationsByRecruteur(Long recruteurId) {
        Response response = new Response();
        try {
            List<Notification> notifications = notificationRepository.findByRecruteurId(recruteurId);
            if (!notifications.isEmpty()) {
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Notifications trouvées pour le recruteur");
                response.setNotificationList(
                        notifications.stream()
                                .map(Utils::mapNotificationEntityToNotificationDTO)
                                .collect(Collectors.toList())
                );
            } else {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Aucune notification trouvée pour le recruteur avec l'ID: " + recruteurId);
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur lors de la récupération des notifications: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getNotificationsByStatut(String statut) {
        Response response = new Response();
        try {
            List<Notification> notifications = notificationRepository.findByStatut(statut);
            if (!notifications.isEmpty()) {
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Notifications trouvées avec le statut: " + statut);
                response.setNotificationList(
                        notifications.stream()
                                .map(Utils::mapNotificationEntityToNotificationDTO)
                                .collect(Collectors.toList())
                );
            } else {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Aucune notification trouvée avec le statut: " + statut);
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur lors de la récupération des notifications: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response searchNotificationsByContenu(String contenu) {
        Response response = new Response();
        try {
            List<Notification> notifications = notificationRepository.findByContenuContainingIgnoreCase(contenu);
            if (!notifications.isEmpty()) {
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Notifications trouvées contenant: " + contenu);
                response.setNotificationList(
                        notifications.stream()
                                .map(Utils::mapNotificationEntityToNotificationDTO)
                                .collect(Collectors.toList())
                );
            } else {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Aucune notification trouvée contenant: " + contenu);
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur lors de la recherche des notifications: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response markNotificationAsRead(Long id) {
        Response response = new Response();
        try {
            Optional<Notification> notificationOptional = notificationRepository.findById(id);
            if (notificationOptional.isPresent()) {
                Notification notification = notificationOptional.get();
                notification.setStatut("lu");
                notificationRepository.save(notification);
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Notification marquée comme lue");
                response.setNotification(Utils.mapNotificationEntityToNotificationDTO(notification));
            } else {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Notification non trouvée avec l'ID: " + id);
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur lors du marquage de la notification comme lue: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response markNotificationAsUnread(Long id) {
        Response response = new Response();
        try {
            Optional<Notification> notificationOptional = notificationRepository.findById(id);
            if (notificationOptional.isPresent()) {
                Notification notification = notificationOptional.get();
                notification.setStatut("non_lu");
                notificationRepository.save(notification);
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Notification marquée comme non lue");
                response.setNotification(Utils.mapNotificationEntityToNotificationDTO(notification));
            } else {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Notification non trouvée avec l'ID: " + id);
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur lors du marquage de la notification comme non lue: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUnreadNotificationsCount(Long userId, String userType) {
        Response response = new Response();
        try {
            long count;
            if ("candidat".equalsIgnoreCase(userType)) {
                count = notificationRepository.findByCandidatIdAndStatut(userId, "non_lu").size();
            } else if ("recruteur".equalsIgnoreCase(userType)) {
                count = notificationRepository.findByRecruteurIdAndStatut(userId, "non_lu").size();
            } else {
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Type d'utilisateur invalide. Doit être 'candidat' ou 'recruteur'");
                return response;
            }

            response.setStatusCode(HttpStatus.OK.value());
            response.setMessage("Nombre de notifications non lues récupéré avec succès");
            response.setMessage(String.valueOf(count)); // On utilise le champ message pour retourner le count
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur lors du comptage des notifications non lues: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getRecentNotifications(Long userId, String userType, int limit) {
        Response response = new Response();
        try {
            List<Notification> notifications;
            if ("candidat".equalsIgnoreCase(userType)) {
                notifications = notificationRepository.findByCandidatIdOrderByIdDesc(userId)
                        .stream().limit(limit).collect(Collectors.toList());
            } else if ("recruteur".equalsIgnoreCase(userType)) {
                notifications = notificationRepository.findByRecruteurIdOrderByIdDesc(userId)
                        .stream().limit(limit).collect(Collectors.toList());
            } else {
                response.setStatusCode(HttpStatus.BAD_REQUEST.value());
                response.setMessage("Type d'utilisateur invalide. Doit être 'candidat' ou 'recruteur'");
                return response;
            }

            if (!notifications.isEmpty()) {
                response.setStatusCode(HttpStatus.OK.value());
                response.setMessage("Notifications récentes récupérées avec succès");
                response.setNotificationList(
                        notifications.stream()
                                .map(Utils::mapNotificationEntityToNotificationDTO)
                                .collect(Collectors.toList())
                );
            } else {
                response.setStatusCode(HttpStatus.NOT_FOUND.value());
                response.setMessage("Aucune notification trouvée pour cet utilisateur");
            }
        } catch (Exception e) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Erreur lors de la récupération des notifications récentes: " + e.getMessage());
        }
        return response;
    }
}