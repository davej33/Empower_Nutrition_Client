package com.example.davidjusten.empower_nutrition_client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    private DatabaseReference user_data;


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
                String food_name = (String) dataSnapshot.child("name").getValue();
                String food_desc = (String) dataSnapshot.child("desc").getValue();
                String food_price = (String) dataSnapshot.child("price").getValue();
                String food_image = (String) dataSnapshot.child("image").getValue();

                mDetailName.setText(food_name);
                mDetailDesc.setText(food_desc);
                mDetailPrice.setText(food_price);
                Picasso.get()
                        .load(food_image)
//                    .placeholder(R.drawable.user_placeholder)
//                    .error(R.drawable.user_placeholder_error)
                        .into(mDetailImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
