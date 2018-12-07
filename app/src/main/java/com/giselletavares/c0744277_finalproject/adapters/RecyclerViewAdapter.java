package com.giselletavares.c0744277_finalproject.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.giselletavares.c0744277_finalproject.R;
import com.giselletavares.c0744277_finalproject.models.Task;
import com.giselletavares.c0744277_finalproject.utils.Formatting;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TasksViewHolder> {

    private List<Task> mTaskList;
    private Context mContext;

    public RecyclerViewAdapter(List<Task> taskList, Context context) {
        mTaskList = taskList;
        mContext = context;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View mView;
        mView = LayoutInflater.from(mContext).inflate(R.layout.item_task_list, viewGroup, false);

        TasksViewHolder tasksViewHolder = new TasksViewHolder(mView);

        return tasksViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder tasksViewHolder, int position) {

        Formatting formatting = new Formatting();

        Task currentTask = mTaskList.get(position);

        if(currentTask.getStatus()) {
            tasksViewHolder.mIsDone.setChecked(true);
        }

        switch (currentTask.getPriority()){
            case 'N':
                tasksViewHolder.mLblPriority.setText("");
                tasksViewHolder.mLblPriority.setVisibility(View.GONE);
                break;
            case 'L':
                tasksViewHolder.mLblPriority.setText("!");
                break;
            case 'M':
                tasksViewHolder.mLblPriority.setText("!!");
                break;
            case 'H':
                tasksViewHolder.mLblPriority.setText("!!!");
                break;
        }

        tasksViewHolder.mLblTask.setText(currentTask.getTaskName());

        if(currentTask.getDuration() != null) {
            tasksViewHolder.mLblDuration.setText(formatting.getDurationFormatter(currentTask.getDuration()));
        } else {
            tasksViewHolder.mLblDuration.setText("");
            tasksViewHolder.mLblDuration.setVisibility(View.GONE);
        }

        if(currentTask.getDueDate() != null) {
            tasksViewHolder.mLblDueDate.setText(formatting.getDateShortFormatter(currentTask.getDueDate()));
        } else {
            tasksViewHolder.mLblDueDate.setText("");
            tasksViewHolder.mLblDueDate.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    public static class TasksViewHolder extends RecyclerView.ViewHolder {

        private CheckBox mIsDone;
        private TextView mLblPriority;
        private TextView mLblTask;
        private TextView mLblDuration;
        private TextView mLblDueDate;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            mIsDone = itemView.findViewById(R.id.chkIsDone);
            mLblPriority = itemView.findViewById(R.id.lblPriority);
            mLblTask = itemView.findViewById(R.id.lblTask);
            mLblDuration = itemView.findViewById(R.id.lblDuration);
            mLblDueDate = itemView.findViewById(R.id.lblDueDate);
        }
    }
}
