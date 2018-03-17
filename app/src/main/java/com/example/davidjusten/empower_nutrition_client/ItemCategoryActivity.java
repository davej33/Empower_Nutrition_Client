package com.example.davidjusten.empower_nutrition_client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class ItemCategoryActivity extends AppCompatActivity {

    private static final String SMOOTHIE = "Smoothie";
    private static final String SHOT = "Shot";
    private static final String BOWL = "Bowl";
    private static final String JUICE = "Juice";
    private static final String TYPE = "type";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_category);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    // signed in
                    Toast.makeText(ItemCategoryActivity.this, "You're signed in!", Toast.LENGTH_SHORT).show();
                } else {
                    // not signed in
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()
                                    ))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
        Log.i("tag", "is this running?????????????????????? ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }
    public void smoothieClicked(View view) {
        Intent smoothieIntent = new Intent(ItemCategoryActivity.this, MenuActivity.class);
        smoothieIntent.putExtra(TYPE, SMOOTHIE);
        startActivity(smoothieIntent);
    }

    public void juiceClicked(View view) {
        Intent juiceIntent = new Intent(ItemCategoryActivity.this, MenuActivity.class);
        juiceIntent.putExtra(TYPE, JUICE);
        startActivity(juiceIntent);
    }

    public void shotsClicked(View view) {
        Intent shotIntent = new Intent(ItemCategoryActivity.this, MenuActivity.class);
        shotIntent.putExtra(TYPE, SHOT);
        startActivity(shotIntent);
    }

    public void bowlsClicked(View view) {
        Intent bowlsIntent = new Intent(ItemCategoryActivity.this, MenuActivity.class);
        bowlsIntent.putExtra(TYPE, BOWL);
        startActivity(bowlsIntent);
    }
}
