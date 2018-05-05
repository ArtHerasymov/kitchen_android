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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

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

    SeekBar lotusSeekbar;
    SeekBar noodlesBar;
    SeekBar kuchiBar;
    SeekBar roastedBar;
    SeekBar carbonaraBar;
    SeekBar pancakesBar;


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
        changeLocaleButton = findViewById(R.id.changeLocaleButton);
        changeLocaleButton.setText(currentLocale);
    }

    public void onSubmitClick(View view) {
       // Remove odd comma from the end of the line
        if(!itemList.equals(""))
            itemList = trimItemList(itemList);
        // Validate preformed order
        setContentView(R.layout.confirm_order);

        // Attaching listeners on seekbars
        lotusSeekbar = findViewById(R.id.seekBarLotus);
        noodlesBar = findViewById(R.id.seekBarNoodles);
        kuchiBar = findViewById(R.id.seekBarKuchi);
        roastedBar = findViewById(R.id.seekBarRoastedSoup);
        carbonaraBar = findViewById(R.id.seekBarCarbonara);
        pancakesBar = findViewById(R.id.seekBarPancakes);

        lotusSeekbar.setOnSeekBarChangeListener(this);
        noodlesBar.setOnSeekBarChangeListener(this);
        kuchiBar.setOnSeekBarChangeListener(this);
        roastedBar.setOnSeekBarChangeListener(this);
        carbonaraBar.setOnSeekBarChangeListener(this);
        pancakesBar.setOnSeekBarChangeListener(this);


        TextView priceView = findViewById(R.id.confirmPrice);
        System.out.println("Items : " + itemList);

        setupSeekbars();
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

        Toast.makeText(
                MainActivity.this,
                message,
                Toast.LENGTH_SHORT)
                .show();
    }

    public void onRefreshCick(final View view){
        View parent = (View) view.getParent().getParent();
        final TextView  curid = parent.findViewById(R.id.id);
        final TextView statusView = findViewById(R.id.status);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.105:5000/orders/details/"+curid.getText();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                        @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                            statusView.setText(response);
                            for(Order o : orderList){
                                if(o.getId() == Integer.parseInt(curid.getText().toString())){
                                    o.setStatus(response);
                                }
                            }
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
        if(itemList.equals("")){
            Toast.makeText(
                    MainActivity.this,
                    "You cannot send an empty order",
                    Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        String url = "http://192.168.1.105:5000/orders/create";
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
                currentOrder.setStatus("IN_PROGRESS");

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    currentOrder.setPrice(Double.parseDouble(jsonObject.getString("FinalPrice")));
                    currentOrder.setId(Integer.parseInt(jsonObject.getString("ID")));
                    System.out.println("FINAL PRICE : " + Double.parseDouble(jsonObject.getString("FinalPrice")));
                    System.out.println("ID : " + Integer.parseInt(jsonObject.getString("ID")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                itemList = "";
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("Waiter", "Johny LOL");
                System.out.println("ITEMS !!!!!!!!!!!!  : " + itemList);
                MyData.put("Items", itemList);
                MyData.put("InitialPrice",String.valueOf(price));
                MyData.put("DiscountID", "615547");
                MyData.put("Type", determineType());
                MyData.put("Locale",currentLocale);

                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
        setContentView(R.layout.activity_main);
    }

    public void onCancelButtonClick(View view){
        this.itemList = "";
        setContentView(R.layout.activity_main);
        changeLocaleButton = findViewById(R.id.changeLocaleButton);
        changeLocaleButton.setText(currentLocale);
    }

    public void onBackFromCheck(View view){
        setContentView(R.layout.activity_main);
        changeLocaleButton = findViewById(R.id.changeLocaleButton);
        changeLocaleButton.setText(currentLocale);
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
                (!itemList.contains("4") && !itemList.contains("5") && !itemList.contains("6"))){
            return "CHINEESE";
        }
        else if((itemList.contains("1") || itemList.contains("2") || itemList.contains("3")) &&
                (itemList.contains("4") || itemList.contains("5") || itemList.contains("6"))){
            return "MIXED";
        } else {
            return "ITALIAN";
        }
    }

   private void setupSeekbars(){
        String[] items = itemList.split(",");

        for(String item : items){
            SeekBar currentSeekBar = null;
            switch(item){
                case "1": {
                    currentSeekBar = findViewById(R.id.seekBarLotus);
                    currentSeekBar.setProgress(currentSeekBar.getProgress() + 1);
                    break;
                }

                case "2":{
                    currentSeekBar = findViewById(R.id.seekBarNoodles);
                    currentSeekBar.setProgress(currentSeekBar.getProgress() + 1);
                    break;
                }
                case "3":{
                    currentSeekBar = findViewById(R.id.seekBarKuchi);
                    currentSeekBar.setProgress(currentSeekBar.getProgress() + 1);
                    break;
                }
                case "4":{
                    currentSeekBar = findViewById(R.id.seekBarRoastedSoup);
                    currentSeekBar.setProgress(currentSeekBar.getProgress() + 1);
                    break;
                }
                case "5":{
                    currentSeekBar = findViewById(R.id.seekBarCarbonara);
                    currentSeekBar.setProgress(currentSeekBar.getProgress() + 1);
                    break;
                }
                case "6":{
                    currentSeekBar = findViewById(R.id.seekBarPancakes);
                    currentSeekBar.setProgress(currentSeekBar.getProgress() + 1);
                    break;
                }
            }
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        // Every time seekbar progress changes, itemList string nullifies and being refilled
        if(!b)
            return;
        itemList = "";
        price = 0.0;
        LinkedList<SeekBar>seekBars = new LinkedList<>();

        seekBars.add(lotusSeekbar);
        seekBars.add(noodlesBar);
        seekBars.add(kuchiBar);

        seekBars.add(roastedBar);
        seekBars.add(carbonaraBar);
        seekBars.add(pancakesBar);

        for(SeekBar bar :seekBars){
            switch(bar.getId()){
                case R.id.seekBarLotus:{
                    for(int j = 0 ; j < bar.getProgress(); j++){
                        itemList = itemList.concat("1,");
                        price+=8;
                    }
                    break;
                }
                case R.id.seekBarNoodles:{
                    for(int j = 0 ; j < bar.getProgress(); j++){
                        itemList = itemList.concat("2,");
                        price += 10;
                    }
                    break;
                }
                case R.id.seekBarKuchi:{
                    for(int j = 0 ; j < bar.getProgress(); j++){
                        itemList = itemList.concat("3,");
                        price += 4;
                    }
                    break;
                }
                case R.id.seekBarRoastedSoup:{
                    for(int j = 0 ; j < bar.getProgress(); j++){
                        itemList = itemList.concat("4,");
                        price+= 7;
                    }
                    break;
                }
                case R.id.seekBarCarbonara:{
                    for(int j = 0 ; j < bar.getProgress(); j++){
                        itemList = itemList.concat("5,");
                        price+=5;
                    }
                    break;
                }
                case R.id.seekBarPancakes:{
                    for(int j = 0 ; j < bar.getProgress(); j++){
                        itemList = itemList.concat("6,");
                        price += 3;
                    }
                    break;
                }
            }
        }
        TextView priceView = findViewById(R.id.confirmPrice);
        priceView.setText(String.valueOf(price));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void onSyncClick(View view){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.105:5000/orders";
        orderList = new LinkedList<>();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0 ; i < jsonArray.length(); i++){

                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                if(!jsonObj.getString("Status").equals("CANCELLED")){
                                    Order order = new Order();
                                    order.setStatus(jsonObj.getString("Status"));
                                    order.setId(Integer.parseInt(jsonObj.getString("OrderID")));
                                    order.setPrice(Double.parseDouble(jsonObj.getString("InitialPrice")));
                                    order.setItems(jsonObj.getString("Items"));
                                    orderList.add(order);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setContentView(R.layout.orderpage);
                        recyclerView = findViewById(R.id.recycler_view);

                        Collections.sort(orderList, new OrderComparator());
                        oAdapter = new DataAdapter(orderList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(oAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

    public void onCancelClick(View view){
        View parent = (View) view.getParent().getParent();
        final TextView  curid = parent.findViewById(R.id.id);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://192.168.1.105:5000/orders/delete/"+curid.getText();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        Iterator<Order> iter = orderList.iterator();

                        while(iter.hasNext()){
                            Order cur = iter.next();
                            if(cur.getId() == Integer.parseInt(curid.getText().toString())){
                                iter.remove();
                            }
                        }
                        setContentView(R.layout.orderpage);
                        recyclerView = findViewById(R.id.recycler_view);

                        Collections.sort(orderList, new OrderComparator());
                        oAdapter = new DataAdapter(orderList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(oAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(stringRequest);
    }

}
