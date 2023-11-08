package com.example.proyectofinal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MensajeAdapter extends RecyclerView.Adapter<MensajeAdapter.mensajeView> {

    private ArrayList<Mensaje> mensajeList = new ArrayList<>();
    private Context context;
    DatabaseReference mDatabase;


    public MensajeAdapter(Context context, ArrayList<Mensaje> mensajeList) {
        this.mensajeList = mensajeList;
        this.context = context;

    }

    @NonNull
    @Override
    public mensajeView onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mensaje, viewGroup, false);
        return new mensajeView(view);
    }

    @Override
    public void onBindViewHolder(mensajeView productoView, int i) {
        Mensaje mensaje = mensajeList.get(i);
        productoView.txtFrom.setText(mensaje.getFrom());
        productoView.txtMensaje.setText(mensaje.getMensaje());
        productoView.txtAsunto.setText(mensaje.getAsunto());
        Glide.with(context).load(mensaje.getFoto()).into(productoView.foto);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        productoView.btnResponder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), Activity_Responder.class);
                i.putExtra("Bfrom",mensaje.getFrom());
                i.putExtra("Basunto",mensaje.getAsunto());
                i.putExtra("Bmensaje",mensaje.getMensaje());
                i.putExtra("Bfoto",mensaje.getFoto());
                view.getContext().startActivity(i);
            }
        });

        productoView.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(view.getContext());
                alerta.setTitle("Mensaje");
                alerta.setMessage("Está seguro de que desea eliminar " + mensaje.getAsunto() + " ? ");
                alerta.setCancelable(false);
                alerta.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDatabase.child("Mensaje").child(mensaje.getId()).removeValue();
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
        return mensajeList.size();
    }

    public class mensajeView extends RecyclerView.ViewHolder {

        private TextView txtFrom, txtTO, txtMensaje, txtAsunto;
        private ImageButton btnResponder, btnEliminar;
        private ImageView foto;

        public mensajeView(@NonNull View itemView) {
            super(itemView);

            txtFrom = itemView.findViewById(R.id.txt_from);
            txtMensaje = itemView.findViewById(R.id.txt_mensaje);
            txtAsunto= itemView.findViewById(R.id.txt_asunto);
            btnResponder = itemView.findViewById(R.id.btnResponder);
            btnEliminar = itemView.findViewById(R.id.btnEliminar2);
            foto = itemView.findViewById(R.id.fotoprod);
        }
    }
}
