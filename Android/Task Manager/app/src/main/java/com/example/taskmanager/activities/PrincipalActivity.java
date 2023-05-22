package com.example.taskmanager.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.db.task.TaskViewModel;
import com.example.taskmanager.task_fragments.TaskAdapter;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity {
    String username;
    TaskViewModel taskViewModel;
    RecyclerView taskRecyclerView;
    TaskAdapter taskAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity);
        username = getIntent().getStringExtra("username");
        TextView tv = findViewById(R.id.textView);
        taskRecyclerView = findViewById(R.id.task_recyclerview);

        if (username != null) {
            tv.setText("Bienvenido: "+username);
        }

        // Inicializar el RecyclerView y el adaptador con lista vacía
        taskAdapter = new TaskAdapter();
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setAdapter(taskAdapter);

        // Inicializar el TaskViewModel y observar los cambios en la lista de tareas
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getUserTasks(username).observe(this, tasks -> {
            // Actualizar la lista de tareas en el adaptador cuando haya cambios
            taskAdapter.setTasks(tasks);
        });
    }
}
