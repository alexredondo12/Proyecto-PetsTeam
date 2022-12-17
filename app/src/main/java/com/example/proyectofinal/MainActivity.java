package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText edEmail;
    private EditText edPwd;
    private Button btnLogin;
    private Button btnReg;
    private FirebaseAuth fAuth;
    private Button btnCambio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(2000);
            setTheme(R.style.SpashTheme);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edEmail = findViewById(R.id.edEmailReg);
        edPwd = findViewById(R.id.edPwd);

        //iniciar la firebase
        fAuth = FirebaseAuth.getInstance();


        btnLogin = findViewById(R.id.btnIniciar);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edEmail.getText().toString().trim();
                String pwd = edPwd.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    edEmail.setError("El correo esta vacío");
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    edPwd.setError("La contraseña está vacía");
                    return;
                }

                if (pwd.length() < 6) {
                    edPwd.setError("La contraseña ha de tener mas de 6 caracteres");
                    return;
                }

                //PASAR EMAIL AL PERFIL
                Email l = new Email(email);
                l.setEmail(email);

                fAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = fAuth.getCurrentUser();

                        /*if (!user.isEmailVerified()) {
                            Toast.makeText(com.example.proyectofinal.MainActivity.this, "ERROR, NO HA VERIFICADO SU CUENTA! ", Toast.LENGTH_SHORT).show();
                        } else */if (task.isSuccessful()) {

                            startActivity(new Intent(getApplicationContext(), PaginaPrincipal.class));
                        } else {
                            Toast.makeText(com.example.proyectofinal.MainActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        btnReg = findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registrarse(v);
            }
        });

        btnCambio = findViewById(R.id.btnCambio);
        btnCambio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ResetPaswd.class);
                startActivity(i);
            }
        });

    }

    public void registrarse(View v) {
        Intent i = new Intent(this, Registro.class);
        startActivity(i);
    }


}