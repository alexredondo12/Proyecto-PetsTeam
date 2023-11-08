package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Buzon extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Mensaje> mensajeArrayList;
    private MensajeAdapter mensajeAdapter;
    DatabaseReference mDatabase;
    Email e = new Email();
    private Context context = this;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzon);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mensajeArrayList = new ArrayList<>();
        mensajeAdapter = new MensajeAdapter(this, mensajeArrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        getProdFromFirebase();

    }

    private void getProdFromFirebase() {
        Query query = mDatabase.child("Mensaje").orderByChild("to").equalTo(e.getEmail());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String asunto = ds.child("asunto").getValue().toString();
                        String from = ds.child("from").getValue().toString();
                        String mensaje = ds.child("mensaje").getValue().toString();
                        String id = ds.child("id").getValue().toString();
                        String to = ds.child("to").getValue().toString();
                        String fo = ds.child("foto").getValue().toString();
                        mensajeArrayList.add(new Mensaje(asunto,from,id, mensaje, to,fo));

                    }
                    mensajeAdapter = new MensajeAdapter(context, mensajeArrayList);
                    recyclerView.setAdapter(mensajeAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}