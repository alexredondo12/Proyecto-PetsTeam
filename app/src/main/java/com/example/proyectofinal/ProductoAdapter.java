package com.example.proyectofinal;


import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.productoView> {

    private List<Producto>productoList = new ArrayList<>();
    private Context context;
    Email e = new Email();
    DatabaseReference mDatabase;

    public ProductoAdapter(Context context, ArrayList<Producto> productoList) {
        this.productoList = productoList;
        this.context = context;
    }

    @NonNull
    @Override
    public productoView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_personalizado, viewGroup, false);
        return new productoView(view);
    }

    @Override
    public void onBindViewHolder(productoView productoView, int i) {
        Producto producto = productoList.get(i);
        productoView.txtNombre.setText(producto.getNombre());
        productoView.txtTalla.setText(producto.getTalla());
        productoView.txtPrecio.setText((String.valueOf(producto.getPrecio())));
        productoView.txtDescrip.setText(producto.getDescripcion());
        productoView.txtcorreo.setText(producto.getUsuario());
        Glide.with(context).load(producto.getUrlFoto()).into(productoView.foto);

        productoView.comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), Activity_enviar.class);
                i.putExtra("to", producto.getUsuario());
                i.putExtra("nombre", producto.getNombre());
                i.putExtra("foto", producto.getUrlFoto());
                view.getContext().startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public class productoView extends RecyclerView.ViewHolder {

        private TextView txtNombre, txtTalla, txtPrecio, txtDescrip, txtFoto, txtcorreo;
        private Button comprar, eliminar;
        private ImageView foto;

        public productoView(@NonNull View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.Nombre);
            txtTalla = itemView.findViewById(R.id.Talla);
            txtPrecio = itemView.findViewById(R.id.Precio);
            txtDescrip = itemView.findViewById(R.id.Descripcion);
            txtcorreo = itemView.findViewById(R.id.correo);
            foto = itemView.findViewById(R.id.txt_foto1);
            comprar = itemView.findViewById(R.id.btnEliminar);

        }
    }
}
