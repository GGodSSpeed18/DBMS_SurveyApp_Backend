package com.example.survey.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.NotificationRepository;
import com.example.survey.data_transfer_objects.Notification;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getUserNotifs(long userid, Boolean read) {
        return notificationRepository.getUserNotifs(userid, read);
    }

    public void createFormNotifications(Notification input, List<Long> allusers) {
        notificationRepository.createFormNotifications(input, allusers);
    }

    public List<Notification> getFormNotifications(long formid, String notif_type) {
        return notificationRepository.getFormNotifications(formid, notif_type);
    }

    public List<Long> getUsersWithFormNotifications(long formid, String notif_type) {
        return notificationRepository.getUsersWithFormNotifications(formid, notif_type);
    }

    public void sendNotification(Notification input, long uesrid) {
        notificationRepository.sendNotification(input, uesrid);
    }

    public void markAsRead(long notif_id) {
        notificationRepository.markAsRead(notif_id);
    }
}
