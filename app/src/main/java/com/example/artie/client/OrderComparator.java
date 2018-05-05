package com.example.artie.client;

import android.support.annotation.NonNull;

import java.util.Comparator;

class OrderComparator implements Comparator<Order> {

    @Override
    public int compare(Order order, Order t1) {
        if(order.getId() < t1.getId())
            return 1;
        else
            return -1;
    }
}
