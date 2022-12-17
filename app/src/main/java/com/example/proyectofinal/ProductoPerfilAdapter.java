package com.example.proyectofinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ProductoPerfilAdapter extends RecyclerView.Adapter<ProductoPerfilAdapter.productoView> {

    private ArrayList<Producto>productoArrayList = new ArrayList<>();
    private Context context;
    DatabaseReference mDatabase;


    public ProductoPerfilAdapter(Context context, ArrayList<Producto> productoArrayList) {
        this.productoArrayList = productoArrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public productoView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_personalizado2, viewGroup, false);
        return new productoView(view);
    }

    @Override
    public void onBindViewHolder(productoView productoView, int i) {
        Producto prod = productoArrayList.get(i);
        productoView.txtNombre.setText(prod.getNombre());
        productoView.txtTalla.setText(prod.getTalla());
        productoView.txtPrecio.setText((String.valueOf(prod.getPrecio())));
        Glide.with(context).load(prod.getUrlFoto()).into(productoView.foto);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        productoView.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(view.getContext());
                alerta.setTitle("Mensaje");
                alerta.setMessage("Está seguro de que desea Eliminar " + prod.getNombre() + " ? ");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child("Producto").child(prod.getId()).removeValue();

                        Intent i2 = new Intent(view.getContext(), PaginaPrincipal.class);
                        context.startActivity(i2);

                    }
                });
                alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alerta.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return productoArrayList.size();
    }


    public class productoView extends RecyclerView.ViewHolder {

        private TextView txtNombre, txtTalla, txtPrecio, txtDescrip, txtFoto;
        private Button comprar, eliminar;
        private ImageView foto;

        public productoView(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.Nombre);
            txtTalla = itemView.findViewById(R.id.Talla);
            txtPrecio = itemView.findViewById(R.id.Precio);
            txtDescrip = itemView.findViewById(R.id.Descripcion);
            foto = itemView.findViewById(R.id.txt_foto1);
            eliminar = itemView.findViewById(R.id.btnEliminar);


        }
    }
}
