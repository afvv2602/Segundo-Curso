package com.example.taskmanager.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.db.task.Task;
import com.example.taskmanager.db.task.TaskViewModel;
import com.example.taskmanager.task_fragments.TaskAdapter;
import com.example.taskmanager.task_fragments.TaskNotificationReceiver;

import java.util.Calendar;

public class PrincipalActivity extends AppCompatActivity implements TaskAdapter.TaskClickListener {
    String username;
    TaskViewModel taskViewModel;
    RecyclerView taskRecyclerView;
    TaskAdapter taskAdapter;
    Button addTask;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity);
        username = getIntent().getStringExtra("username");
        TextView tv = findViewById(R.id.textView);
        taskRecyclerView = findViewById(R.id.task_recyclerview);
        addTask = findViewById(R.id.add_task_button);

        if (username != null) {
            tv.setText("Bienvenido: "+username);
        }

        // Inicializar el RecyclerView y el adaptador con lista vacía
        taskAdapter = new TaskAdapter(this);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        // Inicializar el TaskViewModel y observar los cambios en la lista de tareas
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getUserTasks(username).observe(this, tasks -> {
            // Actualizar la lista de tareas en el adaptador cuando haya cambios
            taskAdapter.setTasks(tasks);
        });

        // Definir la acción del botón para añadir tarea
        addTask.setOnClickListener(view -> showAddTaskDialog());
    }

    private void showAddTaskDialog() {
        // Crear el builder del AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Obtener el layout del diálogo
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_task, null);

        // Asignar el layout al builder
        builder.setView(dialogView);

        // Crear los EditTexts y el botón
        EditText editTextName = dialogView.findViewById(R.id.edittext_name);
        DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
        TimePicker timePicker = dialogView.findViewById(R.id.time_picker);
        EditText editTextDescripcion = dialogView.findViewById(R.id.edittext_descripcion);
        Button buttonAdd = dialogView.findViewById(R.id.button_add);

        // Crear el AlertDialog
        AlertDialog dialog = builder.create();

        // Definir la acción del botón de añadir
        buttonAdd.setOnClickListener(view -> {
            String name = editTextName.getText().toString();
            String description = editTextDescripcion.getText().toString();
            if (!name.isEmpty() && !description.isEmpty()) {
                // Obtener la fecha y hora seleccionadas
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();

                // Convertir la fecha y hora seleccionadas en un objeto Calendar
                Calendar deadline = Calendar.getInstance();
                deadline.set(year, month, day, hour, minute);

                // Crear la nueva tarea
                Task newTask = new Task(0, name, description, deadline.getTime(), username);
                taskViewModel.addTask(newTask);

                // Programar una notificación para un día antes de la fecha límite
                scheduleNotification(newTask);

                // Cerrar el diálogo
                dialog.dismiss();
            }
        });

        // Mostrar el diálogo
        builder.create().show();
    }

    private void showTaskDetailDialog(Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Task Details");
        builder.setMessage(
                "Name: " + task.getName() + "\n" +
                "Deadline: " + task.getDeadline() + "\n" +
                "Description: " + task.getDescription()
        );
        builder.setPositiveButton("Close", null);
        builder.show();
    }

    @Override
    public void onTaskClick(Task task) {
        showTaskDetailDialog(task);
    }

    private void scheduleNotification(Task task) {
        // Crear un Intent para la notificación
        Intent notificationIntent = new Intent(this, TaskNotificationReceiver.class);
        notificationIntent.putExtra("taskName", task.getName());

        // Crear un PendingIntent para la notificación
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                task.getId(), // Este ID debe ser único para cada notificación
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        // Obtener el servicio de alarmas
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Programar la alarma
        Calendar deadline = Calendar.getInstance();
        deadline.setTime(task.getDeadline());
        deadline.add(Calendar.DATE, -1); // Un día antes
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, deadline.getTimeInMillis(), pendingIntent);
    }

}
