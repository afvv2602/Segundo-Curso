package com.example.taskmanager.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.db.task.Task;
import com.example.taskmanager.db.task.TaskViewModel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TaskDialogUtils {
    public static void showTaskDialog(Context context, Task task, TaskViewModel taskViewModel) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.fragment_task, null);
        AlertDialog dialog = createDialog(context, dialogView);
        initTaskDialog(context, dialog, dialogView, task,taskViewModel);
    }

    private static AlertDialog createDialog(Context context, View dialogView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();

        return dialog;
    }

    private static void initTaskDialog(Context context, AlertDialog dialog, View dialogView, Task task,TaskViewModel taskViewModel) {
        TextView taskNameTextView = dialogView.findViewById(R.id.task_name);
        TextView taskDeadlineTextView = dialogView.findViewById(R.id.task_deadline);
        TextView taskDescriptionTextView = dialogView.findViewById(R.id.task_description);
        TextView taskTierTextView = dialogView.findViewById(R.id.task_tier);
        Button deleteButton = dialogView.findViewById(R.id.button);
        Button completeButton = dialogView.findViewById(R.id.button2);
        LinearLayout taskBackground = dialogView.findViewById(R.id.task_background);

        taskNameTextView.setText(task.getName());
        taskDeadlineTextView.setText(DuplicateUtils.remainingTime(task.getDeadline()));
        taskTierTextView.setText("Tier: " + task.getTier());
        taskDescriptionTextView.setText(task.getDescription());

        setDialogBackground(task, taskBackground, completeButton, deleteButton);

        deleteButton.setOnClickListener(view -> {
            NotificationUtils.cancelTaskNotifications(context, task);
            taskViewModel.delete(task);
            dialog.dismiss();
            NotificationUtils.showNotification(context, "Tarea eliminada", task.getName());
            NotificationUtils.playNotificationSound(context, false);
        });

        completeButton.setOnClickListener(view -> {
            Task.Status taskStatus = Task.Status.COMPLETED;
            NotificationUtils.cancelTaskNotifications(context, task);
            task.setStatus(taskStatus);
            taskViewModel.update(task);
            dialog.dismiss();
            NotificationUtils.showNotification(context, "Tarea Completada", task.getName());
            NotificationUtils.playNotificationSound(context, true);
        });
    }

    private static void setDialogBackground(Task task, LinearLayout taskBackground, Button completeButton, Button deleteButton) {
        int backgroundResource;
        int completeButtonResource;
        int deleteButtonResource;

        switch (task.getStatus()) {
            case COMPLETED:
                backgroundResource = R.drawable.tasks_card_completed;
                completeButtonResource = R.drawable.btn_completed;
                deleteButtonResource = R.drawable.btn_completed;
                break;
            case FAILED:
                backgroundResource = R.drawable.tasks_card_failed;
                completeButtonResource = R.drawable.btn_important;
                deleteButtonResource = R.drawable.btn_important;
                break;
            default:
                switch (task.getTier()) {
                    case HIGH:
                        backgroundResource = R.drawable.tasks_card_important;
                        completeButtonResource = R.drawable.btn_important;
                        deleteButtonResource = R.drawable.btn_important;
                        break;
                    case LOW:
                        backgroundResource = R.drawable.tasks_card_low;
                        completeButtonResource = R.drawable.btn_low;
                        deleteButtonResource = R.drawable.btn_low;
                        break;
                    default:
                        backgroundResource = R.drawable.tasks_card;
                        completeButtonResource = R.drawable.btn_default;
                        deleteButtonResource = R.drawable.btn_default;
                }
        }

        taskBackground.setBackgroundResource(backgroundResource);
        completeButton.setBackgroundResource(completeButtonResource);
        deleteButton.setBackgroundResource(deleteButtonResource);
    }

}
