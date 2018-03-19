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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class ItemCategoryActivity extends AppCompatActivity {

    private static final String LOG_TAG = ItemCategoryActivity.class.getSimpleName();

    private static final String SMOOTHIE = "Smoothie";
    private static final String SHOT = "Shot";
    private static final String BOWL = "Bowl";
    private static final String JUICE = "Juice";
    private static final String TYPE = "type";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 1;

    private static String mOrderID;


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

    public static void retrieveOrderId() {
        DatabaseReference orderIdRef = FirebaseDatabase.getInstance().getReference().child("order_id");
        orderIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mOrderID = dataSnapshot.getValue().toString();
                Log.i(LOG_TAG, "order id = " + mOrderID);
                OrderSummaryActivity.setOrderId(mOrderID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthStateListener);
        retrieveOrderId();
        Log.i(LOG_TAG, "order onResume run: " + mOrderID);

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
