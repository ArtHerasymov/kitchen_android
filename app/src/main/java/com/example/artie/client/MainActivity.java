package com.example.artie.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView responseView;
    LinkedList<String>items = new LinkedList<>();
    String itemList = "";
    double price = 0;
    Button lotus,noodles,strawberrykuchi,roastedsoup,carbonara,pancakes;

    private List<Order> orderList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DataAdapter oAdapter;

    private Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lotus = findViewById(R.id.lotusSoup);
        noodles = findViewById(R.id.noodles);
        strawberrykuchi = findViewById(R.id.strawberrykuchi);

        roastedsoup = findViewById(R.id.roastedsoup);
        carbonara = findViewById(R.id.carbonara);
        pancakes = findViewById(R.id.pancakes);

    }

    @Override
    public void onClick(View view) {

        setContentView(R.layout.activity_new_order);
    }

    public void onChineeseClick(View view){
        setContentView(R.layout.activity_chineese);
    }

    public void onItalianClick(View view){
        setContentView(R.layout.activity_italian);
    }

    public void goBackOnClick(View view) {
        setContentView(R.layout.activity_new_order);
    }

    public void cancelClick(View view) {
        setContentView(R.layout.activity_main);
    }

    public void onSubmitClick(View view) {
        String url = "http://192.168.1.106:5000/orders/create";
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Toast.makeText(
                        MainActivity.this,
                        response,
                        Toast.LENGTH_SHORT)
                        .show();
                currentOrder = new Order();
                currentOrder.setItems(itemList);
                currentOrder.setPrice(price);
                currentOrder.setStatus("IN_PROGRESS");
                System.out.println("RESPONSE : " + response);
                int id = Integer.parseInt(response);

                currentOrder.setId(id);
                orderList.add(currentOrder);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Toast.makeText(
                        MainActivity.this,
                        error.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("Waiter", "Johny LOL"); //Add the data you'd like to send to the server.
                MyData.put("Items", itemList);
                MyData.put("DiscountID", "123");
                MyData.put("InitialPrice",String.valueOf(price));
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }

    public void onCheckClicK(View view){
        setContentView(R.layout.orderpage);
        recyclerView = findViewById(R.id.recycler_view);

        for(Order o :orderList){
            System.out.println("ID : " + o.getId());
            System.out.println("PRICE : " + o.getPrice());

        }

        oAdapter = new DataAdapter(orderList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(oAdapter);
    }

    public void onItemClick(View view){
        switch(view.getId()){
            case R.id.lotusSoup:
                itemList += "1";
                price+=8;
                break;
            case R.id.noodles:
                itemList += ",2";
                price += 10;
                break;
            case R.id.strawberrykuchi:
                itemList += ",3";
                price += 4;
                break;
            case R.id.roastedsoup:
                itemList += ",4";
                price += 7;
                break;
            case R.id.carbonara:
                itemList += ",5";
                price += 3;
                break;
            case R.id.pancakes:
                itemList += ",6";
                price += 5;
                break;
        }
    }

    public void onRefreshCick(View view){
        TextView  curid = view.findViewById(R.id.id);
        System.out.println(curid.getText());
//        String url = "http://192.168.1.106:5000/orders/create";
//        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
//        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                //This code is executed if the server responds, whether or not the response contains data.
//                //The String 'response' contains the server's response.
//                Toast.makeText(
//                        MainActivity.this,
//                        response,
//                        Toast.LENGTH_SHORT)
//                        .show();
//                currentOrder = new Order();
//                currentOrder.setItems(itemList);
//                currentOrder.setPrice(price);
//                currentOrder.setStatus("IN_PROGRESS");
//                currentOrder.setId(Integer.getInteger(response));
//                orderList.add(currentOrder);
//            }
//        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                //This code is executed if there is an error.
//                Toast.makeText(
//                        MainActivity.this,
//                        error.getMessage(),
//                        Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }) {
//            protected Map<String, String> getParams() {
//                Map<String, String> MyData = new HashMap<String, String>();
//                MyData.put("Waiter", "Johny LOL"); //Add the data you'd like to send to the server.
//                MyData.put("Items", itemList);
//                MyData.put("DiscountID", "123");
//                MyData.put("InitialPrice",String.valueOf(price));
//                return MyData;
//            }
//        };
//        MyRequestQueue.add(MyStringRequest);
    }
}
