package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CuartoFragment extends Fragment {

    Email e= new Email();
    RecyclerView recyclerView;
    ArrayList<Producto> prodArrayList;
    private ProductoPerfilAdapter productoAdapter;
    DatabaseReference mDatabase;

    public CuartoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cuarto, container, false);

        //boton mail
        Button mail = (Button)view.findViewById(R.id.b_mail);

        Button btnEliminar = (Button)view.findViewById(R.id.btnEliminar);

        //PASAMOS EL EMAIL DEL LOGIN
        TextView a = (TextView) view.findViewById(R.id.txtmail);
        a.setText(e.getEmail());

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        prodArrayList = new ArrayList<>();
        productoAdapter = new ProductoPerfilAdapter(getContext(), prodArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        getProductosFromFirebase();

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), Buzon.class);
                startActivity(i);
        }
        });
        //boton cerrarsesion
        Button btnCerrar = view.findViewById(R.id.btnCerrarSesion);
        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
            }
        });


        return view;
    }

    private void getProductosFromFirebase(){
        Query query = mDatabase.child("Producto").orderByChild("usuario").equalTo(e.getEmail());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.child("id").getValue().toString();
                        String nom = ds.child("nombre").getValue().toString();
                        String des = ds.child("descripcion").getValue().toString();
                        String pre = ds.child("precio").getValue().toString();
                        String ta = ds.child("talla").getValue().toString();
                        String fo = ds.child("urlFoto").getValue().toString();
                        String us = ds.child("usuario").getValue().toString();

                        prodArrayList.add(new Producto(id,nom, des, pre, ta,fo, us));
                    }
                    productoAdapter = new ProductoPerfilAdapter(getContext(), prodArrayList);
                    recyclerView.setAdapter(productoAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}