package com.example.taskmanager.db.task;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;


@Dao
public interface TaskDAO { // DAO (Data access object)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insert(Task task);

    @Update
    void update(Task task);

    @Transaction
    @Query("UPDATE tasks SET status = :status WHERE id = :taskId")
    void updateStatus(int status, int taskId);

    @Query("SELECT * FROM tasks WHERE status = :status")
    List<Task> getTasksByStatus(int status);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks WHERE owner = :owner")
    LiveData<List<Task>> getTasksByOwner(String owner);

    @Query("SELECT * FROM tasks WHERE status = 0")
    LiveData<List<Task>> getOngoingTasks();

}
