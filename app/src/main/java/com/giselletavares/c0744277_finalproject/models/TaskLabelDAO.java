package com.giselletavares.c0744277_finalproject.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface TaskLabelDAO {

    @Insert
    public void addTaskLabel(TaskLabel taskLabel);

}
