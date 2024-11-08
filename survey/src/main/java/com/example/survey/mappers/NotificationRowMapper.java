package com.example.survey.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.survey.data_transfer_objects.Notification;

public class NotificationRowMapper implements RowMapper<Notification> {
    @Override
    public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
        Notification notification = new Notification();
        notification.setNotif_id(rs.getLong("notif_id"));
        notification.setForm_id(rs.getLong("form_id"));
        notification.setNotif_time(rs.getTimestamp("notif_time")); 
        notification.setMessage(rs.getString("message"));
        notification.setSeen(rs.getBoolean("seen"));
        
        // Map the new notifType field
        notification.setNotifTypeFromString(rs.getString("notif_type"));  // Set it as an int
        return notification;
    }
}