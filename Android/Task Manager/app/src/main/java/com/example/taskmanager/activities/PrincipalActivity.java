package com.example.taskmanager.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.db.task.Task;
import com.example.taskmanager.db.task.TaskViewModel;
import com.example.taskmanager.task_fragments.TaskAdapter;

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
        EditText editTextDeadline = dialogView.findViewById(R.id.edittext_deadline);
        EditText editTextDescripcion = dialogView.findViewById(R.id.edittext_descripcion);
        Button buttonAdd = dialogView.findViewById(R.id.button_add);

        // Crear el AlertDialog
        AlertDialog dialog = builder.create();

        // Definir la acción del botón de añadir
        buttonAdd.setOnClickListener(view -> {
            String name = editTextName.getText().toString();
            String deadline = editTextDeadline.getText().toString();
            String description = editTextDescripcion.getText().toString();

            if (inputIsValid(name, deadline, description)) {
                Task newTask = new Task(0, name, description, deadline, username);
                taskViewModel.addTask(newTask);
                dialog.dismiss();
            }
        });

        // Mostrar el diálogo
        builder.create().show();
    }

    private boolean inputIsValid(String name, String deadline, String description) {
        // Verificar que ninguna de las entradas esté vacía
        if (name.isEmpty() || deadline.isEmpty() || description.isEmpty()) {
            return false;
        }

        // Aquí podrías agregar más validaciones si lo necesitas

        // Si pasó todas las validaciones, las entradas son válidas
        return true;
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
}
