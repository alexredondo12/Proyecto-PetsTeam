package com.example.proyectofinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdapterMensajes extends RecyclerView.Adapter<HolderMensaje> {

    private List<MensajeRecibir> listMensaje = new ArrayList<>();
    private Context c;

    public AdapterMensajes( Context c) {

        this.c = c;
    }

    public void addMensaje(MensajeRecibir m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @NonNull
    @Override
    public HolderMensaje onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_mensaje_chat,parent,false);

        return new HolderMensaje(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderMensaje holder, int position) {
        holder.getNombre().setText(listMensaje.get(position).getNombre());
        holder.getMensaje().setText(listMensaje.get(position).getMensaje());

        Long codHora = listMensaje.get(position).getHora();
        Date d = new Date(codHora);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a"); // a pm   o am

        holder.getHora().setText(sdf.format(d));

        if (listMensaje.get(position).getFotoPerfil().isEmpty()){
            holder.getFotoMensaje().setImageResource(R.mipmap.ic_launcher);

        }else{
            Glide.with(c) .load(listMensaje.get(position).getFotoPerfil()).into(holder.getFotoMensaje());
        }
    }

    @Override
    public int getItemCount() {

        return listMensaje.size();
    }
}
