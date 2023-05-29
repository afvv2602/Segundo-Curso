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

    public static List<Task> sortByPriority(List<Task> tasks) {
        List<Task> sortedTasks = new ArrayList<>(tasks);
        quickSortByPriority(sortedTasks, 0, sortedTasks.size() - 1);
        return sortedTasks;
    }

    private static void quickSortByPriority(List<Task> tasks, int low, int high) {
        if (low < high) {
            int pivotIndex = partitionByPriority(tasks, low, high);
            quickSortByPriority(tasks, low, pivotIndex - 1);
            quickSortByPriority(tasks, pivotIndex + 1, high);
        }
    }

    private static int partitionByPriority(List<Task> tasks, int low, int high) {
        Task pivot = tasks.get(high);
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (getPriorityValue(tasks.get(j).getTier()) < getPriorityValue(pivot.getTier())) {
                i++;
                Collections.swap(tasks, i, j);
            }
        }

        Collections.swap(tasks, i + 1, high);

        return i + 1;
    }

    private static int getPriorityValue(Task.Tier tier) {
        switch (tier) {
            case HIGH:
                return 2;
            case LOW:
                return 0;
            default:
                return 1;
        }
    }

    public enum FilterType {
        NONE, COMPLETED, INCOMPLETE, HIGH_PRIORITY, LOW_PRIORITY
    }
}
