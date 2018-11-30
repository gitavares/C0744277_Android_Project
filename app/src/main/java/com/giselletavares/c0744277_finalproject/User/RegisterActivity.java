package com.giselletavares.c0744277_finalproject.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.giselletavares.c0744277_finalproject.Home.HomeActivity;
import com.giselletavares.c0744277_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        if(TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {

            // error message
            Toast.makeText(RegisterActivity.this, "Email/Password cannot be empty", Toast.LENGTH_LONG).show();

        } else if (!password.equals(confirmPassword)) {

            // error message
            Toast.makeText(RegisterActivity.this, "Password does not match with the Confirm Password", Toast.LENGTH_LONG).show();

        } else {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    } else {
                        // sign up fail
                        Toast.makeText(RegisterActivity.this, "Sign up failed", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }


    }


    // back button
    @Override
    public void onClick(View view) {
        int i = view.getId();

        switch (i) {
            case R.id.btnBack:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.btnRegister:
                createAccount(mEmail.getText().toString(), mPassword.getText().toString(), mConfirmPassword.getText().toString());
                break;
        }


    }

}
