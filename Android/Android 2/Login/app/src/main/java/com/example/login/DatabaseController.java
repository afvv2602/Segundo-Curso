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
    private static final String DB_NAME = "user_db";
    private static final String TABLA_USERS = "users";
    private static final String COL_ID = "id";
    private static final String COL_USER = "username";
    private static final String COL_PASSWORD = "password";
    private static final String COL_EMAIL = "email";
    private static final String COL_PHONE = "phone";
    private static final String COL_GENRE = "genre";
    private static final String COL_LOGIN_HISTORY = "login_history";
    private static SQLiteDatabase db;

    // Constructor de la clase
    public DatabaseController(@Nullable Context context) {
        super(context, DB_NAME, null, 3);
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
                COL_PHONE + " TEXT, " +
                COL_LOGIN_HISTORY + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar la tabla de usuarios existente y crear una nueva
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_USERS);
        onCreate(db);
    }
    public boolean registerUser(String user, String password, String email, String genre, String phone) {
        db = this.getWritableDatabase();
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
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_USERS + " WHERE " +
                COL_USER + "=? AND " + COL_PASSWORD + "=?", new String[]{user, password});
        return cursor.getCount() > 0;
    }
    public boolean saveLogin(String user, String loginTime) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_LOGIN_HISTORY, loginTime);
        int query = db.update(TABLA_USERS, contentValues, COL_USER + "=?", new String[]{user});
        return query > 0;
    }

    // Actualiza los datos de usuario, menos el nombre de usuario
    public boolean updateUser(String user, String password, String email, String genre, String phone) {
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_PASSWORD, password);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PHONE, phone);
        contentValues.put(COL_GENRE, genre);
        int query = db.update(TABLA_USERS, contentValues, COL_USER + "=?", new String[]{user});
        return query > 0;
    }
    public boolean deleteUser(String user) {
        db = this.getWritableDatabase();
        int query = db.delete(TABLA_USERS, COL_USER + "=?", new String[]{user});
        return query > 0;
    }

    // Getters
    public String getPassword(String user) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_PASSWORD + " FROM " + TABLA_USERS + " WHERE " + COL_USER + "=?", new String[]{user});
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COL_PASSWORD);
            if (columnIndex != -1) {
                return cursor.getString(columnIndex);
            }
        }
        return null;
    }
    public String getUserGenre(String user) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_GENRE + " FROM " + TABLA_USERS + " WHERE " + COL_USER + "=?", new String[]{user});
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COL_GENRE);
            if (columnIndex != -1) {
                return cursor.getString(columnIndex);
            }
        }
        return null;
    }
    public String getEmail(String username) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_EMAIL + " FROM " + TABLA_USERS + " WHERE " + COL_USER + "=?", new String[]{username});
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COL_EMAIL);
            if (columnIndex != -1) {
                return cursor.getString(columnIndex);
            }
        }
        cursor.close();
        return null;
    }
    public String getPhone(String username) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_PHONE + " FROM " + TABLA_USERS + " WHERE " + COL_USER + "=?", new String[]{username});
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COL_PHONE);
            if (columnIndex != -1) {
                return cursor.getString(columnIndex);
            }
        }
        cursor.close();
        return null;
    }
    public String getLastLogin(String user) {
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_LOGIN_HISTORY + " FROM " + TABLA_USERS + " WHERE " + COL_USER + "=?", new String[]{user});
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(COL_LOGIN_HISTORY);
            if (columnIndex != -1) {
                return cursor.getString(columnIndex);
            }
        }
        return null;
    }
}