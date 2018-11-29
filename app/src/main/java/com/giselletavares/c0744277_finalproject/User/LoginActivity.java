package com.giselletavares.c0744277_finalproject.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.giselletavares.c0744277_finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private Button btnRegister;
    private Button btnLogin;
    private EditText mEmail;
    private EditText mPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        mEmail = findViewById(R.id.txtEmail);
        mPassword = findViewById(R.id.txtPassword);

        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {
            // user is logged in
            // go to home screen
        }
    }

    private void signIn(String email, String password){

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

            // error message
            Toast.makeText(LoginActivity.this, "Email/Password cannot be empty", Toast.LENGTH_LONG).show();

        } else {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            } else {
                                // sign in fail
                                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }

    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        switch (i) {
            case R.id.btnRegister:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btnLogin:
                signIn(mEmail.getText().toString(), mPassword.getText().toString());
                break;
        }

    }
}
