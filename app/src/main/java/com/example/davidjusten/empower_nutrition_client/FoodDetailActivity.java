package com.example.davidjusten.empower_nutrition_client;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.davidjusten.empower_nutrition_client.adapters.OrderSummaryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FoodDetailActivity extends AppCompatActivity {


    private String item_key = null;
    private DatabaseReference mDb;
    private TextView mDetailName, mDetailPrice, mDetailDesc;
    private ImageView mDetailImage;
    private FirebaseAuth mAuth;
    private FirebaseUser current_user;
    private DatabaseReference user_data, mRef;
    private String food_name, food_desc, food_image, food_price;
    private PopupWindow mPopUpWindow;
    private View mPopUpView;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mLayout;
    private Food mCurrentFoodItem;


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

        mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mLayout = findViewById(R.id.food_detail_layout);
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
        // show pop-up
        showPurchaseConfirmationPopUp(view);

    }


    private Object timeOrdered() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd @ hh:mm", Locale.US);
        return df.format(cal.getTime());

    }

    private void showPurchaseConfirmationPopUp(View view) {

        mPopUpView = mLayoutInflater.inflate(R.layout.popup_add_item_confirmation, (ViewGroup) view.getRootView(), false);
        mPopUpWindow = new PopupWindow(mPopUpView,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                true
        );

        if (Build.VERSION.SDK_INT >= 21) {
            mPopUpWindow.setElevation(5.0f);
        }

        // show the popup
        mPopUpWindow.showAtLocation(mLayout, Gravity.CENTER, 0, 0);

        // dim popup background
        dimBehind(mPopUpWindow);
    }

    public static void dimBehind(PopupWindow popupWindow) {
        View container = popupWindow.getContentView().getRootView();
        Context context = popupWindow.getContentView().getContext();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.7f;
        wm.updateViewLayout(container, p);
    }

    public void addItemConfClicked(View view) {

        mPopUpView = mLayoutInflater.inflate(R.layout.popup_item_added, (ViewGroup) view.getRootView(), false);
        mPopUpWindow.dismiss();
        mPopUpWindow = new PopupWindow(mPopUpView,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                true);

        if (Build.VERSION.SDK_INT >= 21) {
            mPopUpWindow.setElevation(5.0f);
        }

        // show the popup
        mPopUpWindow.showAtLocation(mLayout, Gravity.CENTER, 0, 0);

        // dim popup background
        dimBehind(mPopUpWindow);

        final DatabaseReference newOrder = mRef.push();
        user_data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                OrderSummaryAdapter.addItemToCart(MenuActivity.getFoodItem());
                OrderSummaryActivity.updateAdapater();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void backToMenuClicked(View view) {
        startActivity(new Intent(FoodDetailActivity.this, ItemCategoryActivity.class));

    }

    public void checkoutClicked(View view) {
        startActivity(new Intent(FoodDetailActivity.this, OrderSummaryActivity.class));
    }
}
