package com.example.taskmanager.utils;

import com.example.taskmanager.db.task.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterUtils {

    public static List<Task> applyFilter(List<Task> tasks, FilterType filterType) {
        switch (filterType) {
            case COMPLETED:
                return filterCompletedTasks(tasks);
            case INCOMPLETE:
                return filterIncompleteTasks(tasks);
            case HIGH_PRIORITY:
                return filterHighPriorityTasks(tasks);
            case LOW_PRIORITY:
                return filterLowPriorityTasks(tasks);
            case MID_PRIORITY:
                return filterMidPriorityTasks(tasks);
            case FAILED:
                return filterFailedTasks(tasks);
            default:
                return tasks;
        }
    }


    private static List<Task> filterCompletedTasks(List<Task> tasks) {
        List<Task> filtered = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getStatus() == Task.Status.COMPLETED) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    private static List<Task> filterIncompleteTasks(List<Task> tasks) {
        List<Task> filtered = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getStatus() == Task.Status.IN_PROGRESS) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    private static List<Task> filterHighPriorityTasks(List<Task> tasks) {
        List<Task> filtered = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTier() == Task.Tier.HIGH) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    private static List<Task> filterLowPriorityTasks(List<Task> tasks) {
        List<Task> filtered = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTier() == Task.Tier.LOW) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    private static List<Task> filterMidPriorityTasks(List<Task> tasks) {
        List<Task> filtered = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTier() == Task.Tier.DEFAULT) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    private static List<Task> filterFailedTasks(List<Task> tasks) {
        List<Task> filtered = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getStatus() == Task.Status.FAILED) {
                filtered.add(task);
            }
        }
        return filtered;
    }



    public enum FilterType {
        NONE, COMPLETED, INCOMPLETE,FAILED, HIGH_PRIORITY,MID_PRIORITY, LOW_PRIORITY
    }

}
