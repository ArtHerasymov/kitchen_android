package com.example.artie.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewOrderActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
    }

    @Override
    public void onClick(View view) {
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
}
