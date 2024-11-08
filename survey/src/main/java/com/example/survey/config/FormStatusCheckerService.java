package com.example.survey.config;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.example.survey.data_access_layers.FormRepository;
import com.example.survey.data_access_layers.UserRepository;
import com.example.survey.data_transfer_objects.AbstractUserObj;
import com.example.survey.data_transfer_objects.Notification;
import com.example.survey.entities.Form;
import com.example.survey.entities.User;
import com.example.survey.services.NotificationService;

@Service
public class FormStatusCheckerService {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private UserRepository userRepository;

    @Value("${time_check_form_statuses}")
    private long timeCheckFormStatuses; // The delay in milliseconds

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        startScheduler();
    }

    public void startScheduler() {
        Duration delay = Duration.ofMillis(timeCheckFormStatuses);
        taskScheduler.scheduleWithFixedDelay(this::checkFormStatuses, delay);
    }

    public void checkFormStatuses() {
        List<Form> forms = formRepository.getAllForms();
        Date currentDate = new Date();

        for (Form form : forms) {
            List<AbstractUserObj> form_open_to = formRepository.openToUsersAbstract(form.getForm_id(), null);
            
            // Filter out users who already have a notification
            if(currentDate.before(form.getStart_time())){
                //? phase 1
                List<Long> users_with_notifs =  notificationService.getUsersWithFormNotifications(form.getForm_id(), "C");
                List<Long> users_to_notify = form_open_to.stream()
                .map(AbstractUserObj::getUser_id)
                .filter(userId -> (userId!=form.getAuthor())&&(!users_with_notifs.contains(userId)))
                .collect(Collectors.toList());
                User current = userRepository.findById(form.getAuthor());
                String name = current.getFirst_name()+(current.getLast_name()!=null ? (" "+current.getLast_name()): "");
                String message = String.format("Author: %s has created Form %s", name, form.getForm_name());
                Notification notif = new Notification(form.getForm_id(), new Date(), message);
                notif.setNotifTypeFromString("C");
                notificationService.createFormNotifications(notif, users_to_notify);
            }
            if (form.isActive()) {
                //? phase 2
                List<Long> users_with_notifs =  notificationService.getUsersWithFormNotifications(form.getForm_id(), "A");
                List<Long> users_to_notify = form_open_to.stream()
                .map(AbstractUserObj::getUser_id)
                .filter(userId -> (userId!=form.getAuthor())&&(!users_with_notifs.contains(userId)))
                .collect(Collectors.toList());
                String message = String.format("Form %s is now accepting responses", form.getForm_name());
                Notification notif = new Notification(form.getForm_id(), new Date(), message);
                notif.setNotifTypeFromString("A");
                notificationService.createFormNotifications(notif, users_to_notify);

                // notify author when the form starts
                if(!notificationService.getUsersWithFormNotifications(form.getForm_id(), "R").contains(form.getAuthor())){
                    message = String.format("Your Form %s is active !!", form.getForm_name());
                    notif.setNotifTypeFromString("R");
                    notif.setMessage(message);
                    notificationService.sendNotification(notif, form.getAuthor());
                }
                
            } else if (form.getEnd_time() != null && currentDate.after(form.getEnd_time())) {
                //? phase 3
                List<Long> users_with_notifs =  notificationService.getUsersWithFormNotifications(form.getForm_id(), "E");
                List<Long> users_to_notify = form_open_to.stream()
                .map(AbstractUserObj::getUser_id)
                .filter(userId -> (userId!=form.getAuthor())&&(!users_with_notifs.contains(userId)))
                .collect(Collectors.toList());
                String message = String.format("Form %s has closed", form.getForm_name());
                Notification notif = new Notification(form.getForm_id(), new Date(), message);
                notif.setNotifTypeFromString("E");
                notificationService.createFormNotifications(notif, users_to_notify);


                // notify author when the form closes
                if(!notificationService.getUsersWithFormNotifications(form.getForm_id(), "G").contains(form.getAuthor())){
                    message = String.format("Your Form %s has closed! Generate analytics !", form.getForm_name());
                    notif.setNotifTypeFromString("G");
                    notif.setMessage(message);
                    notificationService.sendNotification(notif, form.getAuthor());
                }
            }
        }
    }
}
