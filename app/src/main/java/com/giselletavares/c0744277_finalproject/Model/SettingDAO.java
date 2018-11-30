package com.giselletavares.c0744277_finalproject.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Update;

@Dao
public interface SettingDAO {

    @Update
    public void updateSetting(Setting setting);

}
