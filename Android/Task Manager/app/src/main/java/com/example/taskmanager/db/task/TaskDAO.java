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
public interface TaskDAO {

    // Inserta una tarea en la base de datos
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insert(Task task);

    // Actualiza una tarea en la base de datos
    @Update
    void update(Task task);

    // Actualiza el estado de una tarea en la base de datos
    @Transaction
    @Query("UPDATE tasks SET status = :status WHERE id = :taskId")
    void updateStatus(int status, int taskId);

    // Obtiene las tareas por status
    @Query("SELECT * FROM tasks WHERE status = :status")
    List<Task> getTasksByStatus(int status);

    // Elimina una tarea de la base de datos
    @Delete
    void delete(Task task);

    // Obtiene las tareas que pertenecen a un usuario especifico
    @Query("SELECT * FROM tasks WHERE owner = :owner")
    LiveData<List<Task>> getTasksByOwner(String owner);

    // Obtiene las tareas en curso
    @Query("SELECT * FROM tasks WHERE status = 0")
    LiveData<List<Task>> getOngoingTasks();
}