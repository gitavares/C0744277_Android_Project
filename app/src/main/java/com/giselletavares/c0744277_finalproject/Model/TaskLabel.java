package com.giselletavares.c0744277_finalproject.Model;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "tasksLabels", primaryKeys = {"idTask", "idLabel"})
public class TaskLabel {

    @NonNull
    private String idTask;

    @NonNull
    private String idLabel;

    private Date createdDate;

    public String getIdTask() {
        return idTask;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public String getIdLabel() {
        return idLabel;
    }

    public void setIdLabel(String idLabel) {
        this.idLabel = idLabel;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
