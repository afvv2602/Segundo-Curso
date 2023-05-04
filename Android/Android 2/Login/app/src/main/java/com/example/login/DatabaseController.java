package com.example.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseController extends SQLiteOpenHelper {

    // Guardamos informacion de la tabla o columnas
    // de la base de datos en constantes para usarlas luego en los querys
    public static final String DB_NAME = "user_db";
    public static final String TABLA_USERS = "users";
    public static final String COL_ID = "id";
    public static final String COL_USER = "username";
    public static final String COL_PASSWORD = "password";
    public static final String COL_EMAIL = "email";
    public static final String COL_GENRE = "genre";
    public static final String COL_PHONE = "phone";

    // Constructor de la clase
    public DatabaseController(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    // Este metodo se ejecuta cuando se crea la base de datos por primera vez
    // Y se encarga de crear la tabla de alumnos
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLA_USERS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_GENRE + " TEXT, " +
                COL_PHONE + " TEXT)";
        db.execSQL(createTable);
    }

    // Se ejecuta cuando la tabla se actuliza
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla de usuarios existente y crear una nueva
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USERS);
        onCreate(db);
    }

    // Metodo de registro de un nuevo usuario
    public boolean registerUser(String user, String password, String email, String genre, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER, user);
        contentValues.put(COL_PASSWORD, password);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_GENRE, genre);
        contentValues.put(COL_PHONE, phone);
        long result = db.insert(TABLA_USERS, null, contentValues);
        return result != -1;
    }

    // Metodo login
    public boolean loginUser(String user, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_USERS + " WHERE " +
                COL_USER + "=? AND " + COL_PASSWORD + "=?", new String[]{user, password});
        return cursor.getCount() > 0;
    }

    // Este metodo devuelve la contraseña de un usuario especifico
    public String getPassword(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_PASSWORD + " FROM " + TABLA_USERS + " WHERE " + COL_USER + "=?", new String[]{user});
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COL_PASSWORD);
            if (columnIndex != -1) {
                return cursor.getString(columnIndex);
            }
        }
        return null;
    }

    //Devuelve el genero del usuario
    public String getUserGenre(String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_GENRE + " FROM " + TABLA_USERS + " WHERE " + COL_USER + "=?", new String[]{user});
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COL_GENRE);
            if (columnIndex != -1) {
                return cursor.getString(columnIndex);
            }
        }
        return null;
    }

}