package com.ramattecgmail.rafah.herdeirosapp.Activitys;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Fragments.Alerts.CardapioFragment;
import com.ramattecgmail.rafah.herdeirosapp.Fragments.Alerts.CronogramaFragment;
import com.ramattecgmail.rafah.herdeirosapp.Fragments.Alerts.PlacarFragment;
import com.ramattecgmail.rafah.herdeirosapp.Models.Placar;
import com.ramattecgmail.rafah.herdeirosapp.R;
import com.ramattecgmail.rafah.herdeirosapp.Utils.Atalhos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RetiroActivity extends AppCompatActivity implements View.OnClickListener {
    //ATRIBUTOS
    private String dia;
    private CardView cardPlacar;
    private Button btCardapio1, btCardapio2, btCardapio3, btCronograma1, btCronograma2, btCronograma3;
    private TextView tvCronograma, tvCardapio, tvPlacarA, tvPlacarB, tvAtualizado;
    private DatabaseReference reference;
    private ValueEventListener valueEventListener;
    private Bundle bundle;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retiro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //DEFININDO A TRANSIÇÃO DE ENTRADA DA ACTIVITY
        if(Build.VERSION.SDK_INT >= 22){
            Slide s = new Slide();
            s.setDuration(1000);
            getWindow().setEnterTransition(s);
        }

        //INSTANCIANDO OS COMPONENTES
        cardPlacar = (CardView) findViewById(R.id.card_placar);
        btCardapio1 = (Button) findViewById(R.id.bt_dia1_cardapio);
        btCardapio2 = (Button) findViewById(R.id.bt_dia2_cardapio);
        btCardapio3 = (Button) findViewById(R.id.bt_dia3_cardapio);
        tvCardapio = (TextView) findViewById(R.id.tv_cardapio);
        tvCronograma = (TextView) findViewById(R.id.tv_cronograma);
        tvPlacarA = (TextView) findViewById(R.id.tv_placarA);
        tvPlacarB = (TextView) findViewById(R.id.tv_placarB);
        tvAtualizado = (TextView) findViewById(R.id.tv_atualizacao_placar);
        btCronograma1 = (Button) findViewById(R.id.bt_data1_cronograma);
        btCronograma2 = (Button) findViewById(R.id.bt_data2_cronograma);
        btCronograma3 = (Button) findViewById(R.id.bt_data3_cronograma);

        //Adicionando fontes externas
        Typeface fonteDescricao = Typeface.createFromAsset(getAssets(), "fonts/Royalacid.ttf");
        tvCardapio.setTypeface(fonteDescricao);
        tvCronograma.setTypeface(fonteDescricao);


        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    Placar placar = data.getValue(Placar.class);
                    tvPlacarA.setText(placar.getPlacarEmanuel());
                    tvPlacarB.setText(placar.getPlacarGideoes());
                    tvAtualizado.setText("Atualizado em: "+ placar.getAtualizacao());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RetiroActivity.this, "Erro na leitura do banco, contate o admin!", Toast.LENGTH_SHORT).show();
            }
        };

        //Eventos
        btCardapio1.setOnClickListener(this);
        btCardapio2.setOnClickListener(this);
        btCardapio3.setOnClickListener(this);
        cardPlacar.setOnClickListener(this);
        btCronograma1.setOnClickListener(this);
        btCronograma2.setOnClickListener(this);
        btCronograma3.setOnClickListener(this);
        mostrarPLacar();
    }

    //Atualiza o placar no momento da abertura da tela
    private void mostrarPLacar() {
        //Pegando o ano atual
        Date ano = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("yyyy");
        String anoRetiro = format.format(ano);

        reference = ConfiguracaoFirebase.getFirebaseReference()
                .child("RETIRO")
                .child(anoRetiro)
                .child("PLACAR");

        reference.limitToLast(1);
        reference.addValueEventListener(valueEventListener);

    }

    @Override
    public void onClick(View view) {
        CardapioFragment cardapioFragment = new CardapioFragment();
        CronogramaFragment cronogramaFragment = new CronogramaFragment();
        bundle = new Bundle();

        switch (view.getId()){
            case R.id.bt_dia1_cardapio:
                bundle = new Bundle();
                bundle.putString("data", "24");

                //Inicinado o cardapio
                cardapioFragment.setArguments(bundle);
                cardapioFragment.show(RetiroActivity.this.getSupportFragmentManager(), "AlertDialog Cardapio");
                break;

            case R.id.bt_dia2_cardapio:
                bundle = new Bundle();
                bundle.putString("data", "25");

                //Inicinado o cardapio
                cardapioFragment.setArguments(bundle);
                cardapioFragment.show(RetiroActivity.this.getSupportFragmentManager(), "AlertDialog Cardapio");
                break;

            case R.id.bt_dia3_cardapio:
                bundle = new Bundle();
                bundle.putString("data", "26");

                //Inicinado o cardapio
                cardapioFragment.setArguments(bundle);
                cardapioFragment.show(RetiroActivity.this.getSupportFragmentManager(), "AlertDialog Cardapio");
                break;

            case R.id.card_placar:
                if (Atalhos.verificarLider(getApplicationContext()) == true){

                    PlacarFragment placar = new PlacarFragment();
                    placar.show(RetiroActivity.this.getSupportFragmentManager(), "Novo Placar");

                }
                break;

            case R.id.bt_data1_cronograma:
                bundle.putString("DIA", btCronograma1.getText().toString());

                //INICIANDO O FRAGMENT
                cronogramaFragment.setArguments(bundle);
                cronogramaFragment.show(RetiroActivity.this.getSupportFragmentManager(), "Lista de atividades");
                break;

            case R.id.bt_data2_cronograma:
                bundle.putString("DIA", btCronograma2.getText().toString());

                //INICIANDO O FRAGMENT
                cronogramaFragment.setArguments(bundle);
                cronogramaFragment.show(RetiroActivity.this.getSupportFragmentManager(), "Lista de atividades");
                break;

            case R.id.bt_data3_cronograma:
                bundle.putString("DIA", btCronograma3.getText().toString());

                //INICIANDO O FRAGMENT
                cronogramaFragment.setArguments(bundle);
                cronogramaFragment.show(RetiroActivity.this.getSupportFragmentManager(), "Lista de atividades");
                break;
        }
    }
}
