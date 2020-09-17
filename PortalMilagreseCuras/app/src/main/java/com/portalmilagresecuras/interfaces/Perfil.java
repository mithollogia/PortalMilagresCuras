package com.portalmilagresecuras.interfaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.portalmilagresecuras.R;
import com.portalmilagresecuras.modelo.Usuario;
import com.portalmilagresecuras.persistencia.ApiClient;
import com.portalmilagresecuras.persistencia.ApiInterface;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Perfil extends AppCompatActivity {
    private TextView profileName, profileEmail, profilePhone, profileAniversario;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private BottomNavigationView bottomNavigationView;
    private CircleImageView profileImage;
    private String id, telefone, picture;
    private RelativeLayout rootLayout;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        getSupportActionBar().setTitle("Editar Perfil");

        Intent intent = getIntent();
        picture = intent.getStringExtra("picture");
        rootLayout = findViewById(R.id.rootLayout);
        profileImage = findViewById(R.id.profileImage);
        profileName = findViewById(R.id.profileName);
        profilePhone = findViewById(R.id.profilePhone);
        profileEmail = findViewById(R.id.profileEmail);
        profileAniversario = findViewById(R.id.profileAniversario);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            if (!currentUser.getPhoneNumber().isEmpty()){
                start(currentUser.getPhoneNumber());
            }
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_profile);

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

    public void start(final String numero){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Usuario> call = apiInterface.get_user(numero);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.body().isSuccess()){
                    Usuario usuario = response.body();
                    telefone = numero;
                    id = usuario.getUsuario_id();
                    Picasso.get().load(usuario.getPicture()).into(profileImage);
                    profileName.setText(usuario.getUsuario_name());
                    profilePhone.setText(usuario.getUsuario_phone());
                    profileEmail.setText(usuario.getUsuario_email());
                    profileAniversario.setText(usuario.getUsuario_date());
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                System.out.println("getUsuario: " +t.getMessage());
            }
        });
    }

    private void update(final String key) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("ATUALIZAÇÃO DE PERFIL");

        LayoutInflater inflater =LayoutInflater.from(this);
        View register_layout =inflater.inflate(R.layout.user_update, null);

        final EditText nome  = register_layout.findViewById(R.id.nome);
        final EditText dnasc = register_layout.findViewById(R.id.dnasc);
        final EditText email = register_layout.findViewById(R.id.email);

        nome.setText(profileName.getText().toString());
        email.setText(profileEmail.getText().toString());
        dnasc.setText(profileAniversario.getText().toString());

        dialog.setView(register_layout);
        dialog.setPositiveButton("ATUALIZAR DADOS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                String name     = nome.getText().toString().trim();
                String mail     = email.getText().toString().trim();
                String nasci    = dnasc.getText().toString().trim();

                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                Call<Usuario> call = apiInterface.updateProfile(key, id, name, telefone, mail, nasci);
                call.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        start(telefone);
                        Snackbar.make(rootLayout, "Atualizado com sucesso!", Snackbar.LENGTH_SHORT);
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Snackbar.make(rootLayout, "Não foi possivel atualizar", Snackbar.LENGTH_SHORT);
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

    private void updatePicture(final String key) {
        String picture = null;
        if (bitmap == null) {
            picture = "";
        } else {
            picture = imageString(bitmap);
        }

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Usuario> call = apiInterface.updateImage(key,id, picture);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                start(telefone);
                Snackbar.make(rootLayout, "Atualizado com sucesso!", Snackbar.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Snackbar.make(rootLayout, "Não foi possivel atualizar", Snackbar.LENGTH_SHORT);
            }
        });

    }

    private void carregarFoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profileImage.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Carregar foto");

        dialog.setPositiveButton("atualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                updatePicture("upload");
            }
        });

        dialog.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    public String imageString(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        MenuItem perfilUpdate = menu.findItem(R.id.perfilUpdate);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.perfilUpdate:
                update("update");
                return true;

            case R.id.imageUpdate:
                carregarFoto();
                return true;

            default:  return super.onOptionsItemSelected(item);
        }
    }
}