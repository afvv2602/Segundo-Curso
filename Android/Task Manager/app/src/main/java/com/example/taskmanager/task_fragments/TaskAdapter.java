package com.example.taskmanager.task_fragments;


public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> tasks = new ArrayList<>();

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.nameTextView.setText(task.getName());
        holder.deadlineTextView.setText(task.getDeadline());
        // Hacer lo mismo para otros campos de la tarea
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView deadlineTextView;
        // Otros campos...

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.task_name);
            deadlineTextView = itemView.findViewById(R.id.task_deadline);
            // Inicializar otros campos...
        }
    }
}
