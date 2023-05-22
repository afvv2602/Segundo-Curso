package com.example.taskmanager.task_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.taskmanager.R;
import com.example.taskmanager.db.task.Task;
import com.example.taskmanager.db.task.TaskViewModel;

public class TaskFragment extends Fragment {
    private TaskViewModel taskViewModel;
    private EditText nameEditText;
    private EditText deadlineEditText;
    // Otros campos...

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inicializar vistas y ViewModel
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        nameEditText = view.findViewById(R.id.edittext_name);
        deadlineEditText = view.findViewById(R.id.edittext_deadline);
        // Inicializar otros EditText...
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Configurar onClickListener para el botón de agregar tarea
        Button addButton = view.findViewById(R.id.button_add);
        addButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String deadline = deadlineEditText.getText().toString();

        });

        return view;
    }
}
