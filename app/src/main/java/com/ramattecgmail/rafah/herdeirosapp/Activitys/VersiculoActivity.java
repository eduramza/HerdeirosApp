package com.ramattecgmail.rafah.herdeirosapp.Activitys;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Configs.SharedPreferencias;
import com.ramattecgmail.rafah.herdeirosapp.Fragments.Alerts.VersiculoFragment;
import com.ramattecgmail.rafah.herdeirosapp.Models.Usuarios;
import com.ramattecgmail.rafah.herdeirosapp.Models.Versiculos;
import com.ramattecgmail.rafah.herdeirosapp.R;

import java.util.ArrayList;
import java.util.Random;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class VersiculoActivity extends AppCompatActivity {
    // LÓGICA
    /*
    faço o download de todos os versiculos
    salvo eles em uma array
    gero um numero aleatorio
    com o numero aleatório pego o valor na posição da array
    faço um novo select no firebase ordenando por esse valor
     */
    private TextView tvMensagem, tvAutor, tvVersiculo;
    private ImageView imShare, imPerfil, imLike;
    private Button btVersiculo, btAvancar;
    private DatabaseReference reference, usuarioRef;
    private ValueEventListener valueEventListener;
    private ArrayList<String> arrayVersiculos;
    private Random random;
    private int aleatorio;
    private boolean acaoCurtir;
    private String idVersiculo; //necessário para os likes e deslikes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versiculo);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //habilitando dados offline
        FirebaseDatabase database = ConfiguracaoFirebase.getDatabase();

        tvVersiculo = findViewById(R.id.tv_versiculo);
        tvMensagem = findViewById(R.id.tv_mensagem);
        tvAutor =  findViewById(R.id.tv_autor);
        imShare = findViewById(R.id.im_share_versiculo);
        imPerfil = findViewById(R.id.im_perfil_versiculo);
        btAvancar = findViewById(R.id.bt_avancar_versiculo);
        btVersiculo = findViewById(R.id.bt_add_versiculo);
        imLike  = findViewById(R.id.im_like_versiculo);

        //Configurnado a geração de versiculos
        arrayVersiculos = new ArrayList<>();
        random = new Random();

        /**************************AÇÕES DOS COMPONENTES DA TELA***************************/
        //AÇÃO DO BOTÃO COMPARTILHAR
        imShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Compartilhando com o zap
                Intent shared = new Intent(Intent.ACTION_SEND);
                shared.setType("text/plain");
                shared.putExtra(Intent.EXTRA_TEXT, tvMensagem.getText().toString()+" - "+tvVersiculo.getText().toString()
                        +"by: HerdeirosApp");
                shared.setPackage("com.whatsapp");
                startActivity(shared);
            }
        });

        btAvancar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VersiculoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btVersiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VersiculoFragment versiculo = new VersiculoFragment();
                versiculo.show(VersiculoActivity.this.getSupportFragmentManager(), "Versiculos");
            }
        });

        imLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favoritar();
            }
        });


         /**************************AÇÕES DOS COMPONENTES DA TELA***************************/

        //Ações do FABmenu
        /*fab.setMenuListener(new SimpleMenuListenerAdapter(){
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.fab_mn_compartilhar:
                        //Compartilhando com o zap
                        Intent shared = new Intent(Intent.ACTION_SEND);
                        shared.setType("text/plain");
                        shared.putExtra(Intent.EXTRA_TEXT, tvMensagem.getText().toString()+" - "+tvVersiculo.getText().toString()
                        +"by: HerdeirosApp");
                        shared.setPackage("com.whatsapp");
                        startActivity(shared);
                        return true;

                    case R.id.fab_mn_avancar:
                        Intent intent = new Intent(VersiculoActivity.this, MainActivity.class);
                        startActivity(intent);
                        return true;

                    case R.id.fab_mn_adicinarV:
                        VersiculoFragment versiculo = new VersiculoFragment();
                        versiculo.show(VersiculoActivity.this.getSupportFragmentManager(), "Versiculos");
                        return true;
                }
                return false;
            }
        });*/

        /*********************** PRIMEIRO TRATAMENTO DOS DADOS ******************/
        reference = ConfiguracaoFirebase.getFirebaseReference()
                .child("VERSICULOS");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Não recarregar se for uma atualziação de curtida
                if(acaoCurtir == false){
                    arrayVersiculos.clear();
                    //Listando os versiculos
                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        Versiculos versiculos = data.getValue(Versiculos.class);
                        arrayVersiculos.add(versiculos.getVersiculo());
                    }

                    //Gerando numero randomico
                    aleatorio = random.nextInt(arrayVersiculos.size());
                    //Função que traz o versiculo sorteado
                    setandoTextos(arrayVersiculos.get(aleatorio));

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    /******************************** METODOS ***********************************/
    @Override
    protected void onStart() {
        super.onStart();
        //Usada par impedir o recarregamento da pagina quando for clicado em curtir
        acaoCurtir = false;
        reference.addValueEventListener(valueEventListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //Usada par impedir o recarregamento da pagina quando for clicado em curtir
        acaoCurtir = false;
        reference.removeEventListener(valueEventListener);
    }

    public void setandoTextos(final String versiculo){

        //Guardando o ID do versiculo para likes e deslikes
        idVersiculo = versiculo;

        reference.orderByChild("versiculo")
                .equalTo(versiculo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            Versiculos versiculos = data.getValue(Versiculos.class);
                            tvMensagem.setText(versiculos.getMensagem());
                            tvVersiculo.setText(versiculos.getVersiculo());
                            //É PRECISO SEMPRE VOLTAR A IMAGEM PADRÃO CASO NÃO HAJA RETORNO
                            imPerfil.setImageResource(R.drawable.ic_face);
                            tvAutor.setText(versiculos.getUsuPostagem());

                            //Recuperando a foto do autor da postagem
                            Usuarios usuarios = new Usuarios();
                            usuarios.recuperarFoto(getApplicationContext(), imPerfil, versiculos.getUsuPostagem()+ "-perfil");

                            //Recuperar apelido do usuário que fez a postagem
                            recuperarUsuario(versiculos.getUsuPostagem());

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void recuperarUsuario(String idUsuario){

        usuarioRef = ConfiguracaoFirebase.getFirebaseReference()
                .child("USUARIOS")
                .child(idUsuario);

        //Recuperando do firebase
        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    //Salvando o apelido do usuário corretamente
                    Usuarios usuariosP = dataSnapshot.getValue(Usuarios.class);
                    tvAutor.setText(usuariosP.getApelido());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void favoritar(){

        Versiculos favoritar = new Versiculos();
        SharedPreferencias preferencias = new SharedPreferencias(getApplicationContext());

        Drawable drawable = imLike.getDrawable();
        Drawable favorite = getResources().getDrawable(R.drawable.ic_favorite_border);

        if (drawable.getConstantState().equals(favorite.getConstantState())){
            //Curtindo
            acaoCurtir = true;
            favoritar.curtirVersiculo(idVersiculo, preferencias.getCHAVE_ID());
            imLike.setImageResource(R.drawable.ic_favorite);

        } else {
            //Descurtindo
            acaoCurtir = true;
            favoritar.descurtirVersiculo(idVersiculo, preferencias.getCHAVE_ID());
            imLike.setImageResource(R.drawable.ic_favorite_border);

        }
    }

    public void revealEffect(){
        if (Build.VERSION.SDK_INT > 20){
            /*int cx = bnItemNovoV.getMeasuredWidth() / 2;
            int cy = bnItemNovoV.getMeasuredHeight() / 2;

            int radius = Math.max(bnItemNovoV.getWidth(), bnItemNovoV.getHeight());

            Animator animator = ViewAnimationUtils.createCircularReveal(bnItemNovoV, cx, cy, 0, radius);
            animator.setDuration(1500);
            bnItemNovoV.setVisibility(View.VISIBLE);
            animator.start();*/
        }
    }
}
