package com.giselletavares.c0744277_finalproject.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface TaskDAO {

    @Insert
    public void addTask(Task task);


}
