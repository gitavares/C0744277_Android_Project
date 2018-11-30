package com.giselletavares.c0744277_finalproject.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "settings")
public class Setting {

    @PrimaryKey
    @NonNull
    private String userId;

    private Boolean notificationEmail;
    private Boolean notificationApp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getNotificationEmail() {
        return notificationEmail;
    }

    public void setNotificationEmail(Boolean notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public Boolean getNotificationApp() {
        return notificationApp;
    }

    public void setNotificationApp(Boolean notificationApp) {
        this.notificationApp = notificationApp;
    }
}
