package com.giselletavares.c0744277_finalproject.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giselletavares.c0744277_finalproject.R;
import com.giselletavares.c0744277_finalproject.models.Task;
import com.giselletavares.c0744277_finalproject.utils.Formatting;
import com.giselletavares.c0744277_finalproject.utils.IDataOperations;

import java.util.Calendar;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.TasksViewHolder> {

    private List<Task> mTaskList;
    private Context mContext;
    private Dialog mDialog;
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

        mDialog = new Dialog(mContext);
        mDialog.setContentView(R.layout.dialog_task_detail);

        tasksViewHolder.mLinearLayout_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView lblPriority = mDialog.findViewById(R.id.lblPriority_dialog);
                TextView lblTask = mDialog.findViewById(R.id.lblTask_dialog);
                TextView lblDuration = mDialog.findViewById(R.id.lblDuration_dialog);
                TextView lblDueDate = mDialog.findViewById(R.id.lblDueDate_dialog);
                TextView lblNotesTitle = mDialog.findViewById(R.id.lblNotesTitleField_dialog);
                TextView lblNotes = mDialog.findViewById(R.id.lblNotes_dialog);
                Button btnEditTask = mDialog.findViewById(R.id.btnEditTask_dialog);
                ImageButton btnDeleteTask = mDialog.findViewById(R.id.btnDeleteTask_dialog);

                switch (mTaskList.get(tasksViewHolder.getAdapterPosition()).getPriority()) {
                    case '0':
                        lblPriority.setText("");
                        lblPriority.setVisibility(View.GONE);
                        break;
                    case '1':
                        lblPriority.setVisibility(View.VISIBLE);
                        lblPriority.setText("!");
                        lblPriority.setTextColor(Color.parseColor("#303f9f"));
                        lblPriority.setBackgroundColor(Color.parseColor("#ffefd6"));
                        break;
                    case '2':
                        lblPriority.setVisibility(View.VISIBLE);
                        lblPriority.setText("!!");
                        lblPriority.setTextColor(Color.parseColor("#303f9f"));
                        lblPriority.setBackgroundColor(Color.parseColor("#ffd494"));
                        break;
                    case '3':
                        lblPriority.setVisibility(View.VISIBLE);
                        lblPriority.setText("!!!");
                        lblPriority.setTextColor(Color.parseColor("#303f9f"));
                        lblPriority.setBackgroundColor(Color.parseColor("#ff9800"));
                        break;
                }

                lblTask.setText(mTaskList.get(tasksViewHolder.getAdapterPosition()).getTaskName());

                if (mTaskList.get(tasksViewHolder.getAdapterPosition()).getDuration() != null) {
                    lblDuration.setVisibility(View.VISIBLE);
                    lblDuration.setText("Duration: " + formatting.getDurationFormatter(mTaskList.get(tasksViewHolder.getAdapterPosition()).getDuration()));
                } else {
                    lblDuration.setText("");
                    lblDuration.setVisibility(View.GONE);
                }

                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);

                if (mTaskList.get(tasksViewHolder.getAdapterPosition()).getDueDate() != null) {
                    lblDueDate.setVisibility(View.VISIBLE);
                    lblDueDate.setText("Due Date: " + formatting.getDateShortFormatter(mTaskList.get(tasksViewHolder.getAdapterPosition()).getDueDate()));
                    lblDueDate.setTextColor(Color.parseColor("#212121"));
                    lblDueDate.setBackgroundColor(Color.TRANSPARENT);
                    if(mTaskList.get(tasksViewHolder.getAdapterPosition()).getDueDate().compareTo(today.getTime()) < 0){
                        lblDueDate.setTextColor(Color.RED);
                        lblDueDate.setBackgroundColor(Color.TRANSPARENT);
                    } else if(mTaskList.get(tasksViewHolder.getAdapterPosition()).getDueDate().compareTo(today.getTime()) == 0) {
                        lblDueDate.setText("Due Date: It's Today! " + formatting.getDateShortFormatter(mTaskList.get(tasksViewHolder.getAdapterPosition()).getDueDate()));
                        lblDueDate.setTextColor(Color.parseColor("#FFFFFF"));
                        lblDueDate.setBackgroundColor(Color.parseColor("#3f51b5"));
                    }
                } else {
                    lblDueDate.setText("");
                    lblDueDate.setVisibility(View.GONE);
                }

                if(mTaskList.get(tasksViewHolder.getAdapterPosition()).getNotes() != null) {
                    lblNotes.setVisibility(View.VISIBLE);
                    lblNotesTitle.setVisibility(View.VISIBLE);
                    lblNotesTitle.setText("Notes: ");
                    lblNotes.setText(mTaskList.get(tasksViewHolder.getAdapterPosition()).getNotes());
                } else {
                    lblNotes.setText("");
                    lblNotes.setVisibility(View.GONE);
                    lblNotesTitle.setText("");
                    lblNotesTitle.setVisibility(View.GONE);
                }

                btnEditTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // go to edit activity
                    }
                });

                btnDeleteTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                mDialog.show();
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
                break;
            case '2':
                tasksViewHolder.mLblPriority.setText("!!");
                tasksViewHolder.mLblPriority.setTextColor(Color.parseColor("#303f9f"));
                tasksViewHolder.mLblPriority.setBackgroundColor(Color.parseColor("#ffd494"));
                break;
            case '3':
                tasksViewHolder.mLblPriority.setText("!!!");
                tasksViewHolder.mLblPriority.setTextColor(Color.parseColor("#303f9f"));
                tasksViewHolder.mLblPriority.setBackgroundColor(Color.parseColor("#ff9800"));
                break;
        }

        tasksViewHolder.mLblTask.setText(currentTask.getTaskName());

        if (currentTask.getDuration() != null) {
            tasksViewHolder.mLblDuration.setText(formatting.getDurationFormatter(currentTask.getDuration()));
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

        //Set it to null to erase an existing listener from a recycled view.
        tasksViewHolder.mIsDone.setOnCheckedChangeListener(null);

        if (currentTask.getStatus()) {
            tasksViewHolder.mIsDone.setChecked(true);
//            tasksViewHolder.mIsDone.setEnabled(false);
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
//                notifyDataSetChanged(); // get error all the time
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

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);

            mLinearLayout_task = itemView.findViewById(R.id.task_item_id);
            mIsDone = itemView.findViewById(R.id.chkIsDone);
            mLblPriority = itemView.findViewById(R.id.lblPriority);
            mLblTask = itemView.findViewById(R.id.lblTask);
            mLblDuration = itemView.findViewById(R.id.lblDuration);
            mLblDueDate = itemView.findViewById(R.id.lblDueDate);
        }
    }

}
