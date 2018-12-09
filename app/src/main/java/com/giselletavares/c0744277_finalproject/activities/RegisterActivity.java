package com.giselletavares.c0744277_finalproject.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.giselletavares.c0744277_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnBack;
    private Button btnRegister;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        btnBack = findViewById(R.id.btnBack);
        btnRegister = findViewById(R.id.btnRegister);
        mEmail = findViewById(R.id.txtEmail);
        mPassword = findViewById(R.id.txtPassword);
        mConfirmPassword = findViewById(R.id.txtConfirmPassword);

        btnBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

    }

    private void createAccount(String email, String password, String confirmPassword) {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(RegisterActivity.this);
        String regexpEmail = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

        if(TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
            mBuilder.setMessage("Email/Password cannot be empty");
        } else if (password.length() < 6) {
            mBuilder.setMessage("Password must have 6 characters or more");
        } else if(!email.matches(regexpEmail)) {
            mBuilder.setMessage("Inform a valid email.");
        } else if (!password.equals(confirmPassword)) {
            mBuilder.setMessage("Password does not match with the Confirm Password");
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        mBuilder.setMessage("Sign up failed");
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
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnBack:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btnRegister:
                createAccount(mEmail.getText().toString(), mPassword.getText().toString(), mConfirmPassword.getText().toString());
                break;
        }

    }

}
