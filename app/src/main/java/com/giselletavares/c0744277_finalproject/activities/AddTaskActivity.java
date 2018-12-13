package com.giselletavares.c0744277_finalproject.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
import com.giselletavares.c0744277_finalproject.utils.NotificationByApp;
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
    @BindView(R.id.lblDueDateSelected)
    TextView lblDueDateSelected;
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

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    Formatting formatting;
    Date currentDateTime;

    Task task;

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

        task = new Task();

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

                    formatting = new Formatting();
                    currentDateTime = new Date();
                    String taskId = formatting.getDateTimeForIdFormatter(currentDateTime);

                    task.set_id(taskId);
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
                    }

                    if(!lblDueDateSelected.getText().toString().isEmpty() && swReminder.isChecked()) {
                        setReminder(task.getDueDate());
                        task.setReminder(task.getDueDate());
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

                    if(!txtNotes.getText().toString().isEmpty()){
                        task.setNotes(txtNotes.getText().toString());
                    }

                    task.setStatus(false);
                    task.setCreatedDate(currentDateTime);
                    task.setModifiedDate(currentDateTime);

                    AddTaskActivity.sAppDatabase.mTaskDAO().addTask(task);

                    Toast.makeText(this, "Task saved", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddTaskActivity.this, HomeActivity.class));
                    finish();

                }

                break;
        }
    }

    // refactor
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

    public void setReminder(Date taskDueDate) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(taskDueDate);
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);

        long futureInMillis = calendar.getTimeInMillis() + 1000 * 60; //Wake up the device to fire a one-time (non-repeating) alarm in one minute

        Intent intent = new Intent(AddTaskActivity.this, NotificationByApp.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        }

    }


}
