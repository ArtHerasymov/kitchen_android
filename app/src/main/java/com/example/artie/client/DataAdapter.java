package com.example.artie.client;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private List<Order> orderList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id, items, price,status;

        public MyViewHolder(View view) {
            super(view);
            id = view.findViewById(R.id.id);
            price = view.findViewById(R.id.price);
            status = view.findViewById(R.id.status);
        }
    }


    public DataAdapter(List<Order> ordersList) {
        this.orderList = ordersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.id.setText(order.getId() + "");
        holder.price.setText(String.valueOf(order.getPrice()));
        holder.status.setText(order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}