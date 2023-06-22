package com.example.taskmanager.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
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
import com.example.taskmanager.utils.NotificationUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PrincipalActivity extends AppCompatActivity implements TaskAdapter.TaskClickListener {
    private String username;
    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;
    private TaskNotificationReceiver notificationReceiver;
    private EditText searchTask;
    private Button filter, search, addTask, delete;
    private LiveData<List<Task>> tasksLiveData;
    private List<Task> filteredTasks;
    private FilterUtils.FilterType currentFilter = FilterUtils.FilterType.NONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity);
        initTaskView();
        initNavigationView();
    }

    // Inicializa la vista de tareas
    private void initTaskView() {
        username = getIntent().getStringExtra("username");

        // Configura el RecyclerView y el adaptador
        RecyclerView taskRecyclerView = findViewById(R.id.task_recyclerview);
        TaskRepository taskRepository = new TaskRepository(getApplication());
        taskAdapter = new TaskAdapter(this, taskRepository);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        // Obtener el ViewModel y las tareas del usuario
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        tasksLiveData = taskViewModel.getTasksByOwner(username);
        if (tasksLiveData.getValue() == null) {
            taskViewModel.initSampleTasks(username);
            tasksLiveData = taskViewModel.getTasksByOwner(username);
        }
        tasksLiveData.observe(this, tasks -> {
            filteredTasks = FilterUtils.applyFilter(tasks, currentFilter);
            taskAdapter.setTasks(filteredTasks);
        });
        notificationReceiver = new TaskNotificationReceiver();
        notificationReceiver.setTaskViewModel(taskViewModel);
    }

    // Inicializa el menu de lateral
    private void initNavigationView() {
        LinearLayout drawerContentLayout = findViewById(R.id.drawerLayout);
        LayoutInflater inflater = LayoutInflater.from(this);
        View sideMenu = inflater.inflate(R.layout.fragment_menu, drawerContentLayout, false);
        drawerContentLayout.addView(sideMenu);

        filter = sideMenu.findViewById(R.id.task_filter);
        search = sideMenu.findViewById(R.id.search_task);
        addTask = sideMenu.findViewById(R.id.add_task_button);
        delete = sideMenu.findViewById(R.id.delete_filters);

        filter.setOnClickListener(v -> showFilterDialog());
        search.setOnClickListener(v -> showSearchTaskDialog());
        addTask.setOnClickListener(v -> showAddTaskDialog());
        delete.setOnClickListener(v -> deleteFilters());

        TextView welcomeTextView = sideMenu.findViewById(R.id.welcomeTextView);
        welcomeTextView.setText(String.format("Bienvenido %s", username));
    }

    // Elimina los filtros
    private void deleteFilters() {
        if (searchTask != null) {
            searchTask.setText("");
        }
        currentFilter = FilterUtils.FilterType.NONE;
        taskAdapter.applyFilter(currentFilter);
        filter.setText("Filtrar");
    }

    // Muestra un dialogo para seleccionar un filtro
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona un filtro");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.addAll("Ninguno", "Completas", "En proceso", "Fallidas", "Prioridad alta", "Prioridad media", "Prioridad baja");

        Spinner spinner = new Spinner(this);
        spinner.setAdapter(adapter);
        spinner.setSelection(getFilterPosition());
        builder.setView(spinner);

        builder.setPositiveButton("Aplicar", (dialog, which) -> {
            int selectedPosition = spinner.getSelectedItemPosition();
            applyFilter(selectedPosition);
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Muestra un dialogo para agregar una nueva tarea
    private void showAddTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_add_task, null);
        builder.setView(dialogView);

        EditText taskNameEdit = dialogView.findViewById(R.id.taskNameEdit);
        EditText descriptionEdit = dialogView.findViewById(R.id.taskDescriptionEdit);
        EditText dateEdit = dialogView.findViewById(R.id.datePickerEdit);
        EditText timeEdit = dialogView.findViewById(R.id.timePickerEdit);
        EditText tierEdit = dialogView.findViewById(R.id.tierPickerEdit);
        Button buttonAdd = dialogView.findViewById(R.id.newTaskBtn);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        dateEdit.setOnClickListener(v -> showDatePicker(dateEdit));
        timeEdit.setOnClickListener(v -> showTimePicker(timeEdit));
        tierEdit.setOnClickListener(v -> showTierPicker(tierEdit));

        buttonAdd.setOnClickListener(v -> {
            if (addTask(taskNameEdit, descriptionEdit, dateEdit, timeEdit, tierEdit)) {
                dialog.dismiss();
            } else {
                Toast.makeText(getApplicationContext(), "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Muestra un dialogo para buscar una tarea por su nombre
    private void showSearchTaskDialog() {
        currentFilter = FilterUtils.FilterType.NONE;
        taskAdapter.applyFilter(currentFilter);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_search_task, null);
        searchTask = dialogView.findViewById(R.id.search_edit_text);
        searchTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Actualiza la lista de tareas solo si la busqueda ha cambiado
                if (!s.toString().equals(search.getText().toString())) {
                    searchTaskByName(s.toString());
                    search.setText(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(dialogInterface -> {
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(window.getAttributes());
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                layoutParams.gravity = Gravity.TOP;
                window.setAttributes(layoutParams);
            }
        });

        dialog.show();
    }

    // Muestra un selector de fecha
    private void showDatePicker(EditText dateEdit) {
        Calendar calendar = Calendar.getInstance();
        CustomDatePicker datePickerDialog = new CustomDatePicker(
                this,
                (view, year, month, dayOfMonth) -> {
                    dateEdit.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    // Muestra un selector de hora
    private void showTimePicker(EditText timeEdit) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    timeEdit.setText(String.format("%02d:%02d", hourOfDay, minute));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void showTierPicker(EditText tierEdit) {
        String[] tiers = {"Default", "High", "Low"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Priority");

        builder.setItems(tiers, (dialog, which) -> {
            tierEdit.setText(tiers[which]);
        });

        builder.show();
    }

    // Aplica el filtro seleccionado
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
        filter.setText(currentFilter.toString().toLowerCase());
        taskAdapter.applyFilter(currentFilter);
    }

    // Obtiene la posicion del filtro actual
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
                return 0;
        }
    }

    // Realiza la busqueda de tareas por nombre
    private void searchTaskByName(String searchQuery) {
        currentFilter = FilterUtils.FilterType.SEARCH;
        List<Task> matchingTasks = FilterUtils.applyFilter(filteredTasks, currentFilter, searchQuery);
        if (filteredTasks != null) {
            for (Task task : filteredTasks) {
                if (task.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                    matchingTasks.add(task);
                }
            }
        }
        if (!matchingTasks.isEmpty()) {
            taskAdapter.setTasks(matchingTasks);
        }
    }

    // Agrega una nueva tarea
    private boolean addTask(EditText taskNameEdit, EditText descriptionEdit, EditText dateEdit, EditText timeEdit, EditText tierEdit) {
        String name = taskNameEdit.getText().toString();
        String description = descriptionEdit.getText().toString();
        String date = dateEdit.getText().toString();
        String time = timeEdit.getText().toString();
        String tier = tierEdit.getText().toString();

        if (!name.isEmpty() && !description.isEmpty() && !date.isEmpty() && !time.isEmpty() && !tier.isEmpty()) {
            int selectedYear = 0;
            int selectedMonth = 0;
            int selectedDay = 0;
            int selectedHour = 0;
            int selectedMinute = 0;
            try {
                String[] dateParts = date.split("/");
                String[] timeParts = time.split(":");
                selectedYear = Integer.parseInt(dateParts[2]);
                selectedMonth = Integer.parseInt(dateParts[1]) - 1;
                selectedDay = Integer.parseInt(dateParts[0]);
                selectedHour = Integer.parseInt(timeParts[0]);
                selectedMinute = Integer.parseInt(timeParts[1]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            Calendar deadline = Calendar.getInstance();
            deadline.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
            Task newTask = new Task(0, name, description, deadline.getTime(), username, Task.Tier.valueOf(tier.toUpperCase()), Task.Status.IN_PROGRESS, remainingTime(deadline.getTime()));
            taskViewModel.addTask(newTask);
            currentFilter = FilterUtils.FilterType.NONE;
            taskAdapter.applyFilter(currentFilter);
            NotificationUtils.scheduleTaskNotifications(this,newTask);
            return true;
        }
        return false;
    }


    // Maneja el evento de clic en una tarea
    // Si el menu esta abierto desactiva esta funcion
    public void onTaskClick(Task task) {
        if (!isMenuInflated()) {
            View dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_task, null);
            AlertDialog dialog = createDialog(dialogView);
            initTaskDialog(dialog, dialogView, task);
        }
    }

    // Crea un dialogo personalizado para mostrar los detalles de una tarea
    private AlertDialog createDialog(View dialogView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        return dialog;
    }

    // Configura el dialogo de las tareas
    private void initTaskDialog(AlertDialog dialog, View dialogView, Task task) {
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
            NotificationUtils.cancelTaskNotifications(this,task);
            taskViewModel.delete(task);
            dialog.dismiss();
            NotificationUtils.showNotification(this,"Tarea eliminada", task.getName());
            NotificationUtils.playNotificationSound(this, false);
        });

        completeButton.setOnClickListener(view -> {
            Task.Status taskStatus = Task.Status.COMPLETED;
            NotificationUtils.cancelTaskNotifications(this,task);
            task.setStatus(taskStatus);
            taskViewModel.update(task);
            dialog.dismiss();
            NotificationUtils.showNotification(this,"Tarea Completada", task.getName());
            NotificationUtils.playNotificationSound(this, true);
        });
    }

    // Establece el fondo y los estilos de los botones en el dialogo
    private void setDialogBackground(Task task, LinearLayout taskBackground, Button completeButton, Button deleteButton) {
        int backgroundResource;
        int completeButtonResource;
        int deleteButtonResource;

        switch (task.getStatus()) {
            case COMPLETED:
                backgroundResource = R.drawable.tasks_card_completed;
                completeButtonResource = R.drawable.btn_completed;
                deleteButtonResource = R.drawable.btn_completed;
                break;
            case FAILED:
                backgroundResource = R.drawable.tasks_card_failed;
                completeButtonResource = R.drawable.btn_important;
                deleteButtonResource = R.drawable.btn_important;
                break;
            default:
                switch (task.getTier()) {
                    case HIGH:
                        backgroundResource = R.drawable.tasks_card_important;
                        completeButtonResource = R.drawable.btn_important;
                        deleteButtonResource = R.drawable.btn_important;
                        break;
                    case LOW:
                        backgroundResource = R.drawable.tasks_card_low;
                        completeButtonResource = R.drawable.btn_low;
                        deleteButtonResource = R.drawable.btn_low;
                        break;
                    default:
                        backgroundResource = R.drawable.tasks_card;
                        completeButtonResource = R.drawable.btn_default;
                        deleteButtonResource = R.drawable.btn_default;
                }
        }

        taskBackground.setBackgroundResource(backgroundResource);
        completeButton.setBackgroundResource(completeButtonResource);
        deleteButton.setBackgroundResource(deleteButtonResource);
    }


    // Calcula el tiempo restante para la fecha limite de la tarea
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

    // Revisa si el menu lateral se esta mostrando
    private boolean isMenuInflated() {
        LinearLayout drawerContentLayout = findViewById(R.id.drawerLayout);
        if (drawerContentLayout != null) {
            int visibility = drawerContentLayout.getVisibility();
            return visibility == View.VISIBLE;
        }
        return false;
    }
}
