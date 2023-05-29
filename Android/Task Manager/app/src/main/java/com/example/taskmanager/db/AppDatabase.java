package com.example.taskmanager.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.taskmanager.db.user.User;
import com.example.taskmanager.db.user.UserDAO;
import com.example.taskmanager.db.task.Task;
import com.example.taskmanager.db.task.TaskDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Task.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Se añade un DAO para cada entidad
    public abstract UserDAO userDao();
    public abstract TaskDAO taskDao();

    private static volatile AppDatabase INSTANCE;

    // Un ExecutorService para ejecutar las operaciones de base de datos en segundo plano
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Se construye y obtiene la instancia de la base de datos
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}