package com.dev.MySpringProject.controller;

import com.dev.MySpringProject.dto.NotificationDTO;
import com.dev.MySpringProject.dto.Response;
import com.dev.MySpringProject.service.interfac.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    // Admin peut créer des notifications pour tous
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Response createNotification(@RequestBody NotificationDTO notificationDTO) {
        return notificationService.createNotification(notificationDTO);
    }

    // Tous les utilisateurs peuvent voir leurs notifications
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT', 'RECRUTEUR')")
    public Response getNotificationById(@PathVariable Long id) {
        return notificationService.getNotificationById(id);
    }

    // Admin peut modifier toutes les notifications
    // Les autres utilisateurs ne peuvent modifier que leurs propres notifications
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT', 'RECRUTEUR')")
    public Response updateNotification(@PathVariable Long id, @RequestBody NotificationDTO notificationDTO) {
        return notificationService.updateNotification(id, notificationDTO);
    }

    // Admin peut supprimer toutes les notifications
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response deleteNotification(@PathVariable Long id) {
        return notificationService.deleteNotification(id);
    }

    // Seuls les candidats et admin peuvent voir les notifications d'un candidat
    @GetMapping("/candidat/{candidatId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT')")
    public Response getNotificationsByCandidat(@PathVariable Long candidatId) {
        return notificationService.getNotificationsByCandidat(candidatId);
    }

    // Seuls les recruteurs et admin peuvent voir les notifications d'un recruteur
    @GetMapping("/recruteur/{recruteurId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECRUTEUR')")
    public Response getNotificationsByRecruteur(@PathVariable Long recruteurId) {
        return notificationService.getNotificationsByRecruteur(recruteurId);
    }

    // Seul l'admin peut filtrer par statut globalement
    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response getNotificationsByStatut(@PathVariable String statut) {
        return notificationService.getNotificationsByStatut(statut);
    }

    // Seul l'admin peut faire des recherches globales
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public Response searchNotificationsByContenu(@RequestParam String contenu) {
        return notificationService.searchNotificationsByContenu(contenu);
    }

    // Chaque utilisateur peut marquer ses propres notifications comme lues
    @PutMapping("/{id}/read")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT', 'RECRUTEUR')")
    public Response markNotificationAsRead(@PathVariable Long id) {
        return notificationService.markNotificationAsRead(id);
    }

    // Chaque utilisateur peut marquer ses propres notifications comme non lues
    @PutMapping("/{id}/unread")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT', 'RECRUTEUR')")
    public Response markNotificationAsUnread(@PathVariable Long id) {
        return notificationService.markNotificationAsUnread(id);
    }

    // Chaque utilisateur peut voir son nombre de notifications non lues
    @GetMapping("/unread-count")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT', 'RECRUTEUR')")
    public Response getUnreadNotificationsCount(
            @RequestParam Long userId,
            @RequestParam String userType) {
        return notificationService.getUnreadNotificationsCount(userId, userType);
    }

    // Chaque utilisateur peut voir ses notifications récentes
    @GetMapping("/recent")
    @PreAuthorize("hasAnyRole('ADMIN', 'CANDIDAT', 'RECRUTEUR')")
    public Response getRecentNotifications(
            @RequestParam Long userId,
            @RequestParam String userType,
            @RequestParam(defaultValue = "5") int limit) {
        return notificationService.getRecentNotifications(userId, userType, limit);
    }
}