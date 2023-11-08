package com.example.proyectofinal;

public class Email {

    public static String email;

    public Email( ) {
    }

    public Email(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }


    public String toString() {
        return "Email{" + "email='" + email + '\'' + '}';
    }
}
