package com.ramattecgmail.rafah.herdeirosapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramattecgmail.rafah.herdeirosapp.Models.Galeria;
import com.ramattecgmail.rafah.herdeirosapp.Models.Hinos;
import com.ramattecgmail.rafah.herdeirosapp.R;

import java.util.ArrayList;

/**
 * Created by eduardo on 07/11/2017.
 */

public class GaleriaAdapter extends ArrayAdapter<Galeria> {

    private Context mContext;
    private ArrayList<Galeria> arrayList;

    public GaleriaAdapter(Context mContext, ArrayList<Galeria> objects) {
        super(mContext, 0, objects);
        this.mContext = mContext;
        this.arrayList = objects;
    }

    //CRIANDO UMA NOVA IMAGEM PARA CADA ITEM REFERENCIADO NA ARRAY
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        View v = null;

        //Criando a lista de albuns
        if (arrayList != null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);

            //Montando a view a partir do XML
            v = inflater.inflate(R.layout.lista_galeria, parent, false);

            //Recuperando os elementos para exibição
            ImageView imageView = view.findViewById(R.id.capa_album);
            TextView tvAlbum = view.findViewById(R.id.tv_album);

            Galeria galeria = arrayList.get(position);
            tvAlbum.setText(galeria.getAlbum());

        }

        return v;
    }
}
