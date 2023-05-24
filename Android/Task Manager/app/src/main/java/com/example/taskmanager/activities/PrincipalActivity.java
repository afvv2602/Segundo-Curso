package com.example.taskmanager.activities;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.*;

import com.example.taskmanager.*;
import com.example.taskmanager.db.task.*;
import com.example.taskmanager.task_fragments.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PrincipalActivity extends AppCompatActivity implements TaskAdapter.TaskClickListener {
    private String username;
    private TaskViewModel taskViewModel;
    private TaskAdapter taskAdapter;
    private EditText dateEdit,timeEdit,tierEdit;
    private int selectedYear, selectedMonth, selectedDay,selectedHour, selectedMinute;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity);
        username = getIntent().getStringExtra("username");
        TextView tv = findViewById(R.id.textView);
        RecyclerView taskRecyclerView = findViewById(R.id.task_recyclerview);
        Button addTask = findViewById(R.id.add_task_button);
        tv.setText("Bienvenido: "+username);
        initTaskView(taskRecyclerView, addTask);
    }

    // Inicializa la vista de tareas
    private void initTaskView(RecyclerView taskRecyclerView, Button addTask) {
        taskAdapter = new TaskAdapter(this);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        // Carga las tareas en el recycler view
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getUserTasks(username).observe(this, tasks -> taskAdapter.setTasks(tasks));

        addTask.setOnClickListener(view -> showAddTaskDialog());
    }

    // Muestra el dialogo para agregar tarea
    private void showAddTaskDialog() {
        // Crea un dialogo usando el fragment_task
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.fragment_add_task, null);
        builder.setView(dialogView);

        // Inicializa los elementos del dialogo
        EditText taskNameEdit = dialogView.findViewById(R.id.taskNameEdit);
        EditText descriptionEdit = dialogView.findViewById(R.id.taskDescriptionEdit);
        dateEdit = dialogView.findViewById(R.id.datePickerEdit);
        timeEdit = dialogView.findViewById(R.id.timePickerEdit);
        tierEdit = dialogView.findViewById(R.id.tierPickerEdit);
        Button buttonAdd = dialogView.findViewById(R.id.newTaskBtn);

        // Crea el dialogo pero con el fondo transparente
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Abre los pickers pulsando los edit text correspondientes
        dateEdit.setOnClickListener(v -> showDatePicker());
        timeEdit.setOnClickListener(v -> showTimePicker());
        tierEdit.setOnClickListener(view -> showTierPicker());
        // Muestra el dialogo
        dialog.show();

        buttonAdd.setOnClickListener(view -> {
            if (addTask(taskNameEdit, descriptionEdit)) dialog.dismiss();
            else Toast.makeText(getApplicationContext(), "Debes de rellenar todos los campos", Toast.LENGTH_SHORT).show();
        });
    }

    // Añade una tarea nueva
    private boolean addTask(EditText taskNameEdit, EditText descriptionEdit) {
        String name = taskNameEdit.getText().toString();
        String description = descriptionEdit.getText().toString();
        String date = dateEdit.getText().toString();
        String time = timeEdit.getText().toString();
        String tier = tierEdit.getText().toString();
        // Solo añadimos la tarea si todos los campos estan rellenos
        if (!name.isEmpty() && !description.isEmpty() && !date.isEmpty() && !time.isEmpty() && !tier.isEmpty()) {
            Calendar deadline = Calendar.getInstance();
            deadline.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
            Task newTask = new Task(0, name, description, deadline.getTime(), username,tier,false);
            taskViewModel.addTask(newTask);
            scheduleNotification(newTask);
            return true;
        }
        return false;
    }

    // Abre un panel para elegir la fecha del vencimiento de la tarea
    public void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
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

    // Abre un panel para elegir la hora del vencimiento de la tarea
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
        // Las opciones disponibles en el picker
        String[] tiers = {"Default", "Important", "Low"};

        // Crear un nuevo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Priority");

        // Establecer las opciones y el listener del clic
        builder.setItems(tiers, (dialog, which) -> {
            // Cada vez que se elija una opción, se actualizará el texto del EditText con esa opción
            tierEdit.setText(tiers[which]);
        });

        // Mostrar el AlertDialog
        builder.show();
    }

    // Creacion del popup cuando se pulsa encima de una task
    @Override
    public void onTaskClick(Task task) {
        // Crea un dialogo usando el fragment_task
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.fragment_task, null);
        builder.setView(dialogView);

        // Inicializa los elementos del dialogo
        TextView taskNameTextView = dialogView.findViewById(R.id.task_name);
        TextView taskDeadlineTextView = dialogView.findViewById(R.id.task_deadline);
        TextView taskDescriptionTextView = dialogView.findViewById(R.id.task_description);
        TextView taskTierTextView = dialogView.findViewById(R.id.task_description);
        Button deleteButton = dialogView.findViewById(R.id.button);
        Button completeButton = dialogView.findViewById(R.id.button2);
        ConstraintLayout taskBackground = dialogView.findViewById(R.id.task_background);

        // Rellena los elementos del dialogo con los datos de la tarea
        taskNameTextView.setText(task.getName());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateString = format.format(task.getDeadline());
        taskDeadlineTextView.setText(dateString);
        taskTierTextView.setText(task.getTier());
        taskDescriptionTextView.setText(task.getDescription());
        if(task.getStatus()){
            taskBackground.setBackgroundResource(R.drawable.tasks_card_completed);
            completeButton.setVisibility(View.GONE);
            deleteButton.setBackgroundResource(R.drawable.btn_completed);
        }else{
            switch (task.getTier()) {
                case "Important":
                    taskBackground.setBackgroundResource(R.drawable.tasks_card_important);
                    completeButton.setBackgroundResource(R.drawable.btn_important);
                    deleteButton.setBackgroundResource(R.drawable.btn_important);
                    break;
                case "Low":
                    taskBackground.setBackgroundResource(R.drawable.tasks_card_low);
                    completeButton.setBackgroundResource(R.drawable.btn_low);
                    deleteButton.setBackgroundResource(R.drawable.btn_low);
                    break;
                default:
                    taskBackground.setBackgroundResource(R.drawable.tasks_card);
                    completeButton.setBackgroundResource(R.drawable.btn_border);
                    deleteButton.setBackgroundResource(R.drawable.btn_border);
                    break;
            }
        }
        // Crea el dialogo pero con el fondo transparente
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Muestra el dialogo
        dialog.show();

        deleteButton.setOnClickListener(view -> {
            taskViewModel.delete(task);
            dialog.dismiss();
        });

        completeButton.setOnClickListener(view -> {
            task.setStatus(true); // o lo que sea que necesites hacer para cambiar el estado a true
            taskViewModel.updateStatus(true, task.getId()); // asumiendo que getId() retorna el id de la tarea
            taskBackground.setBackgroundResource(R.drawable.tasks_card_completed); // cambiar a un drawable que represente una tarea completa
            dialog.dismiss();
        });
    }

    // Programar la notificacion para que sea un dia antes
    private void scheduleNotification(Task task) {
        Intent notificationIntent = new Intent(this, TaskNotificationReceiver.class);
        notificationIntent.putExtra("taskName", task.getName());

        // Si ya hay una notificacion con ese id actualiza la informacion
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

        //Se crea la alarma un dia antes de la fecha limite elegida por el usuario
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, deadline.getTimeInMillis(), pendingIntent);
    }
}
