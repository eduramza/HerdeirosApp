package com.ramattecgmail.rafah.herdeirosapp.Fragments.Alerts;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ramattecgmail.rafah.herdeirosapp.Adapters.CronogramaAdapter;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Models.Cronograma;
import com.ramattecgmail.rafah.herdeirosapp.R;
import com.ramattecgmail.rafah.herdeirosapp.Utils.Atalhos;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CronogramaFragment extends DialogFragment {
    //ATRIBUTOS
    private TextView tvTitulo;
    private ListView lista;
    private ImageView imAdd;
    private ArrayList<Cronograma> arrayList;
    private ArrayAdapter adapter;
    private Query query;
    private ValueEventListener valueEventListener;

    public CronogramaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cronograma, container, false);
        this.getDialog().setTitle("Cronograma do Retiro");

        //INSTANCIANDO OS COMPONENTES
        tvTitulo = view.findViewById(R.id.tv_apres_cronograma);
        lista = view.findViewById(R.id.lv_cronograma);
        imAdd = view.findViewById(R.id.im_add_cronograma);

        //Montando a lista
        arrayList = new ArrayList<>();
        adapter = new CronogramaAdapter(getActivity(), arrayList);
        lista.setAdapter(adapter);

        //Recebendo o dia da tela anterior
        final String diaR = getArguments().getString("DIA");
        tvTitulo.setText("Programação de "+ diaR);

        //Buscando os dados
        query = ConfiguracaoFirebase.getFirebaseReference()
                .child("RETIRO")
                .child(Atalhos.getAno())
                .child("CRONOGRAMA")
                .child(diaR)
                .orderByChild("horaInicio");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();

                //Listando as atividades
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    Cronograma cronograma = data.getValue(Cronograma.class);
                    arrayList.add(cronograma);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        imAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Atalhos.verificarLider(getActivity().getApplicationContext()) == false){
                    Toast.makeText(getActivity(), "Você não pode adicionar eventos no cronograma!", Toast.LENGTH_SHORT).show();
                } else {
                    AddCronogramaFragment add = new AddCronogramaFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("dia", diaR);
                    add.setArguments(bundle);

                    add.show(getActivity().getSupportFragmentManager(), "Adicionar Cronograma");
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        query.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        query.removeEventListener(valueEventListener);
    }

}
