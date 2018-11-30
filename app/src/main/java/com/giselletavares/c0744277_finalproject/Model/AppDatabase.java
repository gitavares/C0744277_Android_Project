package com.giselletavares.c0744277_finalproject.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Task.class, Label.class, Setting.class, TaskLabel.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDAO mTaskDAO();
    public abstract LabelDAO mLabelDAO();
    public abstract SettingDAO mSettingDAO();
    public abstract TaskLabelDAO mTaskLabelDAO();

}
