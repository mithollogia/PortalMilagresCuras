package com.portalmilagresecuras.interfaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.portalmilagresecuras.R;
import com.portalmilagresecuras.modelo.Comment;
import com.portalmilagresecuras.modelo.Usuario;
import com.portalmilagresecuras.persistencia.AdaptadorComent;
import com.portalmilagresecuras.persistencia.ApiClient;
import com.portalmilagresecuras.persistencia.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Comentarios extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    BottomNavigationView bottomNavigationView;
    private FloatingActionButton new_pots;
    private RecyclerView recyclerView;
    private List<Comment> postList;
    private AdaptadorComent adaptador;
    ApiInterface apiInterface;
    String numero, autorization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coments);
        getSupportActionBar().hide();

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        recyclerView = findViewById(R.id.reciclerview);
        postList = new ArrayList<>();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        numero = currentUser.getPhoneNumber();
        start(numero);
        openComents();

        new_pots = findViewById(R.id.newPost);
        new_pots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserirComentario("insert");
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_coments);

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

    public void openComents(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Buscando comentários... Aguarde!");
        builder.setCancelable(false);
        final AlertDialog dialog = builder.show();

        Call<List<Comment>> call = apiInterface.get_comments();
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                postList = response.body();
                if(postList.size() > 0) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    adaptador = new AdaptadorComent(getApplicationContext(), postList);
                    recyclerView.setAdapter(adaptador);
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                    Toast.makeText(Comentarios.this, getString(R.string.mgs) +
                            "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Comentarios.this, getString(R.string.mgs) +
                        "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void start(final String numero){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Usuario> call = apiInterface.get_user(numero);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.body().isSuccess()){
                    Usuario usuario = response.body();
                    autorization = usuario.getUsuario_id();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(Comentarios.this, "Falha ao buscar dados!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inserirComentario(final String key) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Publicar um comentário");
        LayoutInflater inflater =LayoutInflater.from(this);
        View register_layout =inflater.inflate(R.layout.postar, null);

        final EditText postagem  = register_layout.findViewById(R.id.editComentario);

        dialog.setView(register_layout);
        dialog.setPositiveButton("Postar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                String post = postagem.getText().toString().trim();
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                Call<Comment> call = apiInterface.insertComment(key, autorization, post);
                call.enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        Toast.makeText(Comentarios.this, "Comentado com sucesso", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Comentarios.class));
                        overridePendingTransition(0,0);
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        Toast.makeText(Comentarios.this, "Falha ao comentar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();
    }

    private void police() {
        startActivity(new Intent(getApplicationContext(), Privacidade.class));
        overridePendingTransition(0,0);
    }

}