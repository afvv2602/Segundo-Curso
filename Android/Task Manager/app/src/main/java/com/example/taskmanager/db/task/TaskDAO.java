package com.example.taskmanager.db.task;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface TaskDAO { // DAO (Data access object)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insert(Task task);

    @Update
    void update(Task task);

    @Query("UPDATE tasks SET status = :status WHERE id = :taskId")
    void updateStatus(boolean status, int taskId);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks WHERE owner = :owner")
    LiveData<List<Task>> getTasksByOwner(String owner);
}
