package com.example.davidjusten.empower_nutrition_client;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.davidjusten.empower_nutrition_client.adapters.OrderSummaryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    private RecyclerView mSummaryRV;
    private static OrderSummaryAdapter mAdapter;

    // popup vars
    private PopupWindow mPopupWindow;
    private View mPopupView;
    private LayoutInflater mLayouInflater;
    private LinearLayout mActivityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);

        mSummaryRV = findViewById(R.id.order_review_summary);
        mSummaryRV.setHasFixedSize(true);
        mSummaryRV.setLayoutManager(new LinearLayoutManager(this));
        mSummaryRV.setAdapter(mAdapter);

        mLayouInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mActivityLayout = findViewById(R.id.order_summary_layout);


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
        mPopupView = mLayouInflater.inflate(R.layout.popup_order_confirmation, (ViewGroup) view.getRootView(), false);
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
        DatabaseReference newOrder = FirebaseDatabase.getInstance().getReference().child("Orders").push();
        List orderedItems = OrderSummaryAdapter.getOrderList();
        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();


        Food itemOrdered = (Food) orderedItems.get(0);
        newOrder.child("itemName").setValue(itemOrdered.getName());
        newOrder.child("time").setValue(timeOrdered());
        newOrder.child("userName").setValue(current_user.getEmail()).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mPopupWindow.dismiss();
                        mPopupView = mLayouInflater.inflate(R.layout.popup_thank_you, mActivityLayout, false);
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
                }
        );
    }

    private Object timeOrdered() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd @ hh:mm", Locale.US);
        return df.format(cal.getTime());

    }

    public void mainMenuClicked(View view) {
        startActivity(new Intent(OrderSummaryActivity.this, ItemCategoryActivity.class));
    }

    public void closeClicked(View view) {

        this.finish();
        System.exit(0);
    }
}
