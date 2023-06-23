package com.example.taskmanager.task_fragments;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.db.task.Task;
import com.example.taskmanager.db.task.TaskRepository;
import com.example.taskmanager.utils.DuplicateUtils;
import com.example.taskmanager.utils.FilterUtils;
import com.example.taskmanager.utils.NotificationUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks = new ArrayList<>();
    private List<Task> filteredTasks = new ArrayList<>();
    private TaskClickListener listener;
    private TaskRepository taskRepository;
    private FilterUtils.FilterType currentFilter = FilterUtils.FilterType.NONE;
    private Context context;
    private Handler handler = new Handler();

    public TaskAdapter(Context context, TaskClickListener listener, TaskRepository taskRepository) {
        this.context = context;
        this.listener = listener;
        this.taskRepository = taskRepository;
        handler.post(updateTaskStatusRunnable);
    }

    // Este hilo se encarga de tener actualizadas las tareas y notificar si alguna ha sido fallida
    private Runnable updateTaskStatusRunnable = new Runnable() {
        @Override
        public void run() {
            Date currentDate = new Date();
            for (Task task : tasks) {
                // Programar una notificacion para indicar que la tarea ha fallado
                if (task.getStatus() == Task.Status.IN_PROGRESS && currentDate.after(task.getDeadline())) {
                    task.setStatus(Task.Status.FAILED);
                    taskRepository.update(task);
                    NotificationUtils.scheduleNotification(context, task, 0);
                }
                task.setRemainingTime(DuplicateUtils.remainingTime(task.getDeadline()));
                taskRepository.update(task);
            }
            if (currentFilter == FilterUtils.FilterType.NONE) {
                filteredTasks = new ArrayList<>(tasks);
            } else {
                filteredTasks = FilterUtils.applyFilter(tasks, currentFilter);
            }
            notifyDataSetChanged();

            handler.postDelayed(this, 10000);
        }
    };


    // Manejar los clicks
    public interface TaskClickListener {
        void onTaskClick(Task task);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    // Vincula los datos de la tareas al recycler view
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = filteredTasks.get(position);
        holder.bind(task);
    }

    @Override
    public int getItemCount() {
        return filteredTasks.size(); // Usar el tamaño de la lista filtrada en lugar de la lista completa
    }

    // Establece la lista completa de tareas y aplica el filtro actual a la nueva lista
    public void setTasks(List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
        applyFilter(currentFilter);
    }

    public void applyFilter(FilterUtils.FilterType filterType) {
        currentFilter = filterType;
        filteredTasks.clear();
        if (filterType == FilterUtils.FilterType.NONE) {
            filteredTasks.addAll(tasks);
        } else {
            for (Task task : tasks) {
                boolean sw = true;
                switch (filterType) {
                    case COMPLETED:
                        sw = task.getStatus() == Task.Status.COMPLETED;
                        break;
                    case INCOMPLETE:
                        sw = task.getStatus() == Task.Status.IN_PROGRESS;
                        break;
                    case FAILED:
                        sw = task.getStatus() == Task.Status.FAILED;
                        break;
                    case HIGH_PRIORITY:
                        sw = task.getTier() == Task.Tier.HIGH;
                        break;
                    case MID_PRIORITY:
                        sw = task.getTier() == Task.Tier.DEFAULT;
                        break;
                    case LOW_PRIORITY:
                        sw = task.getTier() == Task.Tier.LOW;
                        break;
                }
                if (sw) {
                    filteredTasks.add(task);
                }
            }
        }
        notifyDataSetChanged(); // Notificar al adaptador de los cambios en la lista filtrada
    }

    // Clase que representa una cada elemento tarea en el recycler
    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, statusTextView, deadlineTextView, remainingTimeTextView, descriptionTextView;
        ConstraintLayout taskBackground;

        // Inicializa la vista
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.task_name);
            deadlineTextView = itemView.findViewById(R.id.task_deadline);
            statusTextView = itemView.findViewById(R.id.task_status);
            descriptionTextView = itemView.findViewById(R.id.task_description);
            remainingTimeTextView = itemView.findViewById(R.id.task_remainingTime);
            taskBackground = itemView.findViewById(R.id.task_background);
        }

        // Establece los datos en la tarea
        public void bind(Task task) {
            nameTextView.setText(task.getName());
            deadlineTextView.setText(formatDeadline(task.getDeadline()));
            statusTextView.setText(task.getStatus().name());
            descriptionTextView.setText(task.getDescription());
            remainingTimeTextView.setText(task.getRemainingTime());
            itemView.setOnClickListener(v -> listener.onTaskClick(task));
            setTaskBackground(task);
        }

        private void setTaskBackground(Task task) {
            int backgroundResId;
            switch (task.getStatus()) {
                case COMPLETED:
                    backgroundResId = R.drawable.tasks_card_completed;
                    break;
                case FAILED:
                    backgroundResId = R.drawable.tasks_card_failed;
                    break;
                case IN_PROGRESS:
                default:
                    backgroundResId = getTaskTierBackground(task.getTier());
                    break;
            }
            taskBackground.setBackgroundResource(backgroundResId);
        }

        private int getTaskTierBackground(Task.Tier tier) {
            int backgroundResId;
            switch (tier) {
                case HIGH:
                    backgroundResId = R.drawable.tasks_card_important;
                    break;
                case LOW:
                    backgroundResId = R.drawable.tasks_card_low;
                    break;
                case DEFAULT:
                default:
                    backgroundResId = R.drawable.tasks_card;
                    break;
            }
            return backgroundResId;
        }

        private String formatDeadline(Date deadline) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM", Locale.getDefault());
            return sdf.format(deadline);
        }
    }
}
