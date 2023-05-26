package com.example.taskmanager.task_fragments;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.R;
import com.example.taskmanager.db.task.Task;
import com.example.taskmanager.db.task.TaskRepository;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private LiveData<List<Task>> tasks;
    private TaskClickListener listener;
    private TaskRepository taskRepository;
    private Handler handler = new Handler();
    private Runnable updateTaskStatusRunnable = new Runnable() {
        @Override
        public void run() {
            Date currentDate = new Date();
            List<Task> ongoingTasks = tasks.getValue();  // Obtener la lista de tareas desde LiveData<List<Task>>
            for (Task task : ongoingTasks) {
                if (currentDate.after(task.getDeadline())) {
                    task.setStatus(2);
                    taskRepository.update(task);
                }
            }
            handler.postDelayed(this, 60000); // 60000 milliseconds = 1 minute
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
        List<Task> taskList = tasks.getValue();
        if (taskList != null) {
            Task task = taskList.get(position);
            holder.bind(task);
            if (task.getStatus() == 2) {
                holder.taskBackground.setBackgroundResource(R.drawable.tasks_card_failed);
            }
        }
    }


    @Override
    public int getItemCount() {
        List<Task> taskList = tasks.getValue();
        if (taskList != null) {
            return taskList.size();
        } else {
            return 0;
        }
    }

    public void setTasks(LiveData<List<Task>> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, statusTextView, deadlineTextView;
        ConstraintLayout taskBackground;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.task_name);
            deadlineTextView = itemView.findViewById(R.id.task_deadline);
            statusTextView = itemView.findViewById(R.id.task_status);
            taskBackground = itemView.findViewById(R.id.task_background);
        }

        public void bind(Task task) {
            nameTextView.setText(task.getName());
            deadlineTextView.setText(remainingTime(task.getDeadline()));
            statusTextView.setText(String.valueOf(task.getStatus()));
            itemView.setOnClickListener(v -> listener.onTaskClick(task));
            switch (task.getStatus()) {
                case 0:
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
                    break;
                case 1:
                    taskBackground.setBackgroundResource(R.drawable.tasks_card_completed);
                    break;
                case 2:
                    taskBackground.setBackgroundResource(R.drawable.tasks_card_failed);
                    break;
            }
        }

        private String remainingTime(Date deadline) {
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
}
