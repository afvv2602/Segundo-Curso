package com.example.taskmanager.db.task;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "tasks")
@TypeConverters(Task.DateConverter.class)
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private Date deadline;
    private String owner;
    private String tier;
    private int status;

    // Constructor, getters y setters
    public Task(int id,String name,String description, Date deadline, String owner,String tier,int status){
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.owner = owner;
        this.tier = tier;
        this.status = status;
    }

    public int getId(){ return this.id; }

    public String getName(){ return this.name; }

    public String getDescription(){ return this.description; }

    public Date getDeadline() {
        return deadline;
    }
    public String getOwner(){ return this.owner; }
    public String getTier(){ return this.tier; }
    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // Convierte los valores para poder añadirlos a la base de datos
    // Los @TypeConverter son una caracteristica del framework room
    // para facilitar la conversion de tipos que no pueden guardarse en la base de datos
    // Estos metodos permiten que room pueda interactuar con los objetos tipo date
    public static class DateConverter {
        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
    }

}