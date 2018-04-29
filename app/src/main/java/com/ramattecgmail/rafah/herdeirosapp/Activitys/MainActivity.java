package com.ramattecgmail.rafah.herdeirosapp.Activitys;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Configs.SharedPreferencias;
import com.ramattecgmail.rafah.herdeirosapp.Fragments.AdicionarCardapioFragment;
import com.ramattecgmail.rafah.herdeirosapp.Fragments.VersiculoFragment;
import com.ramattecgmail.rafah.herdeirosapp.Models.Galeria;
import com.ramattecgmail.rafah.herdeirosapp.Models.Usuarios;
import com.ramattecgmail.rafah.herdeirosapp.R;
import com.ramattecgmail.rafah.herdeirosapp.Utils.Atalhos;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
            GoogleApiClient.OnConnectionFailedListener{
    //Atributos
    CardView cdCalendario, cdRetiro, cdGaleria, cdMocidade;
    private TextView tvRetiro, tvCalendario, tvMocidade, tvGaleria;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    DatabaseReference referenceUser;
    ValueEventListener listenerUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instanciando os componentes
        cdCalendario = (CardView) findViewById(R.id.card_calendario);
        cdRetiro = (CardView) findViewById(R.id.card_retiro);
        cdGaleria = (CardView) findViewById(R.id.card_galeria);
        cdMocidade = (CardView) findViewById(R.id.card_mocidade);
        tvCalendario = (TextView) findViewById(R.id.tv_calendario);
        tvRetiro = (TextView) findViewById(R.id.tv_retiro);
        tvMocidade = (TextView) findViewById(R.id.tv_mocidade);
        tvGaleria = (TextView) findViewById(R.id.tv_galeria);

        Typeface font = Typeface.createFromAsset(MainActivity.this.getAssets(), "fonts/action jackson.ttf");
        tvCalendario.setTypeface(font);
        tvMocidade.setTypeface(font);
        tvRetiro.setTypeface(font);
        tvGaleria.setTypeface(font);

        //Necessário para fazer o logout
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //google api client settings
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Trabalhando com os dados do usuário aqui
        final SharedPreferencias user = new SharedPreferencias(MainActivity.this);
        referenceUser = ConfiguracaoFirebase.getFirebaseReference().child("USUARIOS").child(user.getCHAVE_ID());

        listenerUser = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Recuperando os dados de acordo com a classe modelo
                Usuarios usuarios = dataSnapshot.getValue(Usuarios.class);

                Usuarios usuariosM = new Usuarios();
                if (user.getCHAVE_NOME().toLowerCase().contains("lucas gomes")
                        || user.getCHAVE_NOME().toLowerCase().contains("taíza")
                        || user.getCHAVE_NOME().toLowerCase().contains("will")
                        || user.getCHAVE_NOME().toLowerCase().contains("daniela")
                        || user.getCHAVE_NOME().toLowerCase().contains("lucas")
                        || user.getCHAVE_NOME().toLowerCase().contains("brand")
                        || user.getCHAVE_NOME().toLowerCase().contains("daniele")
                        || user.getCHAVE_NOME().toLowerCase().contains("taíza")){
                    usuariosM.setNivel("Lider");
                } else if (user.getCHAVE_NOME().toLowerCase().contains("adriel")){
                    usuariosM.setNivel("Regente");
                } else {
                    usuariosM.setNivel("Membro");
                }

                usuariosM.setEmail(user.getCHAVE_EMAIL());
                usuariosM.setNome(user.getCHAVE_NOME());
                usuariosM.setId(user.getCHAVE_ID());
                usuariosM.salvar();

                user.salvarUsuarioPreferences(usuariosM.getId(), usuariosM.getNome(), null, usuariosM.getNivel(), usuariosM.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //EVENTOS DE CLICKS
        cdRetiro.setOnClickListener(this);
        cdCalendario.setOnClickListener(this);
        cdGaleria.setOnClickListener(this);
        cdMocidade.setOnClickListener(this);
    }

    /****************************************************************
     *  METODOS
     **************************************************************/
    @Override
    protected void onStart() {
        super.onStart();
        referenceUser.addValueEventListener(listenerUser);
    }

    @Override
    protected void onStop() {
        super.onStop();
        referenceUser.removeEventListener(listenerUser);
    }

    private void signOut(){
        //firebase
        mAuth.signOut();

        //Google SignOut
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);

                Toast.makeText(MainActivity.this, "Deslogado com sucesso!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //CRIANDO O MENU DE OPÇÕES
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opcoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_versiculo:
                VersiculoFragment fragment = new VersiculoFragment();
                fragment.show(MainActivity.this.getSupportFragmentManager(), "Adicionar Versiculo");
                return true;

            case R.id.mn_compartilhar:
                Intent sendIntent = new Intent();
                String msg = "Venha ficar mais pertinho de nós! Compartilhar cada momento com o grupo Cristo Fonte de Vida!"
                        + Atalhos.DEEP_LINK;
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

            case R.id.mn_sair:
                signOut();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.card_calendario:
                Intent calendario = new Intent(MainActivity.this, CalendarioActivity.class);
                startActivity(calendario);
                break;

            case R.id.card_retiro:
                Intent retiro = new Intent(MainActivity.this, RetiroActivity.class);
                startActivity(retiro);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

            case R.id.card_galeria:
                //Intent galeria = new Intent(MainActivity.this, GaleriaActivity.class);
                //startActivity(galeria);
                Toast.makeText(this, "Em breve!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.card_mocidade:
                //TRANSIÇÃO DE TELA EVOLUIDO
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, null);
                startActivity(new Intent(MainActivity.this, MocidadeActivity.class), compat.toBundle());
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
