package com.example.artie.client;

import android.annotation.SuppressLint;
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
    String currentLocale;

    private List<Order> orderList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DataAdapter oAdapter;
    private Button changeLocaleButton;

    private Order currentOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.itemList = "";
        this.price = 0.0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locale);

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
        if(this.itemList.equals("") || this.price == 0.0){
            Toast.makeText(
                    MainActivity.this,
                    "You haven't selected any items",
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        setContentView(R.layout.confirm_order);
        TextView itemsView = findViewById(R.id.confirmItems);
        TextView priceView = findViewById(R.id.confirmPrice);

        itemsView.setText(itemList);
        priceView.setText(String.valueOf(price));

    }

    public void onCheckClicK(View view){
        setContentView(R.layout.orderpage);
        recyclerView = findViewById(R.id.recycler_view);

        oAdapter = new DataAdapter(orderList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(oAdapter);
    }

    public void onItemClick(View view){
        String message = "";
        switch(view.getId()){
            case R.id.lotusSoup:
                itemList += "1,";
                price+=8;
                message = "Lotus soup added";
                break;
            case R.id.noodles:
                itemList += "2,";
                price += 10;
                message = "Noodles added";
                break;
            case R.id.strawberrykuchi:
                itemList += "3,";
                price += 4;
                message = "Strawberry kuchi added";
                break;
            case R.id.roastedsoup:
                itemList += "4,";
                price += 7;
                message = "Roasted soup added";
                break;
            case R.id.carbonara:
                itemList += "5,";
                price += 3;
                message = "Carbonara added";
                break;
            case R.id.pancakes:
                itemList += "6,";
                price += 5;
                message = "Pancakes added";
                break;
        }
        itemList = trimItemList(itemList);


        Toast.makeText(
                MainActivity.this,
                message,
                Toast.LENGTH_SHORT)
                .show();
    }

    public void onRefreshCick(final View view){
        TextView  curid = view.findViewById(R.id.id);
        final TextView statusView = view.findViewById(R.id.status);
        System.out.println(curid.getText());
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.43.196:5000/orders/details/"+curid.getText();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                        @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                            statusView.setText(response);
                            System.out.println("RESPONSE : " + response);
                        }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    public void confirmButtonClick(View view) {
        String url = "http://192.168.43.196:5000/orders/create";
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(
                        MainActivity.this,
                        response,
                        Toast.LENGTH_SHORT)
                        .show();
                currentOrder = new Order();
                currentOrder.setItems(itemList);
                currentOrder.setPrice(price);
                currentOrder.setStatus("IN_PROGRESS");
                int id = Integer.parseInt(response);

                currentOrder.setId(id);
                orderList.add(currentOrder);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(
                        MainActivity.this,
                        error.getMessage(),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("Waiter", "Johny LOL");
                MyData.put("Items", itemList);
                MyData.put("DiscountID", "123");
                MyData.put("InitialPrice",String.valueOf(price));
                MyData.put("Type", determineType());
                MyData.put("Locale",currentLocale);

                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
        setContentView(R.layout.activity_main);
    }

    public void onCancleButtonClick(View view){
        this.currentOrder = null;
        setContentView(R.layout.activity_main);
    }

    public void onBackFromCheck(View view){
        setContentView(R.layout.activity_main);
    }

    public void onLocaleClick(View view){
        view.getId();
        switch(view.getId()){
            case R.id.usaButton:{
                this.currentLocale = "USA";
                break;
            }
            case R.id.europeButton:{
                this.currentLocale = "EU";
                break;
            }
        }
        setContentView(R.layout.activity_main);
        changeLocaleButton = findViewById(R.id.changeLocaleButton);
        changeLocaleButton.setText(currentLocale);
    }

    public void onLocaleChangeClock(View view){
        changeLocaleButton = findViewById(R.id.changeLocaleButton);
        if(currentLocale.equals("USA")){
            this.currentLocale = "Europe";
            changeLocaleButton.setText(currentLocale);
        } else {
            this.currentLocale = "USA";
            changeLocaleButton.setText(currentLocale);
        }
    }

    public String trimItemList(String itemList){
        return itemList.substring(0, itemList.length() - 1);
    }

    private String determineType(){
        if((itemList.contains("1") || itemList.contains("2") || itemList.contains("3")) &&
                !(itemList.contains("4") || itemList.contains("5") || itemList.contains("6"))){
            return "CHINEESE";
        }
        else if((itemList.contains("1") || itemList.contains("2") || itemList.contains("3")) &&
                (itemList.contains("4") || itemList.contains("5") || itemList.contains("6"))){
            return "MIXED";
        } else {
            return "ITALIAN";
        }
    }

}
