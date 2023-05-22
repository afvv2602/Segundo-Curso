package com.example.taskmanager.db.user;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
@Dao
public interface UserDAO { // DAO (Data access object)

    // Aqui creamos las querys que luego vamos a ejecutar en la aplicacion
    // En este insert si este mismo user ya existe no lo añade a la base de datos
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long insert(User user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    User loginUser(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findUserByUsername(String username);

}
