package com.example.taskmanager.db.task;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface TaskDAO { // DAO (Data access object)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insert(Task task);

    @Query("SELECT * FROM tasks WHERE owner = :owner")
    LiveData<List<Task>> getTasksByOwner(String owner);

}
