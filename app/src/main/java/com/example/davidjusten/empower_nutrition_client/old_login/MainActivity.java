package com.example.davidjusten.empower_nutrition_client.old_login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.davidjusten.empower_nutrition_client.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmail = findViewById(R.id.editEmail);
        mPassword = findViewById(R.id.editPassword);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
    }

    public void signUpButtonClicked(View view) {

        final String emailText = mEmail.getText().toString().trim();
        String pwText = mPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(emailText) && !TextUtils.isEmpty(pwText)) {
            mAuth.createUserWithEmailAndPassword(emailText, pwText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference currentUser = mDatabase.child(user_id);
                        currentUser.child("Name").setValue(emailText);
                    }

                }
            });
        }
    }

    public void signInButtonClicked(View view) {
        Intent signInIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(signInIntent);
    }
}