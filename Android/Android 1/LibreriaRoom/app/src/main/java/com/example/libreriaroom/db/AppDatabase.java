package com.example.libreriaroom.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.libreriaroom.db.user.User;
import com.example.libreriaroom.db.user.UserDAO;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// https://developer.android.com/codelabs/android-room-with-a-view#0
@Database(entities = {User.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Hay que añadir cada uno de nuestros DAOs (Data access object) a la base de datos
    public abstract UserDAO userDao();
    private static volatile AppDatabase INSTANCE;

    // Una pool de hilos para que se hagan las querys y todas las operaciones
    // de forma asincrona sin entrar en el main thread
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
