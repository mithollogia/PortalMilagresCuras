package com.portalmilagresecuras.interfaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.portalmilagresecuras.R;
import com.portalmilagresecuras.modelo.Usuario;
import com.portalmilagresecuras.persistencia.ApiClient;
import com.portalmilagresecuras.persistencia.ApiInterface;
import java.util.concurrent.TimeUnit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private Button enviarNumero, enviarCode, enviarName;
    private String  verificationId, profileName;
    private EditText phone, code, name;
    private TextView setMesssage;
    private Spinner spinner;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        registerCheck();

        spinner = findViewById(R.id.contry);
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, CountryData.countryNames));

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        code = findViewById(R.id.code);
        setMesssage = findViewById(R.id.setMesssage);

        enviarNumero = findViewById(R.id.enviarNumero);
        enviarCode = findViewById(R.id.enviarCode);
        enviarName = findViewById(R.id.enviarName);

        code.setVisibility(View.GONE);
        enviarCode.setVisibility(View.GONE);

        spinner.setVisibility(View.GONE);
        phone.setVisibility(View.GONE);
        enviarNumero.setVisibility(View.GONE);

        setMesssage.setText("BEM VINDO\n\n Por favor, digite seu nome.");

        enviarName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()){
                    name.setError("Nome obrigatorio!");
                    name.requestFocus();
                    return;
                }
                profileName = name.getText().toString().trim();

                spinner.setVisibility(View.VISIBLE);
                phone.setVisibility(View.VISIBLE);
                enviarNumero.setVisibility(View.VISIBLE);

                setMesssage.setText("MUITO BEM\n\n Agora para concluir seu cadastro, selecione seu país e digite seu telefone com DD. ex 46999010100");

                name.setVisibility(View.GONE);
                enviarName.setVisibility(View.GONE);
            }
        });

        enviarNumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ddi = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String number = phone.getText().toString().trim();
                if (ddi.equals("00")){
                    Toast.makeText(Login.this, "Selecione o País", Toast.LENGTH_SHORT).show();
                    spinner.requestFocus();
                    return;
                }
                if (number.isEmpty() || number.length() < 11 || number.equals("46999010101")){
                    phone.setError("Numero incorreto, tente novamente.");
                    phone.requestFocus();
                    return;
                }
                String phoneNumber =  "+" + ddi + number;

                spinner.setVisibility(View.GONE);
                phone.setVisibility(View.GONE);
                enviarNumero.setVisibility(View.GONE);

                setMesssage.setText("NÚMERO ENVIADO COM SUCESSO!\n\n Em até 60 segundos você receberá uma mensagem com um código de acesso, o sistema reconhecerá automaticamente, caso isso não aconteça você poderá digita-lo manualmente.");

                code.setVisibility(View.VISIBLE);
                enviarCode.setVisibility(View.VISIBLE);

                sendVerificationCode(phoneNumber);
            }
        });

        enviarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String acesskey = code.getText().toString().trim();
                if (!code.getText().toString().trim().isEmpty() || code.getText().toString().trim().length() == 6){
                    verifycode(acesskey);
                }else{
                    code.setError("Digite o código ou aguarde...");
                    code.requestFocus();
                }
            }
        });

    }

    public void registerCheck(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            if (currentUser.getPhoneNumber() != null){
                startActivity(new Intent(getBaseContext(), Noticias.class));
                finish();
            }else {
                startActivity(new Intent(getBaseContext(), Login.class));
                finish();
            }
        }
    }

    private void sendVerificationCode(String numero){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        }, 60000);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                numero,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String smsCode = phoneAuthCredential.getSmsCode();
            if (smsCode != null){
                verifycode(smsCode);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Login.this, " \n"  + "\n Tente novamente!", Toast.LENGTH_SHORT).show();
            spinner.setVisibility(View.VISIBLE);
            phone.setVisibility(View.VISIBLE);
            enviarNumero.setVisibility(View.VISIBLE);

            code.setVisibility(View.GONE);
            enviarCode.setVisibility(View.GONE);
        }
    };

    private void verifycode(String idcode){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, idcode);
        signInWhitCredential(credential);
    }

    private void signInWhitCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser != null){
                                finalizarCadastro("insert", currentUser.getPhoneNumber());
                            }

                        }
                    }
                });
    }

    public void finalizarCadastro(String key, String telefone){
        String id = "null", mail = "null", nasci = "null";
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Usuario> call = apiInterface.updateProfile(key, id, profileName, telefone, mail, nasci);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Toast.makeText(Login.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Noticias.class));
                finish();
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(Login.this, "Não foi possivel cadastrar", Toast.LENGTH_SHORT).show();
            }
        });

    }
}