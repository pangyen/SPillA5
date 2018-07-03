package com.example.fatinnabila.spilla;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fatinnabila.spilla.data.Reference;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnItemSelectedListener {
    TextView tvMail;
    TextView tvUser;

    //Firebase realtime
    private DatabaseReference rootRef,demoRef, pillsRef;

    // Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private ArrayList<String> mKeys;

    // private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    // private GoogleApiClient mGoogleApiClient;

    //drawer_patient layout
    DrawerLayout drawer;
    NavigationView navigationView;
    View mHeaderView;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drawer_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //statusPills = (TextView) findViewById(R.id.tv_pstatus);

        //Firebase authentication
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();

        //drawer_patient layout
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // NavigationView Header
        //   mHeaderView = (NavigationView) navigationView.getHeaderView(0);
        // View
        //  textViewUsername = (TextView) mHeaderView.findViewById(R.id.textViewUsernameNav);
        // tvMail= (TextView) navigationView.findViewById(R.id.textMail);

        // Set username & email
        //  textViewUsername.setText(SharedPrefManager.getInstance(this).getUsername());
        ///  tvMail.setText(mCurrentUser.getEmail().toString());

//       tvMail=(TextView)findViewById(R.id.nav_mail) ;
        tvMail=(TextView)findViewById(R.id.tv_email);
        tvMail.setText(mCurrentUser.getEmail());

        tvUser=(TextView)findViewById(R.id.tv_userName);
        tvUser.setText(mCurrentUser.getDisplayName());


//        //view statuspills
//        rootRef = FirebaseDatabase.getInstance().getReference();
//        demoRef = rootRef;
//        demoRef.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String value = dataSnapshot.getValue(String.class);
//                statusPills.setText(value);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });

        demoRef = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.DB_MESSAGE);


    }


    @Override
    protected void onStart() {
        super.onStart();

        // listening for changes
        demoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // stop listening
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                // signOut();
                Toast.makeText(Profile.this, "Succesfully Logout", Toast.LENGTH_SHORT).show();

                mFirebaseAuth.signOut();
                startActivity(new Intent(Profile.this, LoginActivity.class));

                break;
        }

        return true;
    }

    public boolean onNavigationItemSelected(MenuItem item) {

        int id=item.getItemId();


        switch (id){

            case R.id.nav_home:
                Intent h= new Intent(Profile.this,MainActivity.class);
                startActivity(h);
                break;
            case R.id.nav_pills:
                Intent i= new Intent(Profile.this,MainPills.class);
                startActivity(i);
                break;
            case R.id.nav_appointment:
                Intent g= new Intent(Profile.this,MainAppointment.class);
                startActivity(g);
                break;
            case R.id.nav_history:
                Intent s= new Intent(Profile.this,MainHistory.class);
                startActivity(s);
                break;
            case R.id.nav_guardian:
                Intent t= new Intent(Profile.this,MainGuardian.class);
                startActivity(t);
                break;
            case R.id.nav_other:
                Intent u= new Intent(Profile.this,Patient1.class);
                startActivity(u);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    private void updateUI(FirebaseUser user) {

    }

    public void signOutt() {
//
        // Firebase sign out
        mFirebaseAuth.signOut();


        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //  Intent intent = new Intent(MainActivity.this,MainActivity.class);
                        //  startActivity(intent);
                        Toast.makeText(Profile.this, "Logout", Toast.LENGTH_SHORT).show();

                    }
                });
        // sign_out.setVisibility(View.VISIBLE);

        //  tvname.setText(null);
        //  signin.setVisibility(View.VISIBLE);
    }
}
