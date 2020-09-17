package com.portalmilagresecuras.interfaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.portalmilagresecuras.R;

public class Privacidade extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police);
        getSupportActionBar().hide();

        webView = findViewById(R.id.webview);

        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);

        bottomNavigationView = findViewById(R.id.bottom_navigation);


        Intent intent = getIntent();
        if (intent != null){
            Bundle params = intent.getExtras();
            if (params != null){
                webView.loadUrl(params.getString("url"));
                if(params.getString("url").equals("file:///android_asset/www/sobre.html")){
                    bottomNavigationView.setSelectedItemId(R.id.menu_sobre);
                }else{
                    bottomNavigationView.setSelectedItemId(R.id.menu_police);
                }
            }
        }



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(), Noticias.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_coments:
                        startActivity(new Intent(getApplicationContext(), Comentarios.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_sobre:
                        Intent intent = new Intent(getApplicationContext(), Privacidade.class);
                        intent.putExtra("url", "file:///android_asset/www/sobre.html");
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_police:
                        Intent privacidade = new Intent(getApplicationContext(), Privacidade.class);
                        privacidade.putExtra("url", "file:///android_asset/www/index.html");
                        startActivity(privacidade);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.menu_profile:
                        startActivity(new Intent(getApplicationContext(), Perfil.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}