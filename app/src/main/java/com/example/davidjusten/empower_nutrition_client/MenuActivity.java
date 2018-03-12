package com.example.davidjusten.empower_nutrition_client;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

/**
 * Created by davidjusten on 3/12/18.
 */

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDbRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mRecyclerView = findViewById(R.id.menu_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDbRef = FirebaseDatabase.getInstance().getReference().child("Item");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MenuActivity.this,MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(

                Food.class,
                R.layout.menu_item,
                FoodViewHolder.class,
                mDbRef
        ) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {

            }

            @Override
            public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }
        };
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public FoodViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name){
            TextView food_name = mView.findViewById(R.id.item_name);
            food_name.setText(name);
        }

        public void setDesc(String desc){
            TextView food_desc = mView.findViewById(R.id.item_desc);
            food_desc.setText(desc);
        }

        public void setPrice(String price){
            TextView food_price = mView.findViewById(R.id.item_price);
            food_price.setText(price);
        }

        public void setImage(Context context, String image){
            ImageView food_image = mView.findViewById(R.id.item_image);
            Picasso.get()
                    .load(image)
//                    .placeholder(R.drawable.user_placeholder)
//                    .error(R.drawable.user_placeholder_error)
                    .into(food_image);
        }
    }
}
