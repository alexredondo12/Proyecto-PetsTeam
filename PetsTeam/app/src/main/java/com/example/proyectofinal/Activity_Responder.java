package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class Activity_Responder extends AppCompatActivity {

    DatabaseReference mDatabase;
    Email e = new Email();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder);

        EditText mensaje2 = (EditText)findViewById(R.id.mensaje2) ;
        TextView usu = (TextView)findViewById(R.id.para2) ;
        TextView nombreProd = (TextView)findViewById(R.id.nombreProd3) ;
        ImageView foto = (ImageView) findViewById(R.id.foto_prod2) ;

        //intent del buzon
        Intent i = getIntent();
        String fromB =getIntent().getExtras().getString("Bfrom");
        usu.setText(fromB);
        //asunto
        String asuntoB =getIntent().getExtras().getString("Basunto");
        nombreProd.setText(asuntoB);
        //mensaje
        /*String mensajeB =getIntent().getExtras().getString("Bmensaje");
        mensaje2.setText(mensajeB);*/
        //foto
        String f =getIntent().getExtras().getString("Bfoto");
        Glide.with(this).load(f).into(foto);

        Button enviar = (Button) findViewById(R.id.btnEnviar);
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(mensaje2.getText())) {
                    mensaje2.setError("Debe escribir un mensaje");
                    return;
                } else {

                    //insertamos mensaje en firebase
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    Mensaje m = new Mensaje();
                    m.setId(UUID.randomUUID().toString());
                    m.setFrom(e.getEmail());
                    m.setTo(fromB);
                    m.setMensaje(mensaje2.getText().toString());
                    m.setAsunto(asuntoB);
                    m.setFoto(f);
                    mDatabase.child("Mensaje").child(m.getId()).setValue(m);

                }
                //limpiamos campo de mensaje
                mensaje2.setText("");
                Intent volverBuzon = new Intent(view.getContext(), PaginaPrincipal.class);
                startActivity(volverBuzon);
            }
        });

    }
}