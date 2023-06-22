package com.example.taskmanager.task_fragments;

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
import com.example.taskmanager.utils.FilterUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks = new ArrayList<>();
    private List<Task> filteredTasks = new ArrayList<>();
    private TaskClickListener listener;
    private TaskRepository taskRepository;

    private FilterUtils.FilterType currentFilter = FilterUtils.FilterType.NONE;

    private Handler handler = new Handler();
    private Runnable updateTaskStatusRunnable = new Runnable() {
        @Override
        public void run() {
            Date currentDate = new Date();
            for (Task task : tasks) {
                if (task.getStatus() == Task.Status.IN_PROGRESS && currentDate.after(task.getDeadline())) {
                    task.setStatus(Task.Status.FAILED);
                    taskRepository.update(task);
                }
                task.setRemainingTime(calculateRemainingTime(task.getDeadline()));
                taskRepository.update(task);
            }

            if (currentFilter == FilterUtils.FilterType.NONE) {
                filteredTasks = new ArrayList<>(tasks); // Restaurar la lista completa de tareas
            } else {
                filteredTasks = FilterUtils.applyFilter(tasks, currentFilter); // Recalcular la lista filtrada
            }

            notifyDataSetChanged(); // Notificar al adaptador de los cambios en la lista

            handler.postDelayed(this, 5000); // 5000 milliseconds = 5 segundos
        }
    };


    public TaskAdapter(TaskClickListener listener, TaskRepository taskRepository) {
        this.listener = listener;
        this.taskRepository = taskRepository;
        handler.post(updateTaskStatusRunnable);
    }

    public interface TaskClickListener {
        void onTaskClick(Task task);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = filteredTasks.get(position); // Usar la lista filtrada en lugar de la lista completa
        holder.bind(task);
    }


    @Override
    public int getItemCount() {
        return filteredTasks.size(); // Usar el tamaño de la lista filtrada en lugar de la lista completa
    }

    public void setTasks(List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
        applyFilter(currentFilter); // Aplicar el filtro actual a la nueva lista de tareas
    }


    public void applyFilter(FilterUtils.FilterType filterType) {
        currentFilter = filterType;
        filteredTasks.clear();
        if (filterType == FilterUtils.FilterType.NONE) {
            filteredTasks.addAll(tasks);
        }else{
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

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, statusTextView, deadlineTextView, remainingTimeTextView, descriptionTextView;
        ConstraintLayout taskBackground;

        // Recoge los componentes de la tarea
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.task_name);
            deadlineTextView = itemView.findViewById(R.id.task_deadline);
            statusTextView = itemView.findViewById(R.id.task_status);
            descriptionTextView = itemView.findViewById(R.id.task_description);
            remainingTimeTextView = itemView.findViewById(R.id.task_remainingTime);
            taskBackground = itemView.findViewById(R.id.task_background);
        }

        // Se inicializan los componentes de la tarea
        public void bind(Task task) {
            nameTextView.setText(task.getName());
            deadlineTextView.setText(formatDeadline(task.getDeadline()));
            statusTextView.setText(task.getStatus().name()); // Mostrar el nombre del estado
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

    private String calculateRemainingTime(Date deadline) {
        Date currentDate = new Date();
        long differenceMillis = deadline.getTime() - currentDate.getTime();
        long differenceMinutes = TimeUnit.MILLISECONDS.toMinutes(differenceMillis);
        long minutesInDay = TimeUnit.DAYS.toMinutes(1);
        long remainingDays = differenceMinutes / minutesInDay;
        long remainingHours = (differenceMinutes % minutesInDay) / 60;
        long remainingMinutes = differenceMinutes % 60;
        String remainingTime;

        if (differenceMillis <= 0) {
            remainingTime = "Se ha acabado el tiempo";
        } else if (remainingDays > 0) {
            remainingTime = "Quedan: " + remainingDays + " días";
        } else if (remainingHours > 0) {
            remainingTime = "Quedan: " + remainingHours + " horas";
        } else {
            remainingTime = "Quedan: " + remainingMinutes + " minutos";
        }
        return remainingTime;
    }
}
