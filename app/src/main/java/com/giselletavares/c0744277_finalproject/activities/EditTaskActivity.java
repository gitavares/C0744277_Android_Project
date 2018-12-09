package com.giselletavares.c0744277_finalproject.activities;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.giselletavares.c0744277_finalproject.R;
import com.giselletavares.c0744277_finalproject.models.AppDatabase;
import com.giselletavares.c0744277_finalproject.models.Task;
import com.giselletavares.c0744277_finalproject.utils.Formatting;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTaskActivity extends AppCompatActivity implements View.OnClickListener {

    public static AppDatabase sAppDatabase;

    private FirebaseAuth mAuth;

    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.btnSaveTask)
    Button btnSaveTask;
    @BindView(R.id.txtTaskName)
    EditText txtTaskName;
    @BindView(R.id.lblDueDateSelected)
    TextView lblDueDateSelected;
    @BindView(R.id.btnSelectDueDate)
    Button btnSelectDueDate;
    @BindView(R.id.txtDuration)
    EditText txtDuration;
    @BindView(R.id.rbPriorityItemNone)
    RadioButton rbPriorityItemNone;
    @BindView(R.id.rbPriorityItemLow)
    RadioButton rbPriorityItemLow;
    @BindView(R.id.rbPriorityItemMedium)
    RadioButton rbPriorityItemMedium;
    @BindView(R.id.rbPriorityItemHigh)
    RadioButton rbPriorityItemHigh;
    @BindView(R.id.rbPriority)
    RadioGroup rbPriority;
    @BindView(R.id.txtNotes)
    EditText txtNotes;
    @BindView(R.id.swReminder)
    Switch swReminder;
    @BindView(R.id.chkIsDone)
    CheckBox chkIsDone;
    @BindView(R.id.btnDeleteTask)
    ImageButton btnDeleteTask;

    private DatePickerDialog mDatePickerDialog;

    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private Calendar mCalendar;

    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("taskId")) {

            // GETTING USER INFO
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                startActivity(new Intent(EditTaskActivity.this, LoginActivity.class));
            }

            // DATABASE
            sAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "idoitdb")
                    .allowMainThreadQueries() // it will allow the database works on the main thread
                    .fallbackToDestructiveMigration() // because i wont implement now migrations
                    .build();

            task = EditTaskActivity.sAppDatabase.mTaskDAO().getTaskById(getIntent().getStringExtra("taskId"));

            Formatting formatting = new Formatting();

            txtTaskName.setText(task.getTaskName());

            if(task.getDuration() != null) {
                txtDuration.setText(formatting.getDurationFormatter(task.getDuration()));
            }

            if(task.getDueDate() != null) {
                lblDueDateSelected.setText(formatting.getDateMediumFormatter(task.getDueDate()));
            }

            txtNotes.setText(task.getNotes());

            switch (task.getPriority()) {
                case '0':
                    rbPriorityItemNone.setChecked(true);
                    break;
                case '1':
                    rbPriorityItemLow.setChecked(true);
                    break;
                case '2':
                    rbPriorityItemMedium.setChecked(true);
                    break;
                case '3':
                    rbPriorityItemHigh.setChecked(true);
                    break;
            }

            if (task.getReminder() != null) {
                swReminder.setChecked(true);
            }

            if(task.getStatus()){
                chkIsDone.setChecked(true);
            }

            btnBack.setOnClickListener(this);
            btnSaveTask.setOnClickListener(this);
            btnSelectDueDate.setOnClickListener(this);
            btnDeleteTask.setOnClickListener(this);

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnBack:
                startActivity(new Intent(EditTaskActivity.this, HomeActivity.class));
                finish();
                break;

            case R.id.btnDeleteTask:
//                Task taskToDelete = new Task();
//                task.set_id(getIntent().getStringExtra("taskId"));
                task.set_id(task.get_id());
                EditTaskActivity.sAppDatabase.mTaskDAO().deleteTask(task);

                Toast.makeText(this, "Task deleted", Toast.LENGTH_LONG).show();

                startActivity(new Intent(EditTaskActivity.this, HomeActivity.class));
                finish();
                break;

            case R.id.btnSelectDueDate:
                mCalendar = Calendar.getInstance();
                mCalendar.add(Calendar.DAY_OF_YEAR, 1);
                mYear = mCalendar.get(Calendar.YEAR);
                mMonth = mCalendar.get(Calendar.MONTH);
                mDayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

                mDatePickerDialog = new DatePickerDialog(EditTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        lblDueDateSelected.setText(day + "/" + (month + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDayOfMonth);
                mDatePickerDialog.getDatePicker().setMinDate(mCalendar.getTimeInMillis());
                mDatePickerDialog.show();
                break;

            case R.id.btnSaveTask:

                Boolean isValid = fieldsValidation(txtTaskName.getText().toString(), txtDuration.getText().toString());

                if (isValid) {

                    Date currentDateTime = new Date();

//                    final Task task = new Task();
//                    task.set_id(getIntent().getStringExtra("taskId"));
                    task.set_id(task.get_id());
                    task.setUserId(mAuth.getCurrentUser().getUid());
                    task.setTaskName(txtTaskName.getText().toString());

                    // preparing Due Date
                    Date dueDate = currentDateTime;

                    if (!lblDueDateSelected.getText().toString().isEmpty()) {
                        try {
                            dueDate = new SimpleDateFormat("dd/MM/yyyy").parse(lblDueDateSelected.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        task.setDueDate(dueDate);
                    }

                    // only set a reminder if checked, if DueDate is not empty
                    swReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                            if(!lblDueDateSelected.getText().toString().isEmpty() && isChecked) {
                                Log.d("===== finalDueDate: ", task.getDueDate().toString());
                                task.setReminder(task.getDueDate());
                            } else {
                                Log.d("===== Not saving: ", task.getDueDate().toString());
                                task.setReminder(null);
                            }
                        }
                    });

                    if (!txtDuration.getText().toString().isEmpty() && !txtDuration.getText().toString().equals("00:00")) {
                        Date duration = currentDateTime;
                        try {
                            duration = new SimpleDateFormat("HH:mm").parse(txtDuration.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        task.setDuration(duration);
                    }

                    Character priority = null;
                    switch (rbPriority.getCheckedRadioButtonId()) {
                        case R.id.rbPriorityItemNone:
                            priority = '0';
                            break;
                        case R.id.rbPriorityItemLow:
                            priority = '1';
                            break;
                        case R.id.rbPriorityItemMedium:
                            priority = '2';
                            break;
                        case R.id.rbPriorityItemHigh:
                            priority = '3';
                            break;
                    }
                    task.setPriority(priority);

                    if (!txtNotes.getText().toString().isEmpty()) {
                        task.setNotes(txtNotes.getText().toString());
                    }

                    chkIsDone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                            if(isChecked) {
                                task.setStatus(true);
                            } else {
                                task.setStatus(false);
                            }
                        }
                    });

                    task.setCreatedDate(task.getCreatedDate());
                    task.setModifiedDate(currentDateTime);

                    EditTaskActivity.sAppDatabase.mTaskDAO().updateTask(task);

                    Toast.makeText(this, "Task saved", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(EditTaskActivity.this, HomeActivity.class));
                    finish();
                }
                break;
        }
    }

    // refactor
    private Boolean fieldsValidation(String taskName, String duration) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditTaskActivity.this);
        String regexTimer = "^((?:[01]\\d|2[0-3]):[0-5]\\d$)";

        mBuilder.setTitle("Error");
        if (taskName.isEmpty()) {
            mBuilder.setMessage("The Task Description cannot be empty.");
        } else if (!duration.isEmpty() && !duration.matches(regexTimer)) {
            mBuilder.setMessage("The duration is not valid. Try set the HH:MM");
        } else {
            return true;
        }

        mBuilder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog mDialogLabels = mBuilder.create();
        mDialogLabels.show();

        return false;
    }
}
