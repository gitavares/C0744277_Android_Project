package com.giselletavares.c0744277_finalproject.activities;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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
import butterknife.OnClick;

public class AddTaskActivity extends AppCompatActivity {

    public static AppDatabase sAppDatabase;
    private FirebaseAuth mAuth;

    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.txtTaskName)
    EditText txtTaskName;
    @BindView(R.id.txtLabelsSelected)
    EditText txtLabelsSelected;
    @BindView(R.id.lblDueDateSelected)
    TextView lblDueDateSelected;
    @BindView(R.id.btnSelectLabels)
    Button btnSelectLabels;
    @BindView(R.id.btnSelectDueDate)
    Button btnSelectDueDate;
    @BindView(R.id.btnSaveTask)
    Button btnSaveTask;
    @BindView(R.id.txtDuration)
    EditText txtDuration;
    @BindView(R.id.rbPriority)
    RadioGroup rbPriority;
    @BindView(R.id.txtNotes)
    EditText txtNotes;
    @BindView(R.id.swReminder)
    Switch swReminder;


    private DatePickerDialog mDatePickerDialog;

    private int mYear;
    private int mMonth;
    private int mDayOfMonth;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        ButterKnife.bind(this);

        // GETTING USER INFO
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            startActivity(new Intent(AddTaskActivity.this, LoginActivity.class));
        }

        // DATABASE
        sAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "idoitdb")
                .allowMainThreadQueries() // it will allow the database works on the main thread
                .fallbackToDestructiveMigration() // because I won't implement migrations now
                .build();

    }

    @OnClick({R.id.btnBack, R.id.btnSaveTask, R.id.btnSelectDueDate})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btnBack:
                startActivity(new Intent(AddTaskActivity.this, HomeActivity.class));
                finish();
                break;

            case R.id.btnSelectDueDate:
                mCalendar = Calendar.getInstance();
                mCalendar.add(Calendar.DAY_OF_YEAR, 1);
                mYear = mCalendar.get(Calendar.YEAR);
                mMonth = mCalendar.get(Calendar.MONTH);
                mDayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

                mDatePickerDialog = new DatePickerDialog(AddTaskActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                if(isValid) {

                    Formatting formatting = new Formatting();
                    Date currentDateTime = new Date();


                    final Task task = new Task();
                    task.set_id(formatting.getDateTimeForIdFormatter(currentDateTime));
                    task.setUserId(mAuth.getCurrentUser().getUid());
                    task.setTaskName(txtTaskName.getText().toString());

                    if(!lblDueDateSelected.getText().toString().isEmpty()){

                        // preparing Due Date
                        Date dueDate = currentDateTime;
                        try {
                            dueDate = new SimpleDateFormat("dd/MM/yyyy").parse(lblDueDateSelected.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        task.setDueDate(dueDate);

                        // only set a reminder if checked, if DueDate is not empty
                        final Date finalDueDate = dueDate;
                        swReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                task.setReminder(finalDueDate);
                            }
                        });
                    }

                    if(!txtDuration.getText().toString().isEmpty() && !txtDuration.getText().toString().equals("00:00")){
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
                            priority = 'N';
                            break;
                        case R.id.rbPriorityItemLow:
                            priority = 'L';
                            break;
                        case R.id.rbPriorityItemMedium:
                            priority = 'M';
                            break;
                        case R.id.rbPriorityItemHigh:
                            priority = 'H';
                            break;
                    }
                    task.setPriority(priority);

                    if(!txtNotes.getText().toString().isEmpty()){
                        task.setNotes(txtNotes.getText().toString());
                    }

                    task.setStatus(false);
                    task.setCreatedDate(currentDateTime);
                    task.setModifiedDate(currentDateTime);

                    AddTaskActivity.sAppDatabase.mTaskDAO().addTask(task);

                    ///////
                    // pending
                    // work on save labels and task
                    ///////

                    Toast.makeText(this, "Task saved", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddTaskActivity.this, HomeActivity.class));
                    finish();

                }

                break;
        }
    }

    private Boolean fieldsValidation(String taskName, String duration) {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(AddTaskActivity.this);
        String regexTimer = "^((?:[01]\\d|2[0-3]):[0-5]\\d$)";

        mBuilder.setTitle("Error");
        if(taskName.isEmpty()) {
            mBuilder.setMessage("The Task Description cannot be empty.");
        } else if(!duration.isEmpty() && !duration.matches(regexTimer)) {
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