package com.ramattecgmail.rafah.herdeirosapp.Models;

import com.google.firebase.database.DatabaseReference;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;

/**
 * Created by rafah on 07/11/2017.
 */

public class Galeria {
    //ATRIBUTOS
    private String album;
    private String foto;
    private String user;

    public Galeria(){

    }

    public void salvarFotoDB(){

    }

    public void salvarGaleria(){

        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseReference();
        reference.child("GALERIA")
                .child(album.toUpperCase())
                .setValue(this);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

}
