package com.ramattecgmail.rafah.herdeirosapp.Configs;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by rafah on 30/09/2017.
 */

public final class ConfiguracaoFirebase {

    private static FirebaseDatabase database;
    private static FirebaseStorage storage;
    static DatabaseReference reference;
    private static StorageReference storageReference;
    private static FirebaseAuth autenticacao;

    public ConfiguracaoFirebase(){

    }

    public static DatabaseReference getFirebaseReference(){
        if (reference == null){
            reference = FirebaseDatabase.getInstance().getReference();
        }

        return reference;
    }

    public static StorageReference getStorageReference(){
        if (storageReference == null){
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReferenceFromUrl("gs://herdeirosapp.appspot.com/");
            //.child("GALERIA");
        }
        return storageReference;
    }

    public static FirebaseDatabase getDatabase(){
        if (database == null){
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
        return database;
    }

    //Fazendo validação de autenticação e login
    public static FirebaseAuth getFirebaseAutenticacao(){
        if (autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }

        return autenticacao;
    }
}
