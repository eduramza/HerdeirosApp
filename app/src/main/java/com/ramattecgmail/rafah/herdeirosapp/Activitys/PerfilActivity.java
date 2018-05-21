package com.ramattecgmail.rafah.herdeirosapp.Activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Configs.SharedPreferencias;
import com.ramattecgmail.rafah.herdeirosapp.Models.Usuarios;
import com.ramattecgmail.rafah.herdeirosapp.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PerfilActivity extends AppCompatActivity {
    //ATRIBUTOS
    private static final int RC_IN_GALERY = 1;
    private ProgressBar pbPerfil;
    private RoundedImageView imPerfil;
    private EditText etNome, etApelido, etCargo;
    private boolean edit = false;
    private StorageReference storage;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    public String fotoID;
    public SharedPreferencias usuarioPreferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //INSTANCIANDO COMPONENTES
        etNome = findViewById(R.id.et_nome_perfil);
        etApelido = findViewById(R.id.et_apelido_perfil);
        etCargo = findViewById(R.id.et_cargo_perfil);
        imPerfil = findViewById(R.id.img_perfil_perfil);
        pbPerfil = findViewById(R.id.pb_foto_perfil);

        //Para recuperar os dados atuais do usuário
        usuarioPreferencias = new SharedPreferencias(getApplicationContext());
        fotoID = usuarioPreferencias.getCHAVE_ID() + "-perfil";

        Usuarios usuarioFoto = new Usuarios();
        usuarioFoto.recuperarFoto(getApplicationContext(), imPerfil, fotoID);

        etNome.setText(usuarioPreferencias.getCHAVE_NOME());

        //Trabalhando com o FAB e suas funções
        final FloatingActionButton fab = findViewById(R.id.fab_perfil);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {

                if (edit == false){
                    //Editando
                    fab.setImageResource(R.drawable.ic_action_tick);
                    etNome.setEnabled(true);
                    etNome.setBackgroundResource(R.drawable.layout_shadow);
                    etApelido.setEnabled(true);
                    etApelido.setBackgroundResource(R.drawable.layout_shadow);
                    edit = true;

                } else {
                    //SALVANDO OS DADOS EDITADOS
                    Usuarios usuarios = new Usuarios();
                    usuarios.setApelido(etApelido.getText().toString());
                    usuarios.setNome(etNome.getText().toString());
                    //usuarios.setNivel(etCargo.getText().toString());
                    usuarios.setEmail(usuarioPreferencias.getCHAVE_EMAIL());
                    usuarios.atualizar(usuarioPreferencias.getCHAVE_ID());

                    fab.setImageResource(R.drawable.ic_action_edit);
                    etNome.setEnabled(false);
                    etApelido.setEnabled(false);
                    etNome.setBackgroundResource(R.drawable.edits_padrao);
                    etApelido.setBackgroundResource(R.drawable.edits_padrao);
                    edit = false;
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /****** SESSÃO USUÁRIO NO REALTIME DATABASE *******/
        firebase = ConfiguracaoFirebase.getFirebaseReference()
                .child("USUARIOS")
                .child(usuarioPreferencias.getCHAVE_ID());

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //RECUPERANDO DADOS DO USUÁRIO NO FIREBASE
                if (dataSnapshot != null){
                    Usuarios fireUser = dataSnapshot.getValue(Usuarios.class);

                    if (usuarioPreferencias.getCHAVE_NOME().toLowerCase().contains("lucas gomes")
                            || usuarioPreferencias.getCHAVE_NOME().toLowerCase().contains("taíza")
                            || usuarioPreferencias.getCHAVE_NOME().toLowerCase().contains("will")
                            || usuarioPreferencias.getCHAVE_NOME().toLowerCase().contains("daniela")
                            || usuarioPreferencias.getCHAVE_NOME().toLowerCase().contains("lucas")
                            || usuarioPreferencias.getCHAVE_NOME().toLowerCase().contains("brand")
                            || usuarioPreferencias.getCHAVE_NOME().toLowerCase().contains("daniele")
                            || usuarioPreferencias.getCHAVE_NOME().toLowerCase().contains("taíza")){
                        fireUser.setNivel("Lider");
                    } else if (usuarioPreferencias.getCHAVE_NOME().toLowerCase().contains("adriel")){
                        fireUser.setNivel("Regente");
                    } else {
                        fireUser.setNivel("Membro");
                    }

                    /***********************
                     *
                     *
                     *    A CONFIGURAÇÃO DE MEMBRO AINDA NÃO ESTÁ FUNCIONANDO
                     *    NA VERDADE ESTÁ SENDO FIXADA PELA REGRA ACIMA
                     */
                    etApelido.setText(fireUser.getApelido());
                    etCargo.setText(fireUser.getNivel());
                    etNome.setText(fireUser.getNome());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        /****** FIM DA SESSÃO USUÁRIO NO REALTIME DATABASE *******/
        /****************** INICIO CONFIGURAÇÃO PARA O STORAGE **************/
        storage = ConfiguracaoFirebase.getStorageReference().child(fotoID);
        /****************** FIM CONFIGURAÇÃO PARA O STORAGE **************/
        imPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });
    }

    public void abrirGaleria(){

        //ABRINDO A GALERIA DE IMAGENS
        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galeria, RC_IN_GALERY);

    }

    //TRATANDO O RETORNO DA GAERIA
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Testando o processo de retorno dos dados
        //Se o código de requisição é igua ao passado na função abrirGaleria, o resultado for OK
        //e trouxer pelo menos um dado
        if (requestCode == RC_IN_GALERY && resultCode == RESULT_OK && data != null){

            //Recuperando o local na memoria da foto
            Uri endImagem = data.getData();

            //Recuperando a imagem em si
            try{
                Bitmap imagemG = MediaStore.Images.Media.getBitmap(getContentResolver(), endImagem);

                //Comprimindo a imagem em formato PNG
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imagemG.compress(Bitmap.CompressFormat.PNG, 80, stream);

                //Convertendo em Bytes para upload no Firebase Store
                byte[] byteImagem = stream.toByteArray();

                //Fazendo upload
                //Usuarios usuariosFoto = new Usuarios().uploadFoto(byteImagem);
                uploadImagem(byteImagem);

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void uploadImagem(byte[] byteFoto){
        //O CÓDIGO ABAIXO FAZ O UPLOAD COMPLETO DE UMA IMAGEM NO FOREBASE STORAGE
        UploadTask uploadTask = storage.putBytes(byteFoto);

        //TRABALHANDO AS AÇÕES DE UPLOAD
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                exibirProgress(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PerfilActivity.this, "Não foi possível atualizar a foto. "+
                        "Por favor, verifique sua conexão com a internet e tente novamente!", Toast.LENGTH_SHORT).show();

                exibirProgress(false);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                exibirProgress(false);
                Toast.makeText(PerfilActivity.this, "Foto atualziada com sucesso!", Toast.LENGTH_SHORT).show();

                //Atualizando os dados do firebase
                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null){
                            Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);
                            usuarios.setFoto_url(fotoID);
                            usuarios.atualizar(usuarioPreferencias.getCHAVE_ID());

                            //Recuperando a foto na tela
                            usuarios.recuperarFoto(getApplicationContext(), imPerfil, fotoID);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private void exibirProgress(boolean exibir) {
        pbPerfil.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }

}
