package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPaswd extends AppCompatActivity {

    private EditText edText;
    private Button btnPsw;
    private String email = "";
    private FirebaseAuth auth;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_paswd);

        edText = findViewById(R.id.edTxtEmail);
        btnPsw = findViewById(R.id.btnResetPsw);

        auth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        btnPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edText.getText().toString();
                if (!email.isEmpty()) {
                    mDialog.setMessage("Espera un momento...");
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                    resetPassword();
                } else {
                    Toast.makeText(ResetPaswd.this, "Debe escribir un email", Toast.LENGTH_SHORT).show();
                }
                mDialog.dismiss();
                Intent i = new Intent(ResetPaswd.this, MainActivity.class);
                startActivity(i);

            }
        });

    }

    private void resetPassword() {
        auth.setLanguageCode("es");
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ResetPaswd.this, "Se ha enviado un correo electronico", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(ResetPaswd.this, "No se pudo enviar el correo de restablecer la contraseña", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}