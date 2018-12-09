package com.giselletavares.c0744277_finalproject.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import com.giselletavares.c0744277_finalproject.R;
import com.giselletavares.c0744277_finalproject.models.AppDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    public static AppDatabase sAppDatabase;
    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.lblEmailUser)
    TextView lblEmailUser;
    @BindView(R.id.btnChangePassword)
    Button btnChangePassword;
    @BindView(R.id.swNotifyByApp)
    Switch swNotifyByApp;
    @BindView(R.id.swNotifyByEmail)
    Switch swNotifyByEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        // GETTING USER INFO
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
        }

        // DATABASE
        sAppDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "idoitdb")
                .allowMainThreadQueries() // it will allow the database works on the main thread
                .fallbackToDestructiveMigration() // because I won't implement migrations now
                .build();

        lblEmailUser.setText(mAuth.getCurrentUser().getEmail());

        swNotifyByApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

        swNotifyByEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });

    }

    @OnClick({R.id.btnBack, R.id.btnChangePassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                startActivity(new Intent(SettingsActivity.this, HomeActivity.class));
                finish();
                break;

            case R.id.btnChangePassword:
                startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
                finish();
                break;
        }
    }
}
