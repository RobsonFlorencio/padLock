package com.example.letimendes.padlock.ui;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.letimendes.padlock.R;
import com.example.letimendes.padlock.model.Pessoa;
import com.google.gson.Gson;


public class SplashScreenActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth não está disponível!", Toast.LENGTH_SHORT).show();
            finish();
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        }, 5000);
    }
}
