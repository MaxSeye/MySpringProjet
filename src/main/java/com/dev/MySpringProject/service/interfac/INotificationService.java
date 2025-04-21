package com.dev.MySpringProject.service.interfac;

import com.dev.MySpringProject.dto.NotificationDTO;
import com.dev.MySpringProject.dto.Response;

public interface INotificationService {

    // CRUD Standard
    Response createNotification(NotificationDTO notificationDTO);
    Response getNotificationById(Long id);
    Response updateNotification(Long id, NotificationDTO notificationDTO);
    Response deleteNotification(Long id);

    // Méthodes de recherche
    Response getNotificationsByCandidat(Long candidatId);
    Response getNotificationsByRecruteur(Long recruteurId);
    Response getNotificationsByStatut(String statut);
    Response searchNotificationsByContenu(String contenu);

    // Méthodes de gestion des statuts
    Response markNotificationAsRead(Long id);
    Response markNotificationAsUnread(Long id);

    // Méthodes avancées
    Response getUnreadNotificationsCount(Long userId, String userType); // 'candidat' ou 'recruteur'
    Response getRecentNotifications(Long userId, String userType, int limit);
}