package com.giselletavares.c0744277_finalproject.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void addTask(Task task);

    @Query("SELECT * FROM tasks")
    List<Task> getTasks();

    @Query("SELECT * " +
            "FROM tasks " +
            "WHERE userId = :userId " +
            "AND status = :isDone " +
            "ORDER BY dueDate ASC, priority DESC")
    List<Task> getTasksInbox(String userId, Boolean isDone);

    @Query("SELECT * " +
            "FROM tasks " +
            "WHERE userId = :userId " +
            "AND status = :isDone " +
            "AND dueDate = :today " +
            "ORDER BY priority DESC")
    List<Task> getTasksToday(String userId, Boolean isDone, Date today);

    @Query("SELECT * " +
            "FROM tasks " +
            "WHERE userId = :userId " +
            "AND status = :isDone " +
            "AND dueDate > :today " +
            "ORDER BY dueDate ASC, priority DESC")
    List<Task> getTasksNextDays(String userId, Boolean isDone, Date today);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTask(Task task);

}
