package com.example.proyectofinal;

public class MensajeChat {
    private String mensaje;
    private String url_foto;
    private String nombre;
    private String fotoPerfil;
    private String type_mensaje;


    public MensajeChat() {
    }

    public MensajeChat(String mensaje, String nombre, String fotoPerfil, String type_mensaje) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.type_mensaje = type_mensaje;

    }

    public MensajeChat(String mensaje, String url_foto, String nombre, String fotoPerfil, String type_mensaje) {
        this.mensaje = mensaje;
        this.url_foto = url_foto;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.type_mensaje = type_mensaje;

    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getType_mensaje() {
        return type_mensaje;
    }

    public void setType_mensaje(String type_mensaje) {
        this.type_mensaje = type_mensaje;
    }


    public String getUrl_foto() {
        return url_foto;
    }

    public void setUrl_foto(String url_foto) {
        this.url_foto = url_foto;
    }
}
