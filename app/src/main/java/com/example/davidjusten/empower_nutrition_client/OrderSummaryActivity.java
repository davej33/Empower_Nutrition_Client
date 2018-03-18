package com.example.davidjusten.empower_nutrition_client;

import android.content.ClipData;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.PopupWindow;

import com.example.davidjusten.empower_nutrition_client.adapters.OrderSummaryAdapter;

import java.util.ArrayList;

public class OrderSummaryActivity extends AppCompatActivity {

    private static final String LOG_TAG = OrderSummaryActivity.class.getSimpleName();
    private static ArrayList<Food> mOrderList;
    private RecyclerView mSummaryRV;
    private static OrderSummaryAdapter mAdapter;
    private PopupWindow mPopupWindow;
    private View mPopupView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);

        mSummaryRV = findViewById(R.id.order_review_summary);
        mSummaryRV.setHasFixedSize(true);
        mSummaryRV.setLayoutManager(new LinearLayoutManager(this));
        mSummaryRV.setAdapter(mAdapter);


//        newOrder.child("itemName").setValue(food_name);
//        newOrder.child("time").setValue(timeOrdered());
//        newOrder.child("userName").setValue(current_user.getEmail()).addOnCompleteListener(
//                new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                    }
//                }
//        );

    }


    public static void updateAdapater() {
        if (mAdapter == null)  mAdapter = new OrderSummaryAdapter();
        mAdapter.notifyDataSetChanged();
    }

    public void addItemsClicked(View view) {
        startActivity(new Intent(OrderSummaryActivity.this, ItemCategoryActivity.class));
    }

    public void cancelClicked(View view) {
    }

    public void orderClicked(View view) {

    }
}
