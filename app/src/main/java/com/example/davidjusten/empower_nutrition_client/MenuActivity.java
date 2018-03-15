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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

/**
 * Created by davidjusten on 3/12/18.
 */

public class MenuActivity extends AppCompatActivity {

    private static final String LOG_TAG = MenuActivity.class.getSimpleName();
    private static final String SMOOTHIE = "Smoothie";
    private static final String SHOT = "Shot";
    private static final String BOWL = "Bowl";
    private static final String JUICE = "Juice";
    private static final String TYPE = "type";

    private RecyclerView mRecyclerView;
    private DatabaseReference mDbRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> mAdapter;
    private String mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // get item type
        mType = getIntent().getStringExtra(TYPE);

        mRecyclerView = findViewById(R.id.menu_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDbRef = FirebaseDatabase.getInstance().getReference().child("Item");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MenuActivity.this, MainActivity.class);
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
        Query query = writeQuery();
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(query, Food.class)
                .build();
        mAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
                holder.setDesc(model.getDesc());
                Log.i("This", "desc check: " + model.getDesc());
                holder.setName(model.getName());
                holder.setPrice(model.getPrice());

                final String item_key = getRef(position).getKey();
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent detailIntent = new Intent(MenuActivity.this, FoodDetailActivity.class);
                        detailIntent.putExtra("ItemId", item_key);
                        startActivity(detailIntent);
                    }
                });
            }

            @Override
            public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
                return new FoodViewHolder(view);
            }
        };

        mAdapter.startListening();

        mRecyclerView.setAdapter(mAdapter);
    }

    private Query writeQuery() {
        switch (mType) {
            case SMOOTHIE:
                return mDbRef.orderByChild("type").equalTo("Smoothie");
            case JUICE:
                return mDbRef.orderByChild("type").equalTo("Juice");
            case SHOT:
                return mDbRef.orderByChild("type").equalTo("Shot");
            case BOWL:
                return mDbRef.orderByChild("type").equalTo("Bowl");
            default:
                Log.e(LOG_TAG, "No item type match for Firebase query: " + mType);
        }
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public FoodViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView food_name = mView.findViewById(R.id.item_name);
            food_name.setText(name);
        }

        public void setDesc(String desc) {
            TextView food_desc = mView.findViewById(R.id.item_desc);
            food_desc.setText(desc);
        }

        public void setPrice(String price) {
            TextView food_price = mView.findViewById(R.id.item_price);
            food_price.setText(price);
        }

//        public void setImage(String image) {
//            ImageView food_image = mView.findViewById(R.id.item_image);
//            Picasso.get()
//                    .load(image)
////                    .placeholder(R.drawable.user_placeholder)
////                    .error(R.drawable.user_placeholder_error)
//                    .into(food_image);
//        }
    }
}
