package com.giselletavares.c0744277_finalproject.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.giselletavares.c0744277_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btnBack)
    ImageButton btnBack;
    @BindView(R.id.txtCurrentPassword)
    EditText txtCurrentPassword;
    @BindView(R.id.txtNewPassword)
    EditText txtNewPassword;
    @BindView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;
    @BindView(R.id.btnChangePassword)
    Button btnChangePassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

        // GETTING USER INFO
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(ChangePasswordActivity.this, LoginActivity.class));
        }

        btnBack.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                startActivity(new Intent(ChangePasswordActivity.this, HomeActivity.class));
                finish();
                break;

            case R.id.btnChangePassword:

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ChangePasswordActivity.this);

                mBuilder.setTitle("Error");
                if(txtCurrentPassword.getText().toString().isEmpty() || txtNewPassword.getText().toString().isEmpty() || txtConfirmPassword.getText().toString().isEmpty()) {
                    mBuilder.setMessage("The fields cannot be empty.");
                } else if(txtNewPassword.getText().toString().length() < 6) {
                    mBuilder.setMessage("Password must have 6 characters or more");
                } else if(!txtNewPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
                    mBuilder.setMessage("Password does not match with the Confirm Password");
                } else {
                    mAuth.signInWithEmailAndPassword(mAuth.getCurrentUser().getEmail(), txtCurrentPassword.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mAuth.getCurrentUser().updatePassword(txtNewPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(ChangePasswordActivity.this, "Password Successfully changed", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(ChangePasswordActivity.this, SettingsActivity.class));
                                                    finish();
                                                } else {
                                                    mBuilder.setMessage("Password does not changed");
                                                }
                                            }
                                        });
                                    } else {
                                        mBuilder.setMessage("Current Password incorrect");
                                    }
                                }
                            });

                }

                mBuilder.setNegativeButton("Try again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog mDialogLabels = mBuilder.create();
                mDialogLabels.show();

                break;
        }
    }
}
