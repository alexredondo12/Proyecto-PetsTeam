package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Registro extends AppCompatActivity {

    private Button btnVolver;
    private Button btnRegistro;

    private EditText edNombre, edEmail, edPsdw, edPsdw2, edDni;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edNombre = findViewById(R.id.edNombreComp);
        edEmail = findViewById(R.id.edEmailReg);
        edPsdw = findViewById(R.id.edPasw);
        edPsdw2 = findViewById(R.id.edPasw2);
        edDni = findViewById(R.id.edDNI);

        firebaseAuth = FirebaseAuth.getInstance();

        btnRegistro = findViewById(R.id.btnIniciar);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nombre = edNombre.getText().toString().trim();
                final String email = edEmail.getText().toString().trim();
                final String dni = edDni.getText().toString().trim();
                String password = edPsdw.getText().toString().trim();
                String password2 = edPsdw2.getText().toString().trim();

                if (TextUtils.isEmpty(nombre)) {
                    edNombre.setError("El campo Nombre está vacío");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    edEmail.setError("El campo Email está vacío");
                    return;
                }

                {
                    if (TextUtils.isEmpty(dni)) {
                        edDni.setError("El campo DNI está vacío");
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        edPsdw.setError("El campo Contraseña está vacío");
                        return;
                    }

                    if (password.length() < 6) {
                        edPsdw.setError("La contraseña debe tener más de 6 caracteres");
                        return;
                    }

                    if (TextUtils.isEmpty(password2)) {
                        edPsdw2.setError("Rellene este campo");
                        return;
                    }

                    if (!password2.equals(password)) {
                        edPsdw2.setError("Las contraseñas no coinciden");
                        return;
                    }

                    //PASAR EMAIL AL PERFIL
                    Email l = new Email(email);
                    l.setEmail(email);

                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Registro.this, "Usuario Creado.", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                mDatabase.child(user.getUid()).setValue(nombre);
                                mDatabase.child(user.getUid()).setValue(dni);

                                user.sendEmailVerification();

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(Registro.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver(v);
            }
        });
    }

    public void volver(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}

