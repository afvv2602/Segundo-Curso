package com.example.taskmanager.db.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDAO {

    // Inserta un usuario en la base de datos
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insert(User user);

    // Busca y devuelve un usuario por su nombre de usuario y contraseña
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User loginUser(String username, String password);

    // Busca y devuelve un usuario por su nombre
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findUserByUsername(String username);
}