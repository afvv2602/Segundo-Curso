package com.example.taskmanager.db.task;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.taskmanager.db.AppDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    public void delete(Task task) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            taskRepository.delete(task);
        });
    }
    public void initSampleTasks(String username) {
        List<Task> sampleTasks = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        // Crea algunas tareas de muestra con diferentes niveles de prioridad y estados
        for (int i = 1; i <= 20; i++) {
            String name = "Task " + i;
            String description = "Description for Task " + i;
            int tierIndex = i % 3;  // Alterna entre los tres niveles de prioridad
            Task.Tier tier;
            switch (tierIndex) {
                case 0:
                    tier = Task.Tier.LOW;
                    break;
                case 1:
                    tier = Task.Tier.DEFAULT;
                    break;
                case 2:
                    tier = Task.Tier.HIGH;
                    break;
                default:
                    tier = Task.Tier.DEFAULT;
                    break;
            }
            int statusIndex = i % 3;  // Alterna entre los tres estados
            Task.Status status;
            switch (statusIndex) {
                case 0:
                    status = Task.Status.IN_PROGRESS;
                    break;
                case 1:
                    status = Task.Status.COMPLETED;
                    break;
                case 2:
                    status = Task.Status.FAILED;
                    break;
                default:
                    status = Task.Status.IN_PROGRESS;
                    break;
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);  // Agrega un día a la fecha actual
            Date deadline = calendar.getTime();
            Task task = new Task(i, name, description, deadline, username, tier, status,calculateRemainingTime(deadline));
            sampleTasks.add(task);
        }
        AppDatabase.databaseWriteExecutor.execute(() -> {
            for (Task task : sampleTasks) {
                taskRepository.insert(task);
            }
        });
    }
    private String calculateRemainingTime(Date deadline) {
        Date currentDate = new Date();
        long differenceMillis = deadline.getTime() - currentDate.getTime();
        long differenceMinutes = TimeUnit.MILLISECONDS.toMinutes(differenceMillis);
        long minutesInDay = TimeUnit.DAYS.toMinutes(1);
        long remainingDays = differenceMinutes / minutesInDay;
        long remainingHours = (differenceMinutes % minutesInDay) / 60;
        long remainingMinutes = differenceMinutes % 60;
        String remainingTime;

        if (differenceMillis <= 0) {
            remainingTime = "Se ha acabado el tiempo";
        } else if (remainingDays > 0) {
            remainingTime = "Quedan: " + remainingDays + " días";
        } else if (remainingHours > 0) {
            remainingTime = "Quedan: " + remainingHours + " horas";
        } else {
            remainingTime = "Quedan: " + remainingMinutes + " minutos";
        }
        return remainingTime;
    }

}