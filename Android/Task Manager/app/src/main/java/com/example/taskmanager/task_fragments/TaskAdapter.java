package com.example.taskmanager.task_fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.db.task.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Adaptador para el RecyclerView que muestra las tareas
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks = new ArrayList<>();
    private TaskClickListener listener;

    // Interfaz para el click en las tareas
    public interface TaskClickListener {
        void onTaskClick(Task task);
    }

    // Constructor del adaptador
    public TaskAdapter(TaskClickListener listener) {
        this.listener = listener;
    }

    // Infla la vista para cada item de la lista
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    // Vincula los datos con la vista
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);  // call bind method here
    }

    // Retorna el numero total de tareas
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    // Actualiza la lista de tareas y notifica al adaptador
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    // Contenedor de vistas para cada item de la lista
    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView deadlineTextView;
        ConstraintLayout taskBackground;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.task_name);
            deadlineTextView = itemView.findViewById(R.id.task_deadline);
        }

        public void bind(Task task) {
            nameTextView.setText(task.getName());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String dateString = format.format(task.getDeadline());
            deadlineTextView.setText(dateString);
            taskBackground = itemView.findViewById(R.id.task_background);
            itemView.setOnClickListener(v -> listener.onTaskClick(task));
            switch (task.getTier()) {
                case "Important":
                    taskBackground.setBackgroundResource(R.drawable.tasks_card_important);
                    break;
                case "Low":
                    taskBackground.setBackgroundResource(R.drawable.tasks_card_low);
                    break;
                default:
                    taskBackground.setBackgroundResource(R.drawable.tasks_card);
                    break;
            }
            if (task.getStatus()){
                taskBackground.setBackgroundResource(R.drawable.tasks_card_completed);
            }
        }
    }
}
