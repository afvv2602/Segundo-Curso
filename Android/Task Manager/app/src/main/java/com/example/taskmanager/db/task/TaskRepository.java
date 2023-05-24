package com.example.taskmanager.db.task;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.taskmanager.db.AppDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TaskRepository {
    private TaskDAO taskDao;

    private ExecutorService executorService;

    public TaskRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        taskDao = db.taskDao();
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<Task>> getTasksByOwner(String owner) {
        return taskDao.getTasksByOwner(owner);
    }

    public void insert(Task task) {
        executorService.execute(() -> taskDao.insert(task));
    }

    public void update(Task task) {
        executorService.execute(() -> taskDao.update(task));
    }

    public void updateStatus(boolean status, int taskId) {
        executorService.execute(() -> {taskDao.updateStatus(status, taskId);});
    }

    public void delete(Task task) {
        executorService.execute(() -> taskDao.delete(task));
    }

}
