package com.giselletavares.c0744277_finalproject.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.giselletavares.c0744277_finalproject.R;
import com.giselletavares.c0744277_finalproject.activities.HomeActivity;
import com.giselletavares.c0744277_finalproject.fragments.TodayFragment;
import com.giselletavares.c0744277_finalproject.models.AppDatabase;
import com.giselletavares.c0744277_finalproject.models.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationByApp extends BroadcastReceiver {

    public static AppDatabase sAppDatabase;
    private FirebaseAuth mAuth;

    @Override
    public void onReceive(Context context, Intent intent) {

        // GETTING USER INFO
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // DATABASE
        sAppDatabase = Room.databaseBuilder(context, AppDatabase.class, "idoitdb")
                .allowMainThreadQueries() // it will allow the database works on the main thread
                .fallbackToDestructiveMigration() // because i wont implement now migrations
                .build();

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        List<Task> tasks = TodayFragment.sAppDatabase.mTaskDAO().getTasksReminderForToday(currentUser.getUid(), false, today.getTime());

        String tasksName = "";
        for(Task task : tasks){
            tasksName += task.getTaskName() + "\n";
        }

        String CHANNEL_ID = "task_reminder";
        CharSequence name = "task_channel";
        String Description = "Task Reminder channel";

        Formatting formatting = new Formatting();
        Date currentDateTime = new Date();
        String nId = formatting.getDateTimeForNotificationIdFormatter(currentDateTime);
        int NOTIFICATION_ID = Integer.parseInt(nId);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(true);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        Intent resultIntent = new Intent(context, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Task Reminder")
                .setContentText(tasksName)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(tasksName))
                .setSmallIcon(R.drawable.ic_action_reminder)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setColor(Color.RED)
                .addAction(R.drawable.ic_launcher_foreground, "I Do It", pendingIntent);


        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }

    }

}
