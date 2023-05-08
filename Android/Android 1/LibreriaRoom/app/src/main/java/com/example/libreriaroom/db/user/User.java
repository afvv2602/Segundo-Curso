package com.example.libreriaroom.db.user;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//Cada entity representa una tabla de nuestra base de datos
@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
