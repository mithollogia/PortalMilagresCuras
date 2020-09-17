package com.portalmilagresecuras.interfaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.portalmilagresecuras.R;
import com.portalmilagresecuras.modelo.Posts;
import com.portalmilagresecuras.persistencia.Adaptador;
import com.portalmilagresecuras.persistencia.ApiClient;
import com.portalmilagresecuras.persistencia.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Noticias extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private List<Posts> postList;
    private Adaptador adaptador;
    ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        recyclerView = findViewById(R.id.reciclerview);
        postList = new ArrayList<>();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

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

        openPosts();
    }

    public void openPosts(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Buscando postagens... Aguarde!");
        builder.setCancelable(false);
        final AlertDialog dialog = builder.show();

        Call<List<Posts>> call = apiInterface.getPosts();
        call.enqueue(new Callback<List<Posts>>() {
            @Override
            public void onResponse(Call<List<Posts>> call, Response<List<Posts>> response) {
                postList = response.body();

                if(postList.size() > 0) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    adaptador = new Adaptador(getApplicationContext(), postList);
                    recyclerView.setAdapter(adaptador);
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                    Toast.makeText(Noticias.this, R.string.error, Toast.LENGTH_SHORT).show(); police();
                }
            }

            @Override
            public void onFailure(Call<List<Posts>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Noticias.this, R.string.error, Toast.LENGTH_SHORT).show(); police();
            }
        });
    }

    private void police() {
        startActivity(new Intent(getApplicationContext(), Privacidade.class));
        overridePendingTransition(0,0);
    }
}