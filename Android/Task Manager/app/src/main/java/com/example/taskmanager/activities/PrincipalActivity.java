    package com.example.taskmanager.activities;

    import android.app.AlarmManager;
    import android.app.AlertDialog;
    import android.app.DatePickerDialog;
    import android.app.PendingIntent;
    import android.app.TimePickerDialog;
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
        EditText dateEdit;

        int selectedYear, selectedMonth, selectedDay;



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

            // Iniciamos el adaptador y la vista con la lista vacia
            taskAdapter = new TaskAdapter(this);
            taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            taskRecyclerView.setAdapter(taskAdapter);

            // Se inicia el ViewModel para poder observar los cambios en la lista
            taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
            taskViewModel.getUserTasks(username).observe(this, tasks -> {
                // Actualizamos la lista en cuanto haya cambios en la BBDD
                taskAdapter.setTasks(tasks);
            });

            // Boton añadir tarea
            addTask.setOnClickListener(view -> showAddTaskDialog());
        }

        private void showAddTaskDialog() {
            // Creacion del dialogo de alerta
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.fragment_task, null);

            // Asignar el layout al builder
            builder.setView(dialogView);

            // Inicializamos la vista
            EditText taskNameEdit = dialogView.findViewById(R.id.taskNameEdit);
            EditText descriptionEdit = dialogView.findViewById(R.id.taskDescriptionEdit);
            dateEdit = dialogView.findViewById(R.id.datePickerEdit);
            Button buttonAdd = dialogView.findViewById(R.id.newTaskBtn);


            // Crear el AlertDialog
            AlertDialog dialog = builder.create();

            dateEdit.setOnClickListener(v -> showDatePicker());
            // Cuando se pulsa añadir
            buttonAdd.setOnClickListener(view -> {
                String name = taskNameEdit.getText().toString();
                String description = descriptionEdit.getText().toString();
                if (!name.isEmpty() && !description.isEmpty()) {
                    // Convertir la fecha y hora seleccionadas en un objeto Calendar
                    Calendar deadline = Calendar.getInstance();
                    deadline.set(selectedYear, selectedMonth, selectedDay);

                    // Crear la nueva tarea
                    Task newTask = new Task(0, name, description, deadline.getTime(), username);
                    taskViewModel.addTask(newTask);

                    // Programar una notificación para un día antes de la fecha límite
                    scheduleNotification(newTask);

                    // Cerrar el diálogo
                    dialog.dismiss();
                }
            });

            // Mostrar el dialogo con todas las opciones
            builder.create().show();
        }

        public void showDatePicker() {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            // Almacena la fecha seleccionada.
                            selectedYear = year;
                            selectedMonth = month;
                            selectedDay = dayOfMonth;

                            // Actualiza el EditText con la fecha seleccionada.
                            dateEdit.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        }
                    },
                    year,
                    month,
                    dayOfMonth
            );
            datePickerDialog.show();
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
