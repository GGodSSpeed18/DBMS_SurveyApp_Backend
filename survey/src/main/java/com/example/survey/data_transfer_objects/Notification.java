package com.example.survey.data_transfer_objects;

import java.util.Date;

public class Notification {
    private long notif_id;
    private long form_id;
    private Date notif_time;
    private String message;
    private boolean seen;

    public static final int NOTIF_TYPE_CREATE = 1;
    public static final int NOTIF_TYPE_OPEN = 2;
    public static final int NOTIF_TYPE_CLOSED = 3;

    // Author notifs
    public static final int NOTIF_TYPE_CLOSED_AUTHOR = 4;
    public static final int NOTIF_TYPE_ACTIVE_AUTHOR = 5;

    private int notifType;

    public Notification() {
    }

    public Notification(long form_id, Date notif_time, String message) {
        this.form_id = form_id;
        this.notif_time = notif_time;
        this.message = message;
    }

    public Notification(long form_id, Date notif_time, String message, String notif_type) {
        this.form_id = form_id;
        this.notif_time = notif_time;
        this.message = message;
        setNotifTypeFromString(notif_type);
    }

    public long getNotif_id() {
        return notif_id;
    }

    public void setNotif_id(long notif_id) {
        this.notif_id = notif_id;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getForm_id() {
        return form_id;
    }

    public void setForm_id(long form_id) {
        this.form_id = form_id;
    }

    public Date getNotif_time() {
        return notif_time;
    }

    public void setNotif_time(Date notif_time) {
        this.notif_time = notif_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getNotifType() {
        return notifType;
    }

    public void setNotifType(int notifType) {
        this.notifType = notifType;
    }

    public String getNotifTypeString() {
        return switch (notifType) {
            case NOTIF_TYPE_CREATE -> "C";
            case NOTIF_TYPE_OPEN -> "A";
            case NOTIF_TYPE_CLOSED -> "E";
            case NOTIF_TYPE_CLOSED_AUTHOR -> "G";
            case NOTIF_TYPE_ACTIVE_AUTHOR -> "R";
            default -> "C";
        };
    }

    public void setNotifTypeFromString(String notifTypeString) {
        this.notifType = switch (notifTypeString) {
            case "C" -> NOTIF_TYPE_CREATE;
            case "A" -> NOTIF_TYPE_OPEN;
            case "E" -> NOTIF_TYPE_CLOSED;
            case "G" -> NOTIF_TYPE_CLOSED_AUTHOR;
            case "R" -> NOTIF_TYPE_ACTIVE_AUTHOR;
            default -> NOTIF_TYPE_CREATE;
        };
    }
}
