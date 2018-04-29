package com.ramattecgmail.rafah.herdeirosapp.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ramattecgmail.rafah.herdeirosapp.Models.Eventos;
import com.ramattecgmail.rafah.herdeirosapp.R;

import java.util.ArrayList;

/**
 * Created by rafah on 30/09/2017.
 */

public class EventoAdapter extends ArrayAdapter<Eventos> {

    private ArrayList<Eventos> eventosArrayList;
    private Context context;

    public EventoAdapter(@NonNull Context c, @NonNull ArrayList<Eventos> objects) {
        super(c, 0, objects);
        this.eventosArrayList = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        //Validando e criando a lista
        if (eventosArrayList != null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Montando a view a partir do xml
            view = inflater.inflate(R.layout.lista_eventos, parent, false);

            //Recuperando os elementos para exibição
            TextView titulo = view.findViewById(R.id.tv_titulo_lista);
            TextView data = view.findViewById(R.id.tv_data_lista);

            Eventos eventos = eventosArrayList.get(position);
            titulo.setText(eventos.getTitulo());
            data.setText(eventos.getDataInicio());

        }

        return view;
    }

}
