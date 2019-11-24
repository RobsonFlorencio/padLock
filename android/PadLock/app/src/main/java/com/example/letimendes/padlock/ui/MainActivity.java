package com.example.letimendes.padlock.ui;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.letimendes.padlock.R;
import com.example.letimendes.padlock.model.Pessoa;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("PADLOCK", MODE_PRIVATE);
        Pessoa pessoa = new Pessoa();
        pessoa.setEmail(prefs.getString("email", null));
        pessoa.setSenha(prefs.getString("senha", null));
        if(pessoa.getEmail() != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new HomeFragment());
            ft.commit();
        }else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, new LoginFragment());
            ft.commit();
        }
    }
}
