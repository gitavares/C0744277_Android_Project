package com.giselletavares.c0744277_finalproject.models;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Task.class, Setting.class}, version = 6)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDAO mTaskDAO();
    public abstract SettingDAO mSettingDAO();

}
