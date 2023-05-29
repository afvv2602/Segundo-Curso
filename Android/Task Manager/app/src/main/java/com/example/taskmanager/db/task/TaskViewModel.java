package com.example.taskmanager.db.task;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.taskmanager.db.AppDatabase;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
    }

    public LiveData<List<Task>> getTasksByOwner(String owner) {
        return taskRepository.getTasksByOwner(owner);
    }

    public LiveData<List<Task>> getOngoingTasks() {
        return taskRepository.getOngoingTasks();
    }

    public void addTask(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskRepository.insert(task);
        });
    }

    public void update(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskRepository.update(task);
        });
    }

    public void updateStatus(int status, int taskId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskRepository.updateStatus(status, taskId);
        });
    }

    public void delete(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskRepository.delete(task);
        });
    }
}