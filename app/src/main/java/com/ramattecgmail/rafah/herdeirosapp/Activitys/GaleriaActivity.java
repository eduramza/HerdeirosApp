package com.ramattecgmail.rafah.herdeirosapp.Activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ramattecgmail.rafah.herdeirosapp.Models.Galeria;
import com.ramattecgmail.rafah.herdeirosapp.R;

public class GaleriaActivity extends AppCompatActivity {
    //ATRIBUTOS
    public EditText etAlbum;
    public TextInputLayout tiAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //etAlbum = (EditText) findViewById(R.id.et_album);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_galeria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        alertAlbum();
        return super.onOptionsItemSelected(item);
    }

    private void alertAlbum() {

        //Inflando um novo layout
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_nova_galeria, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(GaleriaActivity.this, R.style.myDialog));
        builder.setCancelable(false);
        builder.setTitle("Novo Album");

        //Colocando o arquivo XML
        builder.setView(view)
                .setPositiveButton(R.string.bt_salvar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        etAlbum = (EditText) findViewById(R.id.et_album);
                        tiAlbum = (TextInputLayout) findViewById(R.id.ti_album);

                        Galeria galeria = new Galeria();
                        galeria.setAlbum("Teste");
                        galeria.salvarGaleria();

                    }
                }).setNegativeButton(R.string.bt_cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.create();
        builder.show();

    }

}
