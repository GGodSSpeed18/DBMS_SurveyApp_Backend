package com.example.survey.data_access_layers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.survey.data_transfer_objects.Notification;
import com.example.survey.mappers.NotificationRowMapper;
import com.example.survey.utilities.TableNames;

@Repository
public class NotificationRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Notification> getUserNotifs(long userid, Boolean read) {
        String sql;
        Object[] params;
        if(read!=null){
            sql = String.format("SELECT * FROM %s WHERE user_id = ? AND seen = ? ", TableNames.notifT);
            params = new Object[]{userid, read};
        }else{
            sql = String.format("SELECT * FROM %s WHERE user_id = ? ", TableNames.notifT);
            params = new Object[]{userid};
        }
        return jdbcTemplate.query(sql, new NotificationRowMapper(), params);
    }

    public void createFormNotifications(Notification input, List<Long> allusers) {
        String sql = String.format("INSERT INTO %s (form_id, user_id, notif_time, message, notif_type) VALUES (?, ?, ?, ?, ?)", TableNames.notifT);
        for(Long user: allusers){
            Object[] params = new Object[]{input.getForm_id(), user, input.getNotif_time(), input.getMessage(), input.getNotifTypeString()};
            jdbcTemplate.update(sql, params);
        }
    }

    public void sendNotification(Notification input, long userid) {
        String sql = String.format("INSERT INTO %s (form_id, user_id, notif_time, message, notif_type) VALUES (?, ?, ?, ?, ?)", TableNames.notifT);
        Object[] params = new Object[]{input.getForm_id(), userid, input.getNotif_time(), input.getMessage(), input.getNotifTypeString()};
        jdbcTemplate.update(sql, params);
    }

    public List<Notification> getFormNotifications(long formid, String notif_type) {
        String sql = String.format("SELECT * FROM %s WHERE form_id = ? AND notif_type = ?", TableNames.notifT);
        return jdbcTemplate.query(sql, new NotificationRowMapper(), new Object[]{formid, notif_type});
    }

    public List<Long> getUsersWithFormNotifications(long formid, String notif_type) {
        String sql = String.format("SELECT user_id FROM %s WHERE form_id = ? AND notif_type = ?", TableNames.notifT);
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("user_id"), new Object[]{formid, notif_type});  
    }

    public void markAsRead(long notif_id) {
        String sql = String.format("UPDATE %s SET seen=TRUE WHERE notif_id = ?", TableNames.notifT);
        jdbcTemplate.update(sql, new Object[]{notif_id});
    }

    public void markAllRead(long userid) {
        String sql = String.format("UPDATE %s SET seen=TRUE WHERE user_id = ?", TableNames.notifT);
        jdbcTemplate.update(sql, new Object[]{userid});
    }
}