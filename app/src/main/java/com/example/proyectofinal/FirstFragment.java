package com.example.proyectofinal;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Producto> productoArrayList;
    private ProductoAdapter productoAdapter;
    DatabaseReference mDatabase;
    ImageView f;
    SearchView sv;

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        TextView n = view.findViewById(R.id.Nombre);
        TextView d = view.findViewById(R.id.Descripcion);
        TextView p = view.findViewById(R.id.Precio);
        TextView t = view.findViewById(R.id.Talla);
        f = view.findViewById(R.id.txt_foto1);

        //buscador
        sv=view.findViewById(R.id.svBuscar);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        productoArrayList = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        getProductoFromFirebase();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                buscar(s);

                return true;
            }
        });

        return view;
    }

    private void buscar(String s) {
        ArrayList<Producto> miProd = new ArrayList<>();
        for (Producto obj: productoArrayList){
            if (obj.getNombre().toLowerCase().contains(s.toLowerCase())){
                miProd.add(obj);
            }
            ProductoAdapter prodBuscar = new ProductoAdapter(getContext(),miProd);
            recyclerView.setAdapter(prodBuscar);
        }

    }

    private void getProductoFromFirebase() {
        mDatabase.child("Producto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.child("id").getValue().toString();
                        String nom = ds.child("nombre").getValue().toString();
                        String des = ds.child("descripcion").getValue().toString();
                        String pre = ds.child("precio").getValue().toString();
                        String ta = ds.child("talla").getValue().toString();
                        String us = ds.child("usuario").getValue().toString();
                        String fo = ds.child("urlFoto").getValue().toString();
                        productoArrayList.add(new Producto(id, nom, des, pre, ta, fo, us));
                    }
                    productoAdapter = new ProductoAdapter(getContext(), productoArrayList);
                    recyclerView.setAdapter(productoAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}