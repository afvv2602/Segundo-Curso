package com.example.taskmanager.task_fragments;

public class TaskFragment extends Fragment {
    private TaskViewModel taskViewModel;
    private EditText nameEditText;
    private EditText deadlineEditText;
    // Otros campos...

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inicializar vistas y ViewModel
        nameEditText = // Inicializar EditText...
        deadlineEditText = // Inicializar EditText...
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        // Configurar onClickListener para el botón de agregar tarea
        addButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String deadline = deadlineEditText.getText().toString();
            // Obtener otros campos...

            // Crear un nuevo objeto Task
            Task task = new Task(name, deadline, // Otros campos...);
            taskViewModel.insertTask(task);
        });
    }
}
