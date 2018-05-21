package com.ramattecgmail.rafah.herdeirosapp.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ramattecgmail.rafah.herdeirosapp.Configs.ConfiguracaoFirebase;
import com.ramattecgmail.rafah.herdeirosapp.Configs.SharedPreferencias;
import com.ramattecgmail.rafah.herdeirosapp.R;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
    //ATRIBUTOS
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    private CallbackManager callbackManager;
    private Button btFaceLogin;
    private Button btGoogleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide(); //ESCONDER A ACTION BAR PADRÃO POR ESTÉTICA
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        /*************** TESTE DE LOGIN COM BOTÃO CUSTOMIZADO ***********/
        FacebookSdk.sdkInitialize(getApplicationContext());

        btFaceLogin = findViewById(R.id.bt_login_face);
        btGoogleLogin = findViewById(R.id.bt_login_google);

        //EVENTOS
        btGoogleLogin.setOnClickListener(this);
        btFaceLogin.setOnClickListener(this);

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "Sucesso linha 82");
                AccessToken token  = AccessToken.getCurrentAccessToken();
                handleFacebookAccessToken(token);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        /*************** TESTE DE LOGIN COM BOTÃO CUSTOMIZADO ***********/

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    /********************************************************
     * *********************** METODOS *********************
     ******************************************************/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Testing result
        if (requestCode == RC_SIGN_IN /*&& resultCode == RESULT_OK*/){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                //Google Sigin as succesfull, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }

        //TRABALHANDO RESULTADO DO FACEBOOK LOGIN
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct){
        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //se o a autenticação com o fire base for bem sucedida
                        if (task.isSuccessful()){

                            //salvando os dados no sharedPreferences
                            SharedPreferencias user = new SharedPreferencias(LoginActivity.this);
                            user.salvarUsuarioPreferences(acct.getId(), acct.getDisplayName(),
                                    null, "Membro", acct.getEmail(), acct.getGivenName());

                            Intent versiculo = new Intent(LoginActivity.this, VersiculoActivity.class);
                            startActivity(versiculo);

                        } else {
                            Log.w("Login Result", "signInWithCredential:failure ", task.getException());
                        }

                    }
                });
    }

    //TROCANDO O TOKEN DO FACEBOOK POR UMA CREDENCIAL DO FIREBASE USER
    public void handleFacebookAccessToken(AccessToken tokenF){
        Log.d(TAG, "manipulando o token de acesso do Facebook: " +tokenF);

        AuthCredential credential = FacebookAuthProvider.getCredential(tokenF.getToken());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //Login bem sucedido, trocando as credenciais com o firebase
                            Log.d(TAG, "Login com a credencial: sucesso!");
                            FirebaseUser Fuser = mAuth.getCurrentUser();

                            //Salvando os dados no sharedPreferences
                            SharedPreferencias userF = new SharedPreferencias(LoginActivity.this);
                            userF.salvarUsuarioPreferences(Fuser.getUid(), Fuser.getDisplayName(),
                                    null, "Membro", Fuser.getEmail(), Fuser.getDisplayName());

                            Intent versiculo = new Intent(LoginActivity.this, VersiculoActivity.class);
                            startActivity(versiculo);

                        }
                    }
                });
    }

    //Metodo que abre a tela de seleção de contas do google para fazer o login
    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_google:
                googleSignIn();
                break;

            case R.id.bt_login_face:
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile", "user_link"));
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (mAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginActivity.this, VersiculoActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
