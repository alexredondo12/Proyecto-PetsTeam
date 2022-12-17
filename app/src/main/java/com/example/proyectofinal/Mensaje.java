package com.example.proyectofinal;

public class Mensaje {

    String id;
    String from;
    String to ;
    String mensaje;
    String asunto;
    String foto;

    public Mensaje() {
    }

    public Mensaje(String asunto, String from, String id, String mensaje, String to, String foto) {
        this.asunto = asunto;
        this.from = from;
        this.id = id;
        this.mensaje = mensaje;
        this.to = to;
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
