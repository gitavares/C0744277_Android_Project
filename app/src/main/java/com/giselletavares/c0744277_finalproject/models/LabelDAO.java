package com.giselletavares.c0744277_finalproject.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

@Dao
public interface LabelDAO {

    @Insert
    void addLabel(Label label);

    @Query("SELECT _id, userId, labelName, labelChecked, labelColor " +
            "FROM labels " +
            "WHERE userId = :userId " +
            "ORDER BY labelName ASC")
    Cursor getLabels(String userId);

}
