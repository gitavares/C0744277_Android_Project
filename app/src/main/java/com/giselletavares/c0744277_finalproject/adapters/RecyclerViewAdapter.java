package com.giselletavares.c0744277_finalproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giselletavares.c0744277_finalproject.R;
import com.giselletavares.c0744277_finalproject.activities.EditTaskActivity;
import com.giselletavares.c0744277_finalproject.models.Task;
import com.giselletavares.c0744277_finalproject.utils.Formatting;
import com.giselletavares.c0744277_finalproject.utils.IDataOperations;

import java.util.Calendar;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TasksViewHolder> {

    private List<Task> mTaskList;
    private Context mContext;
    Formatting formatting = new Formatting();
    private IDataOperations mIDataOperations;

    public RecyclerViewAdapter(IDataOperations iDataOperations, List<Task> taskList, Context context) {
        this.mTaskList = taskList;
        this.mContext = context;
        this.mIDataOperations = iDataOperations;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View mView;
        mView = LayoutInflater.from(mContext).inflate(R.layout.item_task_list, viewGroup, false);
        final TasksViewHolder tasksViewHolder = new TasksViewHolder(mView);

        tasksViewHolder.mLinearLayout_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, EditTaskActivity.class);
                intent.putExtra("taskId", mTaskList.get(tasksViewHolder.getAdapterPosition()).get_id());
                mContext.startActivity(intent);

            }
        });

        return tasksViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TasksViewHolder tasksViewHolder, final int position) {

        final Task currentTask = mTaskList.get(position);

        switch (currentTask.getPriority()) {
            case '0':
                tasksViewHolder.mLblPriority.setText("");
                tasksViewHolder.mLblPriority.setVisibility(View.GONE);
                break;
            case '1':
                tasksViewHolder.mLblPriority.setText("!");
                tasksViewHolder.mLblPriority.setTextColor(Color.parseColor("#303f9f"));
                tasksViewHolder.mLblPriority.setBackgroundColor(Color.parseColor("#ffefd6"));
                tasksViewHolder.mLblPriority.setVisibility(View.VISIBLE);
                break;
            case '2':
                tasksViewHolder.mLblPriority.setText("!!");
                tasksViewHolder.mLblPriority.setTextColor(Color.parseColor("#303f9f"));
                tasksViewHolder.mLblPriority.setBackgroundColor(Color.parseColor("#ffd494"));
                tasksViewHolder.mLblPriority.setVisibility(View.VISIBLE);
                break;
            case '3':
                tasksViewHolder.mLblPriority.setText("!!!");
                tasksViewHolder.mLblPriority.setTextColor(Color.parseColor("#303f9f"));
                tasksViewHolder.mLblPriority.setBackgroundColor(Color.parseColor("#ff9800"));
                tasksViewHolder.mLblPriority.setVisibility(View.VISIBLE);
                break;
        }

        tasksViewHolder.mLblTask.setText(currentTask.getTaskName());

        if (currentTask.getDuration() != null) {
            tasksViewHolder.mLblDuration.setText(formatting.getDurationFormatter(currentTask.getDuration()));
            tasksViewHolder.mLblDuration.setVisibility(View.VISIBLE);
        } else {
            tasksViewHolder.mLblDuration.setText("");
            tasksViewHolder.mLblDuration.setVisibility(View.GONE);
        }

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        if (currentTask.getDueDate() != null) {
            tasksViewHolder.mLblDueDate.setText(formatting.getDateShortFormatter(currentTask.getDueDate()));
            tasksViewHolder.mLblDueDate.setTextColor(Color.parseColor("#212121"));
            tasksViewHolder.mLblDueDate.setVisibility(View.VISIBLE);
            tasksViewHolder.mLblDueDate.setBackgroundColor(Color.TRANSPARENT);
            if(currentTask.getDueDate().compareTo(today.getTime()) < 0){
                tasksViewHolder.mLblDueDate.setTextColor(Color.RED);
            } else if(currentTask.getDueDate().compareTo(today.getTime()) == 0) {
                tasksViewHolder.mLblDueDate.setText("It's Today! " + formatting.getDateShortFormatter(currentTask.getDueDate()));
                tasksViewHolder.mLblDueDate.setTextColor(Color.parseColor("#FFFFFF"));
                tasksViewHolder.mLblDueDate.setBackgroundColor(Color.parseColor("#3f51b5"));
            }
        } else {
            tasksViewHolder.mLblDueDate.setText("");
            tasksViewHolder.mLblDueDate.setVisibility(View.GONE);
        }

        if(currentTask.getReminder() != null) {
            tasksViewHolder.mImgReminder.setVisibility(View.VISIBLE);
        } else {
            tasksViewHolder.mImgReminder.setVisibility(View.GONE);
        }

        //Set it to null to erase an existing listener from a recycled view.
        tasksViewHolder.mIsDone.setOnCheckedChangeListener(null);

        if (currentTask.getStatus()) {
            tasksViewHolder.mIsDone.setChecked(true);
        } else {
            tasksViewHolder.mIsDone.setChecked(false);
        }
        tasksViewHolder.mIsDone.setTag(currentTask.get_id());

        tasksViewHolder.mIsDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mIDataOperations.update(currentTask.get_id(), !currentTask.getStatus());
                mTaskList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mTaskList.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTaskList.size();
    }

    public static class TasksViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLinearLayout_task;
        private CheckBox mIsDone;
        private TextView mLblPriority;
        private TextView mLblTask;
        private TextView mLblDuration;
        private TextView mLblDueDate;
        private ImageView mImgReminder;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            mLinearLayout_task = itemView.findViewById(R.id.task_item_id);
            mIsDone = itemView.findViewById(R.id.chkIsDone);
            mLblPriority = itemView.findViewById(R.id.lblPriority);
            mLblTask = itemView.findViewById(R.id.lblTask);
            mLblDuration = itemView.findViewById(R.id.lblDuration);
            mLblDueDate = itemView.findViewById(R.id.lblDueDate);
            mImgReminder = itemView.findViewById(R.id.imgReminder);
        }
    }

}
