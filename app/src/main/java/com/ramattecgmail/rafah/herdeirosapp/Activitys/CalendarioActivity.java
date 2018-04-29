package com.ramattecgmail.rafah.herdeirosapp.Activitys;

import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramattecgmail.rafah.herdeirosapp.Adapters.EventoAdapter;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Fragments.EventoDialogFragment;
import com.ramattecgmail.rafah.herdeirosapp.Models.Eventos;
import com.ramattecgmail.rafah.herdeirosapp.R;
import com.ramattecgmail.rafah.herdeirosapp.Utils.Atalhos;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class CalendarioActivity extends AppCompatActivity  {
    //ATRIBUTOS
    private CalendarView calendario;
    private ListView listView;
    private String delID;

    private ArrayList<Eventos> arrayList;
    private ArrayAdapter adapter;
    private DatabaseReference reference;
    private ValueEventListener valueEventListener;

    //Ações para remover dados de eventos que já passaram
    private DatabaseReference removeReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendario = (CalendarView) findViewById(R.id.cv_calendario);

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int ano, int mes, int dia) {
                if (Atalhos.verificarLider(getApplicationContext()) == false){

                    //Atalhos.acessoNegado(CalendarioActivity.this);
                } else {
                    //Enviando dados para o fragment
                    int acerto = mes + 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("DATA SELECIONADA", dia+"/"+acerto+"/"+ano+" 00:00:00");

                    //Configurando o evento de transição entre as activities
                    EventoDialogFragment evento = new EventoDialogFragment();
                    evento.setArguments(bundle);
                    evento.show(CalendarioActivity.this.getSupportFragmentManager(), "Teste de alert dialog fragment");
                }

            }
        });

        /******************************* CONFIGURANDO A LISTA DE EVENTOS **************************/
        arrayList = new ArrayList<>();
        listView = (ListView) findViewById(R.id.lv_eventos);

        adapter = new EventoAdapter(CalendarioActivity.this, arrayList);
        listView.setAdapter(adapter);

        reference = ConfiguracaoFirebase.getFirebaseReference()
                .child("EVENTOS");
        //reference.orderByChild("dataFim").limitToLast(10); será usado para ordenar em ordem decrescente

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //LIMPANDO A LISTA
                arrayList.clear();

                //Listando cada um dos eventos
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    Eventos eventos = data.getValue(Eventos.class);
                    arrayList.add(eventos);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        /****************************FIM  CONFIGURANDO A LISTA DE EVENTOS *************************/
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (Atalhos.verificarLider(CalendarioActivity.this) == true){
                    alertDialog(position);
                    return true;
                }
                return false;
            }
        });

        //Compartilhando o evento
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //pegando a descrição do evento e compartilhando
                Eventos eventos = arrayList.get(position);
                String descricaoEvento = eventos.getDescricao() + "\n inicio em:\n"+ eventos.getDataInicio();

                Atalhos.compartilhaDialog(descricaoEvento, CalendarioActivity.this);
            }
        });

    }

    /************************************* METODOS ************************************/
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

    public void alertDialog(final int position){
        //CONFIGURAÇÃO DO ALERT DIALOG DENTRO DO EVENTO DE CLICK
        AlertDialog.Builder alert = new AlertDialog.Builder(CalendarioActivity.this);

        alert.setTitle("Exclusão de Evento");
        alert.setMessage("Deseja excluir este evento?");
        alert.setCancelable(false);

        Eventos eventosModel = arrayList.get(position);
        delID = eventosModel.getId();

        //AÇÃO DO ALERT DIALOG
        alert.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //EXCLUINDO A EVENTO
                removeReference = ConfiguracaoFirebase.getFirebaseReference();
                removeReference.child("EVENTOS").child(delID).removeValue();
                Toast.makeText(CalendarioActivity.this, "Evento excluido com sucesso!",Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //materializando o alert
        alert.create();
        alert.show();
    }

}
