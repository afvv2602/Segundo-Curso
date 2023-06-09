package com.example.taskmanager.activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.db.task.Task;
import com.example.taskmanager.db.task.TaskRepository;
import com.example.taskmanager.db.task.TaskViewModel;
import com.example.taskmanager.task_fragments.TaskAdapter;
import com.example.taskmanager.utils.CustomDatePicker;
import com.example.taskmanager.utils.DuplicateUtils;
import com.example.taskmanager.utils.FilterUtils;
import com.example.taskmanager.utils.NotificationUtils;
import com.example.taskmanager.utils.TaskDialogUtils;

import java.util.Calendar;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity implements TaskAdapter.TaskClickListener {
    private String username;
    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;
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
        taskAdapter = new TaskAdapter(this, this, taskRepository);
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
            Task newTask = new Task(0, name, description, deadline.getTime(), username, Task.Tier.valueOf(tier.toUpperCase()), Task.Status.IN_PROGRESS, DuplicateUtils.remainingTime(deadline.getTime()));
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
            TaskDialogUtils.showTaskDialog(this, task,taskViewModel);
        }
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
