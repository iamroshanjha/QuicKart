package com.a0quickcartgmail.quickart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Aboutus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
