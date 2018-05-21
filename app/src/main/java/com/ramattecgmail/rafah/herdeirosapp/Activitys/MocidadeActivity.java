package com.ramattecgmail.rafah.herdeirosapp.Activitys;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ramattecgmail.rafah.herdeirosapp.Adapters.HinosAdapter;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Configs.SharedPreferencias;
import com.ramattecgmail.rafah.herdeirosapp.Fragments.Alerts.AdicionarHinoFragment;
import com.ramattecgmail.rafah.herdeirosapp.Models.Hinos;
import com.ramattecgmail.rafah.herdeirosapp.R;
import com.ramattecgmail.rafah.herdeirosapp.Utils.Atalhos;

import java.util.ArrayList;

public class MocidadeActivity extends AppCompatActivity {
    //ATRIBUTOS
    private ListView listView;
    private ArrayList<Hinos> arrayList;
    private ArrayAdapter adapter;
    private DatabaseReference reference;
    private ValueEventListener valueEventListener;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mocidade);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //DEFININDO A TRANSIÇÃO DE ENTRADA DA ACTIVITY
        if (Build.VERSION.SDK_INT >= 22){
            Slide s = new Slide();
            s.setDuration(1000);
            getWindow().setEnterTransition(s);
        }

        //Montando a lista de exibição dos hinos
        listView = (ListView) findViewById(R.id.lv_hinos);

        arrayList = new ArrayList<>();
        adapter = new HinosAdapter(MocidadeActivity.this, arrayList);
        listView.setAdapter(adapter);

        //*************** FIREBASE INICIO ************************/
        reference = ConfiguracaoFirebase.getFirebaseReference().child("MOCIDADE")
                .child("HINOS");
        reference.orderByKey();

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();

                //Listando os hinos
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    Hinos hinos = data.getValue(Hinos.class);
                    arrayList.add(hinos);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //*************** FIREBASE INICIO ************************/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                try{
                    Hinos hinos = arrayList.get(position);
                    //salvando no shared para ser utilizado depois
                    SharedPreferencias preferences = new SharedPreferencias(MocidadeActivity.this);
                    preferences.salvarHinoPreferences(hinos.getNumero(), hinos.getTitulo(), hinos.getCantor(), hinos.getUrl());

                    Intent letraA = new Intent(MocidadeActivity.this, LetraHinosActivity.class);
                    startActivity(letraA);

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    /******************************* METODOS *********************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mocidade, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_add_hino:

                if (Atalhos.verificarRegente(getApplicationContext()) == true) {
                    AdicionarHinoFragment hinoFragment = new AdicionarHinoFragment();
                    hinoFragment.show(MocidadeActivity.this.getSupportFragmentManager(), "Alert Adicionar Hino");
                } else {
                    Atalhos.acessoNegado(MocidadeActivity.this);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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
}
