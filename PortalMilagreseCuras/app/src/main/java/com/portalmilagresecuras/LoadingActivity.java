package com.portalmilagresecuras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import com.portalmilagresecuras.interfaces.Login;

public class LoadingActivity extends AppCompatActivity {
    TextView logoName;
    ImageView logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getSupportActionBar().hide();

        logoImage = findViewById(R.id.logoImage);
        logoName = findViewById(R.id.logoTitle);

        logoImage.setTranslationY(-1000);
        logoName.setTranslationY(1000);
        logoImage.animate().translationY(0).setDuration(1500).start();
        logoName.animate().translationY(0).setDuration(1500).start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        }, 2500);
    }
}