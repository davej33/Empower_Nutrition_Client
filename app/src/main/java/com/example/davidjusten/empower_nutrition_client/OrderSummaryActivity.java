package com.example.davidjusten.empower_nutrition_client;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.davidjusten.empower_nutrition_client.adapters.OrderSummaryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OrderSummaryActivity extends AppCompatActivity {

    private static final String LOG_TAG = OrderSummaryActivity.class.getSimpleName();

    private static RecyclerView mSummaryRV;
    private static OrderSummaryAdapter mAdapter;

    private static String mOrderId;
    private TextView mPreTax;
    private TextView mTax;
    private TextView mTotalCost;

    // popup vars
    private PopupWindow mPopupWindow;
    private View mPopupView;
    private LayoutInflater mLayoutInflater;
    private LinearLayout mActivityLayout;
    private double mPreTaxCosts;
    private double mTaxCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        mSummaryRV = findViewById(R.id.order_review_summary);
        mSummaryRV.setHasFixedSize(true);
        mSummaryRV.setLayoutManager(new LinearLayoutManager(this));
        mSummaryRV.setAdapter(mAdapter);

        mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mActivityLayout = findViewById(R.id.order_summary_layout);

        // cost views
        mPreTax = findViewById(R.id.order_review_pretax_cost);
        mTax = findViewById(R.id.order_review_tax);
        mTotalCost = findViewById(R.id.order_review_total_cost);

        // cost calcs
        mPreTax.setText(calculatePreTaxCosts());
        mTaxCalc = mPreTaxCosts * .065;
        mTax.setText(String.valueOf(mTaxCalc));
        mTotalCost.setText(String.valueOf(mPreTaxCosts + mTaxCalc));
    }

    private String calculatePreTaxCosts() {
        List<Food> list = OrderSummaryAdapter.getOrderList();
        mPreTaxCosts = 0;
        for (Food f : list) {
            mPreTaxCosts += f.getQuantity() * Double.valueOf(f.getPrice());
            Log.i(LOG_TAG, "pretax cost: " + mPreTaxCosts);
        }
        return String.valueOf(mPreTaxCosts);
    }

    public static void updateAdapater() {
        if (mAdapter == null) mAdapter = new OrderSummaryAdapter();
        mAdapter.notifyDataSetChanged();
    }

    public void addItemsClicked(View view) {
        startActivity(new Intent(OrderSummaryActivity.this, ItemCategoryActivity.class));
    }

    public void cancelClicked(View view) {
    }

    public void orderClicked(View view) {
        mPopupView = mLayoutInflater.inflate(R.layout.popup_order_confirmation, (ViewGroup) view.getRootView(), false);
        mPopupWindow = new PopupWindow(mPopupView,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                true);

        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }

        mPopupWindow.showAtLocation(mActivityLayout, Gravity.CENTER, 0, 0);

        dimBehind(mPopupWindow);
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

    public void orderConfirmationClicked(View view) {
        DatabaseReference newOrder = FirebaseDatabase.getInstance().getReference().child("Orders");
        DatabaseReference orderIdRef = FirebaseDatabase.getInstance().getReference().child("order_id");
        List<Food> orderedItems = OrderSummaryAdapter.getOrderList();
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();


        // get order id
//        final DatabaseReference orderIdRef = FirebaseDatabase.getInstance().getReference().child("order_id");
//        orderIdRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                orderId = (Integer) dataSnapshot.getValue();
//                Log.i(LOG_TAG, "order_id: ========== " + orderId);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        // create order id child in orders


        String orderId = "new_order_" + String.valueOf(mOrderId);
        DatabaseReference orderIdRefChild = newOrder.child(orderId);

        // save order items to db
        orderIdRefChild.setValue(orderedItems);

        showThankYouPopup();
        updateOrderNumber(orderIdRef);
        OrderSummaryAdapter.clearOrderItems();
    }


    private void updateOrderNumber(DatabaseReference orderIdRef) {
        Log.i(LOG_TAG, "order id: " + mOrderId);
        if (mOrderId == null) ItemCategoryActivity.retrieveOrderId();
        int i = Integer.valueOf(mOrderId);
        int newID = ++i;
        String newIdString = String.valueOf(newID);
        orderIdRef.setValue(newIdString);
        Log.i(LOG_TAG, "new id: " + newIdString);
    }

    private void showThankYouPopup() {
        mPopupWindow.dismiss();
        mPopupView = mLayoutInflater.inflate(R.layout.popup_thank_you, mActivityLayout, false);
        mPopupWindow = new PopupWindow(mPopupView,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                true);

        if (Build.VERSION.SDK_INT >= 21) {
            mPopupWindow.setElevation(5.0f);
        }

        mPopupWindow.showAtLocation(mActivityLayout, Gravity.CENTER, 0, 0);

        dimBehind(mPopupWindow);
    }

    private Object timeOrdered() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd @ hh:mm", Locale.US);
        return df.format(cal.getTime());

    }

    public void mainMenuClicked(View view) {
        startActivity(new Intent(OrderSummaryActivity.this, ItemCategoryActivity.class));
    }

    public static void setOrderId(String orderId) {
        mOrderId = orderId;
    }
}
