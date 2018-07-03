package com.example.fatinnabila.spilla;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{


    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private GoogleApiClient mGoogleApiClient;
    Button sign_out;
    Button signin;
    //TextView tvname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //  private SignInButton mBtnSignIn;

        mAuth = FirebaseAuth.getInstance();
        // Check user, if not exists then go login
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //redirect to main page
        if(currentUser != null) {
            userAlreadyLoggedIn();
        }


        signin = (Button) findViewById(R.id.btn_signin);
//        sign_out= (Button) findViewById(R.id.sign_out);
//        sign_out.setVisibility(View.VISIBLE);



//        sign_out.setOnClickListener(new View.OnClickListener() {
//            @Override
//
//            public void onClick(View v) {
//
//                int i = v.getId();
//                if(i==R.id.sign_out){
//
//
//                }
//                signOut();
//            }
//        });


        findViewById(R.id.btn_signin).setOnClickListener(LoginActivity.this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(LoginActivity.this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i==R.id.btn_signin){

            signIn();

        }
    }

    public void signIn(){

       Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
       Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN){

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){

                GoogleSignInAccount account = result.getSignInAccount();

                firebaseAuthWithGoogle(account);
            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        // Toast.makeText(LoginActivity.this,"",Toast.LENGTH_LONG).show();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        String name = getdata();

                        if (task.isSuccessful()){
                            // sign_out.setVisibility(View.VISIBLE);

                            FirebaseUser user = mAuth.getCurrentUser();

                            // signin.setVisibility(View.VISIBLE);
                            // tvname.setText("Welcome "+name);

                            if(user != null) {
                                Toast.makeText(LoginActivity.this, "Hi! Welcome " + user.getDisplayName(), Toast.LENGTH_LONG).show();
                            }


                            userAlreadyLoggedIn();

                        }else {
                            Toast.makeText(LoginActivity.this,"Something went wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signOut() {
        // Firebase sign out
        mAuth.signOut();


        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //  Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        //  startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Logout", Toast.LENGTH_SHORT).show();

                    }
                });
        // sign_out.setVisibility(View.VISIBLE);

        //  tvname.setText(null);
        //  signin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public String getdata(){
        String name = null;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getEmail();
            //  String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            String uid = user.getUid();
        }

        return name;

    }

    private void userAlreadyLoggedIn() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
