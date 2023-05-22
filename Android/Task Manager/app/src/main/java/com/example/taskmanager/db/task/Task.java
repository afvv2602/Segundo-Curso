package com.example.taskmanager.db.task;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String deadline;
    private String owner;

    // Constructor, getters y setters
    public Task(int id,String name,String description, String deadline, String owner){
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.owner = owner;
    }

    public int getId(){ return this.id; }

    public String getName(){ return this.name; }

    public String getDescription(){ return this.description; }

    public String getDeadline(){ return this.deadline; }

    public String getOwner(){ return this.owner; }

}