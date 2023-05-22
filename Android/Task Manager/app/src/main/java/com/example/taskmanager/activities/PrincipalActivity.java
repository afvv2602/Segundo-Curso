package com.example.taskmanager.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanager.R;
import com.example.taskmanager.db.user.UserViewModel;

import org.w3c.dom.Text;

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

        // Inicializar el TaskViewModel y observar los cambios en la lista de tareas
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        taskViewModel.getUserTasks(username).observe(this, tasks -> {
            // Actualizar la lista de tareas en el adaptador cuando haya cambios
            taskAdapter.setTasks(tasks);
        });

        // Inicializar el RecyclerView y el adaptador
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter();
        taskRecyclerView.setAdapter(taskAdapter);
    }
}

