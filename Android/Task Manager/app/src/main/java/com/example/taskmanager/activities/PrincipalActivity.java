package com.example.taskmanager.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.db.task.Task;
import com.example.taskmanager.db.task.TaskRepository;
import com.example.taskmanager.db.task.TaskViewModel;
import com.example.taskmanager.task_fragments.TaskAdapter;
import com.example.taskmanager.task_fragments.TaskExpirationNotificationReceiver;
import com.example.taskmanager.task_fragments.TaskNotificationReceiver;
import com.example.taskmanager.utils.CustomDatePicker;
import com.example.taskmanager.utils.FilterUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PrincipalActivity extends AppCompatActivity implements TaskAdapter.TaskClickListener {
    private String username;
    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;
    private TaskNotificationReceiver notificationReceiver;
    private EditText dateEdit, timeEdit, tierEdit;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    private List<Task> filteredTasks;
    private FilterUtils.FilterType currentFilter = FilterUtils.FilterType.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity);
        username = getIntent().getStringExtra("username");
        RecyclerView taskRecyclerView = findViewById(R.id.task_recyclerview);
        FloatingActionButton addTask = findViewById(R.id.add_task_button);
        FloatingActionButton filterTask = findViewById(R.id.task_filter);
        FloatingActionButton testTask = findViewById(R.id.task_test);
        initTaskView(taskRecyclerView, addTask, filterTask, testTask);
    }

    // Inicializa la vista de tareas
    private void initTaskView(RecyclerView taskRecyclerView, FloatingActionButton addTask, FloatingActionButton filterTask, FloatingActionButton testTask) {
        TaskRepository taskRepository = new TaskRepository(getApplication());
        taskAdapter = new TaskAdapter(this,taskRepository);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        LiveData<List<Task>> tasksLiveData = taskViewModel.getTasksByOwner(username);
        tasksLiveData.observe(this, tasks -> {
            List<Task> filteredTasks = FilterUtils.applyFilter(tasks, currentFilter);
            taskAdapter.setTasks(filteredTasks);
        });
        notificationReceiver = new TaskNotificationReceiver();
        notificationReceiver.setTaskViewModel(taskViewModel);

        filterTask.setOnClickListener(view -> showFilterDialog());
        testTask.setOnClickListener(view -> showFilterDialog());
        addTask.setOnClickListener(view -> showAddTaskDialog());

        List<Task> tasks = tasksLiveData.getValue();
        if (tasks != null) {
            filteredTasks = FilterUtils.applyFilter(tasks, currentFilter);
            taskAdapter.setTasks(filteredTasks);
        } else {
            taskViewModel.initSampleTasks(username);
        }
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Filter");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll("None", "Completed", "Incomplete", "Failed" ,"High Priority", "Mid Priority" ,"Low Priority");

        Spinner spinner = new Spinner(this);
        spinner.setAdapter(adapter);

        // Mostrar el filtro actual seleccionado en el Spinner
        spinner.setSelection(getFilterPosition());

        builder.setView(spinner);

        builder.setPositiveButton("Apply", (dialog, which) -> {
            int selectedPosition = spinner.getSelectedItemPosition();
            applyFilter(selectedPosition);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void applyFilter(int filterPosition) {
        switch (filterPosition) {
            case 1:
                currentFilter = FilterUtils.FilterType.COMPLETED;
                break;
            case 2:
                currentFilter = FilterUtils.FilterType.INCOMPLETE;
                break;
            case 3:
                currentFilter = FilterUtils.FilterType.FAILED;
                break;
            case 4:
                currentFilter = FilterUtils.FilterType.HIGH_PRIORITY;
                break;
            case 5:
                currentFilter = FilterUtils.FilterType.MID_PRIORITY;
                break;
            case 6:
                currentFilter = FilterUtils.FilterType.LOW_PRIORITY;
                break;
            default:
                currentFilter = FilterUtils.FilterType.NONE;
                break;
        }
        taskAdapter.applyFilter(currentFilter);
    }

    private int getFilterPosition() {
        switch (this.currentFilter) {
            case COMPLETED:
                return 1;
            case INCOMPLETE:
                return 2;
            case FAILED:
                return 3;
            case HIGH_PRIORITY:
                return 4;
            case MID_PRIORITY:
                return 5;
            case LOW_PRIORITY:
                return 6;
            default:
                return 0; // None
        }
    }

    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_add_task, null);
        builder.setView(dialogView);

        EditText taskNameEdit = dialogView.findViewById(R.id.taskNameEdit);
        EditText descriptionEdit = dialogView.findViewById(R.id.taskDescriptionEdit);
        dateEdit = dialogView.findViewById(R.id.datePickerEdit);
        timeEdit = dialogView.findViewById(R.id.timePickerEdit);
        tierEdit = dialogView.findViewById(R.id.tierPickerEdit);
        Button buttonAdd = dialogView.findViewById(R.id.newTaskBtn);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        dateEdit.setOnClickListener(v -> showDatePicker());
        timeEdit.setOnClickListener(v -> showTimePicker());
        tierEdit.setOnClickListener(view -> showTierPicker());

        buttonAdd.setOnClickListener(view -> {
            if (addTask(taskNameEdit, descriptionEdit)) {
                dialog.dismiss();
            } else {
                Toast.makeText(getApplicationContext(), "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean addTask(EditText taskNameEdit, EditText descriptionEdit) {
        String name = taskNameEdit.getText().toString();
        String description = descriptionEdit.getText().toString();
        String date = dateEdit.getText().toString();
        String time = timeEdit.getText().toString();
        String tier = tierEdit.getText().toString();

        if (!name.isEmpty() && !description.isEmpty() && !date.isEmpty() && !time.isEmpty() && !tier.isEmpty()) {
            Calendar deadline = Calendar.getInstance();
            deadline.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
            Task newTask = new Task(0, name, description, deadline.getTime(), username, Task.Tier.valueOf(tier.toUpperCase()), Task.Status.IN_PROGRESS, remainingTime(deadline.getTime()));
            taskViewModel.addTask(newTask);
            scheduleNotification(newTask);
            scheduleExpiration(newTask);
            return true;
        }
        return false;
    }

    public void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        CustomDatePicker datePickerDialog = new CustomDatePicker(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedYear = year;
                    selectedMonth = month;
                    selectedDay = dayOfMonth;
                    dateEdit.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    timeEdit.setText(String.format("%02d:%02d", hourOfDay, minute));
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void showTierPicker() {
        String[] tiers = {"Default", "High", "Low"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Priority");

        builder.setItems(tiers, (dialog, which) -> {
            tierEdit.setText(tiers[which]);
        });

        builder.show();
    }

    public void onTaskClick(Task task) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_task, null);
        AlertDialog dialog = createDialog(dialogView);
        handleButtonClicks(dialog, dialogView, task);
    }

    private AlertDialog createDialog(View dialogView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        return dialog;
    }

    private void handleButtonClicks(AlertDialog dialog, View dialogView, Task task) {
        TextView taskNameTextView = dialogView.findViewById(R.id.task_name);
        TextView taskDeadlineTextView = dialogView.findViewById(R.id.task_deadline);
        TextView taskDescriptionTextView = dialogView.findViewById(R.id.task_description);
        TextView taskTierTextView = dialogView.findViewById(R.id.task_tier);
        Button deleteButton = dialogView.findViewById(R.id.button);
        Button completeButton = dialogView.findViewById(R.id.button2);
        LinearLayout taskBackground = dialogView.findViewById(R.id.task_background);

        taskNameTextView.setText(task.getName());
        taskDeadlineTextView.setText(remainingTime(task.getDeadline()));
        taskTierTextView.setText("Tier: " + task.getTier());
        taskDescriptionTextView.setText(task.getDescription());

        setDialogBackground(task, taskBackground, completeButton, deleteButton);

        deleteButton.setOnClickListener(view -> {
            taskViewModel.delete(task);
            dialog.dismiss();
        });

        completeButton.setOnClickListener(view -> {
            Task.Status taskStatus = Task.Status.COMPLETED;
            task.setStatus(taskStatus);
            taskViewModel.update(task);
            scheduleTaskNotification(task, taskStatus.ordinal());
            dialog.dismiss();
        });
    }

    private void setDialogBackground(Task task, LinearLayout taskBackground, Button completeButton, Button deleteButton) {
        switch (task.getStatus()) {
            case COMPLETED:
                taskBackground.setBackgroundResource(R.drawable.tasks_card_completed);
                completeButton.setVisibility(View.GONE);
                deleteButton.setBackgroundResource(R.drawable.btn_completed);
                break;
            case FAILED:
                taskBackground.setBackgroundResource(R.drawable.tasks_card_failed);
                completeButton.setBackgroundResource(R.drawable.btn_important);
                deleteButton.setBackgroundResource(R.drawable.btn_important);
                break;
            default:
                switch (task.getTier()) {
                    case HIGH:
                        taskBackground.setBackgroundResource(R.drawable.tasks_card_important);
                        completeButton.setBackgroundResource(R.drawable.btn_important);
                        deleteButton.setBackgroundResource(R.drawable.btn_important);
                        break;
                    case LOW:
                        taskBackground.setBackgroundResource(R.drawable.tasks_card_low);
                        completeButton.setBackgroundResource(R.drawable.btn_low);
                        deleteButton.setBackgroundResource(R.drawable.btn_low);
                        break;
                    default:
                        taskBackground.setBackgroundResource(R.drawable.tasks_card);
                        completeButton.setBackgroundResource(R.drawable.btn_default);
                }
        }
    }

    private void scheduleTaskNotification(Task task, int taskCompleted) {
        Intent notificationIntent = new Intent(this, TaskNotificationReceiver.class);
        notificationIntent.putExtra("taskName", task.getName());
        notificationIntent.putExtra("taskId", task.getId());
        notificationIntent.putExtra("taskStatus", taskCompleted);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                task.getId(),
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, task.getDeadline().getTime(), pendingIntent);
    }

    private void scheduleNotification(Task task) {
        Intent notificationIntent = new Intent(this, TaskNotificationReceiver.class);
        notificationIntent.putExtra("taskName", task.getName());
        notificationIntent.putExtra("taskId", task.getId());
        notificationIntent.putExtra("taskStatus", task.getStatus());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                task.getId(),
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar deadline = Calendar.getInstance();
        deadline.setTime(task.getDeadline());
        deadline.add(Calendar.DATE, -1);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, deadline.getTimeInMillis(), pendingIntent);
    }

    private void scheduleExpiration(Task task) {
        Intent expirationIntent = new Intent(this, TaskExpirationNotificationReceiver.class);
        expirationIntent.putExtra("taskName", task.getName());
        expirationIntent.putExtra("taskId", task.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                task.getId(),
                expirationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar deadline = Calendar.getInstance();
        deadline.setTime(task.getDeadline());
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, deadline.getTimeInMillis(), pendingIntent);
    }

    private String remainingTime(Date deadline) {
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
