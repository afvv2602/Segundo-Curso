package com.example.taskmanager.db.task;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "tasks")
@TypeConverters({Task.DateConverter.class, Task.TierConverter.class, Task.StatusConverter.class})
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private Date deadline;
    private String owner;
    private Tier tier;
    private String remainingTime;
    private Status status;

    // Constructor, getters y setters
    public Task(int id, String name, String description, Date deadline, String owner, Tier tier, Status status, String remainingTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.owner = owner;
        this.tier = tier;
        this.status = status;
        this.remainingTime = remainingTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public String getOwner() {
        return owner;
    }

    public Tier getTier() {
        return tier;
    }

    public Status getStatus() {
        return status;
    }

    public String getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        this.remainingTime = remainingTime;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Tier {
        HIGH, DEFAULT, LOW
    }

    public enum Status {
        COMPLETED, FAILED, IN_PROGRESS
    }

    // Convierte los valores para poder añadirlos a la base de datos
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

    public static class TierConverter {
        @TypeConverter
        public static Tier fromString(String value) {
            return value == null ? null : Tier.valueOf(value);
        }

        @TypeConverter
        public static String tierToString(Tier tier) {
            return tier == null ? null : tier.name();
        }
    }

    public static class StatusConverter {
        @TypeConverter
        public static Status fromString(String value) {
            return value == null ? null : Status.valueOf(value);
        }

        @TypeConverter
        public static String statusToString(Status status) {
            return status == null ? null : status.name();
        }
    }
}
