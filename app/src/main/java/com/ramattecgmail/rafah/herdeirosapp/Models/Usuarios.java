package com.ramattecgmail.rafah.herdeirosapp.Models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rafah on 02/10/2017.
 */

public class Usuarios {
    //ATRIBUTOS
    String id;
    String nome;
    //String telefone;
    String email;
    String foto_url;
    String nivel;
    String apelido;
    //String grupo;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    public Usuarios(){

    }

    public void salvar(){
        //String cripto = CriptografiaBase64.criptografarTelefone(telefone);
        DatabaseReference banco = ConfiguracaoFirebase.getFirebaseReference();
        banco.child("USUARIOS").child(id).setValue(this);
    }

    public void atualizar(String id){
        //CRIANDO A REFERENCIA AO DADO QUE SERÁ ATUALIZADO
        databaseReference = ConfiguracaoFirebase.getFirebaseReference().child("USUARIOS")
                .child(id);

        //CRIANDO O HASHMAP QUE ARMAZENA OS VALORES
        Map<String, Object> map = new HashMap<>();
        map.put("nome", getNome());
        map.put("email", getEmail());
        //map.put("telefone", getTelefone());
        //map.put("fotoPerfil", getFoto_url());
        map.put("nivel", getNivel());
        map.put("apelido", getApelido());

        //COMANDO QUE MANDA ATUALIZAÇÃO PARA O FIREBASE
        databaseReference.updateChildren(map);

    }

    public final void uploadFoto(byte[] byteFoto){

    }

    public void recuperarFoto(final Context context, final ImageView imPerfil, String cripto) {
        //Recuperando a imagem no storage
        storageReference = ConfiguracaoFirebase.getStorageReference().child(cripto);

        //Recuperando ela em formato arquivo
        try{
            final File arquivo = File.createTempFile("ImagemPeerfil", "png");
            storageReference.getFile(arquivo).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Arredondando a imagem com Picasso
                    Transformation transformation = new RoundedTransformationBuilder()
                            .cornerRadius(30)
                            .oval(true)
                            .build();

                    //Recuperando a imagem com picasso
                    Picasso.with(context)
                            .load(arquivo)
                            .fit()
                            .transform(transformation)
                            .into(imPerfil);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (storageReference.getFile(arquivo) == null){
                        Toast.makeText(context, "Não foi possivel recuperar a foto, favor verificar " +
                                "o acesso a internet e tentar novamente!", Toast.LENGTH_LONG).show();
                        Log.i("Erro", e.getMessage());
                    }
                }
            });

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFoto_url() {
        return foto_url;
    }

    public void setFoto_url(String foto_url) {
        this.foto_url = foto_url;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }
}
