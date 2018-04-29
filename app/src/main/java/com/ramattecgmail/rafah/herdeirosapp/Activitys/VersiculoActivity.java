package com.ramattecgmail.rafah.herdeirosapp.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Fragments.VersiculoFragment;
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
    TextView tvMensagem;
    TextView tvAutor;
    TextView tvVersiculo;
    DatabaseReference reference;
    ValueEventListener valueEventListener;
    ArrayList<String> arrayVersiculos;
    Random random;
    int aleatorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versiculo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //habilitando dados offine
        FirebaseDatabase database = ConfiguracaoFirebase.getDatabase();

        //Button btContinuar = (Button) findViewById(R.id.bt_continuar);
        FabSpeedDial fab = (FabSpeedDial) findViewById(R.id.fab_versiculo);
        tvVersiculo = (TextView) findViewById(R.id.tv_versiculo);
        tvMensagem = (TextView) findViewById(R.id.tv_mensagem);
        tvAutor = (TextView) findViewById(R.id.tv_autor);

        //Configurnado a geração de versiculos
        arrayVersiculos = new ArrayList<>();
        random = new Random();

        //Ações do FABmenu
        fab.setMenuListener(new SimpleMenuListenerAdapter(){
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
        });

        /*********************** PRIMEIRO TRATAMENTO DOS DADOS ******************/
        reference = ConfiguracaoFirebase.getFirebaseReference()
                .child("VERSICULOS");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

    /******************************** METODOS ***********************************/
    @Override
    protected void onStart() {
        super.onStart();
        reference.addValueEventListener(valueEventListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        reference.removeEventListener(valueEventListener);
    }

    public void setandoTextos(final String versiculo){

        reference.orderByChild("versiculo")
                .equalTo(versiculo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data: dataSnapshot.getChildren()){
                            Versiculos versiculos = data.getValue(Versiculos.class);
                            tvMensagem.setText(versiculos.getMensagem());
                            tvVersiculo.setText(versiculos.getVersiculo());
                            tvAutor.setText(versiculos.getUsuPostagem());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
