package com.example.davidjusten.empower_nutrition_client.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.davidjusten.empower_nutrition_client.Food;
import com.example.davidjusten.empower_nutrition_client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidjusten on 3/18/18.
 */

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.OrderSummaryViewHolder> {

    private static final String LOG_TAG = OrderSummaryAdapter.class.getSimpleName();
    private static List<Food> mOrderList;

    @NonNull
    @Override
    public OrderSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_summary_item, parent, false);
        return new OrderSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderSummaryViewHolder holder, int position) {
        Food item = mOrderList.get(position);

        holder.name.setText(item.getName());
        holder.cost.setText(item.getPrice());
//        holder.quantity.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        if (mOrderList != null) {
            return mOrderList.size();
        } else {
            return 0;
        }
    }

    public static void addItemToCart(Food foodItem) {
        if (mOrderList == null) mOrderList = new ArrayList<>();
        mOrderList.add(foodItem);
        Log.i(LOG_TAG, "Order List ----------- " + mOrderList.size());
    }


    public class OrderSummaryViewHolder extends RecyclerView.ViewHolder {

        View mView = itemView;
        TextView name;
        TextView cost;
        TextView quantity;

        public OrderSummaryViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            name = itemView.findViewById(R.id.order_item_name);
            cost = itemView.findViewById(R.id.order_item_cost);
            quantity = itemView.findViewById(R.id.order_item_quantity);
        }
    }
}
