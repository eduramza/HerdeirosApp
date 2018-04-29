package com.ramattecgmail.rafah.herdeirosapp.Fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.ramattecgmail.rafah.herdeirosapp.Activitys.MainActivity;
import com.ramattecgmail.rafah.herdeirosapp.Adapters.CardapioAdapter;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Models.Cardapio;
import com.ramattecgmail.rafah.herdeirosapp.R;
import com.ramattecgmail.rafah.herdeirosapp.Utils.Atalhos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardapioFragment extends DialogFragment implements View.OnClickListener {
    //ATRIBUTOS
    private ImageView imAdd;
    private TextView tvMenu;
    private ListView listView;
    private ArrayList<Cardapio> arrayList;
    private ArrayAdapter adapter;
    private Query queryFirebase;
    private ValueEventListener valueEventListener;


    public CardapioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardapio, container, false);
        this.getDialog().setTitle("Cardapio do Retiro");

        //INSTANCIANDO OS COMPONENTES
        tvMenu = view.findViewById(R.id.tv_titulo_cardapio);
        listView = view.findViewById(R.id.lv_cardapio);
        imAdd = view.findViewById(R.id.im_add_cardapio);

        //Montando a lista
        arrayList = new ArrayList<>();
        adapter = new CardapioAdapter(getActivity(), arrayList);
        listView.setAdapter(adapter);

        //Recebendo a data enviada da activity anterior
        String dia = getArguments().getString("data");

        //Adicionando fontes externas
        Typeface fontMenu = Typeface.createFromAsset(getActivity().getAssets(), "fonts/action jackson.ttf");
        tvMenu.setTypeface(fontMenu);


        //Pegando o ano atual
        Date ano = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("yyyy");
        String anoRetiro = format.format(ano);

        //Contato com o realtimeDatabase
        queryFirebase = ConfiguracaoFirebase.getFirebaseReference()
                .child("RETIRO")
                .child(anoRetiro)
                .child("CARDAPIO")
                .child(dia)
                .orderByChild("ordem");

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList.clear();

                //Listando os eventos
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    Cardapio cardapio = data.getValue(Cardapio.class);
                    arrayList.add(cardapio);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        imAdd.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.im_add_cardapio:
                if (Atalhos.verificarLider(getActivity().getApplicationContext()) == false){
                    Toast.makeText(getActivity(), "Você não pode adicionar o cárdapio do dia!", Toast.LENGTH_SHORT).show();
                } else {
                    AdicionarCardapioFragment fragmentCardapio = new AdicionarCardapioFragment();
                    fragmentCardapio.show(getActivity().getSupportFragmentManager(), "Adicionar Cardapio");
                }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        queryFirebase.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        queryFirebase.removeEventListener(valueEventListener);
    }

}
