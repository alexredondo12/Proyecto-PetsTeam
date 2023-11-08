package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class Activity_enviar extends AppCompatActivity {

    DatabaseReference mDatabase;
    Email e = new Email();
    Intent i;

    private static final String CANAL_ID = "NOTIFICACION";
    private static final int NOTIFICACION_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar);
        EditText mensaje = (EditText) findViewById(R.id.mensaje2);

        Intent intent = getIntent();
        //poner email a quién va dirigido el mensaje
        String from = getIntent().getExtras().getString("to");
        TextView usu = (TextView) findViewById(R.id.para2);
        usu.setText(from);

        //poner asunto
        String asunto = getIntent().getExtras().getString("nombre");
        TextView nombreProd = (TextView) findViewById(R.id.nombreProd2);
        nombreProd.setText(asunto);

        //poner foto
        String f = getIntent().getExtras().getString("foto");
        ImageView foto = (ImageView) findViewById(R.id.foto_prod);
        Glide.with(this).load(f).into(foto);

        Button enviar = (Button) findViewById(R.id.btnEnviar3);

        if (from.equals(e.getEmail())) {
            Toast.makeText(this, " ¡Este es tu producto! ", Toast.LENGTH_SHORT).show();
            i = new Intent(this, PaginaPrincipal.class);
            startActivity(i);


        } else {
            enviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (TextUtils.isEmpty(mensaje.getText())) {
                        mensaje.setError("Debe escribir un mensaje");
                        return;
                    } else {

                        //insertamos mensaje en firebase
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        Mensaje m = new Mensaje();
                        m.setId(UUID.randomUUID().toString());
                        m.setFrom(e.getEmail());
                        m.setTo(from);
                        m.setMensaje(mensaje.getText().toString());
                        m.setAsunto(asunto);
                        m.setFoto(f);
                        mDatabase.child("Mensaje").child(m.getId()).setValue(m);
                        //creamos canal
                        crearNotificacion();
                        //creamos notificacion
                        crearNot();

                    }
                    //limpiamos campo de mensaje
                    mensaje.setText("");

                    i = new Intent(view.getContext(), PaginaPrincipal.class);
                    startActivity(i);
                }
            });

        }
    }

    private void crearNot() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CANAL_ID);
        builder.setSmallIcon(R.drawable.ic_home_black_24dp);
        builder.setContentTitle("PETS TEAM");
        builder.setContentText("Has enviado un mensaje.");
        builder.setColor(Color.WHITE);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        builder.setLights(Color.GREEN, 1000, 1000);
        builder.setVibrate(new long[]{100});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }

    private void crearNotificacion() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "notificacion";
            NotificationChannel notificacion = new NotificationChannel(CANAL_ID, name, NotificationManager.IMPORTANCE_HIGH);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(notificacion);


        }
    }
}