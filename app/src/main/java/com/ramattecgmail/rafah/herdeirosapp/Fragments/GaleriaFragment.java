package com.ramattecgmail.rafah.herdeirosapp.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ramattecgmail.rafah.herdeirosapp.Adapters.GaleriaAdapter;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Models.Galeria;
import com.ramattecgmail.rafah.herdeirosapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class GaleriaFragment extends Fragment {
    //ATRIBUTOS
    private GridView gridView;
    private ArrayList<Galeria> arrayList;
    private ArrayAdapter adapter;
    private DatabaseReference reference;
    private ValueEventListener valueEventListener;

    public GaleriaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_galeria, container, false);

        //Instanciando os componentes
        gridView = view.findViewById(R.id.grid_galerias);
        arrayList = new ArrayList<>();
        adapter = new GaleriaAdapter(getActivity(), arrayList);
        gridView.setAdapter(adapter);

        //******************* CONFIGURAÇÕES DO BANCO *********************/
        reference = ConfiguracaoFirebase.getFirebaseReference().child("GALERIA");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();

                //Listando os albuns
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    Galeria galeria = data.getValue(Galeria.class);
                    arrayList.add(galeria);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //EVENTO DO GRIDVIEW
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(getActivity(), "clicado na posição " + position, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        reference.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        reference.removeEventListener(valueEventListener);
    }
}
