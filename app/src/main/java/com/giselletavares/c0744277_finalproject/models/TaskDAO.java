package com.giselletavares.c0744277_finalproject.models;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    public void addTask(Task task);

    @Query("SELECT * FROM tasks")
    public List<Task> getTasks();

    @Query("SELECT * " +
            "FROM tasks " +
            "WHERE userId = :userId " +
            "AND status = :isDone " +
            "ORDER BY dueDate ASC, priority DESC")
    public List<Task> getTasksInbox(String userId, Boolean isDone);

    @Query("SELECT * " +
            "FROM tasks " +
            "WHERE userId = :userId " +
            "AND status = :isDone " +
            "AND dueDate = :today " +
            "ORDER BY priority DESC")
    public List<Task> getTasksToday(String userId, Boolean isDone, Date today);

    @Query("SELECT * " +
            "FROM tasks " +
            "WHERE userId = :userId " +
            "AND status = :isDone " +
            "AND dueDate > :today " +
            "ORDER BY dueDate ASC, priority DESC")
    public List<Task> getTasksNextDays(String userId, Boolean isDone, Date today);



}
