package com.example.davidjusten.empower_nutrition_client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetailActivity extends AppCompatActivity {

    private String item_key = null;
    private DatabaseReference mDb;
    private TextView mDetailName, mDetailPrice, mDetailDesc;
    private ImageView mDetailImage;
    private Button mOrderButton;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private DatabaseReference user_data, mRef;
    private String food_name, food_desc, food_image, food_price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        item_key = getIntent().getExtras().getString("ItemId");
        mDb = FirebaseDatabase.getInstance().getReference().child("Item");

        mDetailName = findViewById(R.id.detailName);
        mDetailDesc = findViewById(R.id.detailDesc);
        mDetailPrice = findViewById(R.id.detailPrice);
        mDetailImage = findViewById(R.id.detailImageView);

        mAuth = FirebaseAuth.getInstance();

        // get current user
        current_user = mAuth.getCurrentUser();
        user_data = FirebaseDatabase.getInstance().getReference().child(current_user.getUid());

        //extract data
        mDb.child(item_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                food_name = (String) dataSnapshot.child("name").getValue();
                food_desc = (String) dataSnapshot.child("desc").getValue();
                food_price = (String) dataSnapshot.child("price").getValue();
                food_image = (String) dataSnapshot.child("image").getValue();

                mDetailName.setText(food_name);
                mDetailDesc.setText(food_desc);
                mDetailPrice.setText(food_price);
                Picasso.get()
                        .load(food_image)
//                    .placeholder(R.drawable.user_placeholder)
//                    .error(R.drawable.user_placeholder_error)
                        .into(mDetailImage);

                mRef = FirebaseDatabase.getInstance().getReference().child("Orders");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void purchaseItemClicked(View view) {
        final DatabaseReference newOrder = mRef.push();
        user_data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("tag","datasnapshot: " + dataSnapshot.child("Name").getValue());
                newOrder.child("itemName").setValue(food_name);
                newOrder.child("userName").setValue(current_user.getEmail()).addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(FoodDetailActivity.this, MenuActivity.class));
                            }
                        }
                );
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
