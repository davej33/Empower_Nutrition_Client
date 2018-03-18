package com.example.davidjusten.empower_nutrition_client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class OrderReviewActivity extends AppCompatActivity {


    private static ArrayList<Food> mOrderList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);


        //                newOrder.child("itemName").setValue(food_name);
//                newOrder.child("time").setValue(timeOrdered());
//                newOrder.child("userName").setValue(current_user.getEmail()).addOnCompleteListener(
//                        new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                            }
//                        }
//                );

    }

    public static void addItemToCart(Food foodItem) {
        if(mOrderList == null) mOrderList = new ArrayList<>();
        mOrderList.add(foodItem);
    }
}
