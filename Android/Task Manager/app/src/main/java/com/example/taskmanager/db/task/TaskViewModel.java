package com.example.taskmanager.db.task;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanager.db.AppDatabase;

// El ViewModel es el que se encarga de pasarle la informacion a la UI, se encarga de hacer
// de puente entre el UserDAO y la UI
public class TaskViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    private final MutableLiveData<List<Task>> userTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        AppDatabase appDatabase = AppDatabase.getDatabase(application);
        taskRepository = new TaskRepository(appDatabase.taskDao());
        userTasks = new MutableLiveData<>();
    }

    public LiveData<List<Task>> getUserTasks(String owner) {
        return taskRepository.getTasksByOwner(owner);
    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

}

