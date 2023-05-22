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

    // Constructor, getters y setters
    public Task(int id,String name,String description, Date deadline, String owner){
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.owner = owner;
    }

    public int getId(){ return this.id; }

    public String getName(){ return this.name; }

    public String getDescription(){ return this.description; }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getOwner(){ return this.owner; }

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