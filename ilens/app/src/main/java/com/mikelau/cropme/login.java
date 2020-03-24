package com.mikelau.cropme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mikelau.cropme.R;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    public void clklogin(View v) {
        Intent i = new Intent(login.this, search.class);
        startActivity(i);
    }
}
