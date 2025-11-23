package com.example.jeu_x_o;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jeu_x_o.login.LoginActivity; // IMPORTANT
import com.example.jeu_x_o.home.MainActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            // Aller vers LoginActivity au lieu de MainActivity
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, 1200);
    }
}