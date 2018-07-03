package com.example.fatinnabila.spilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.fatinnabila.spilla.adapter.GuardianAdapter;
import com.example.fatinnabila.spilla.data.Reference;
import com.example.fatinnabila.spilla.model.GuardianModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;


public class
MainGuardian extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GuardianAdapter mAdapter;
    private final static int GUARDIAN_ADD = 1000;
    private DatabaseReference mGuardianReference;
    private ArrayList<String> mKeys;

    // Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;

    //NAVIGATION DRAWER
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
    // its a good pratice to clean and rebuild your project now and then.
    // now lets run our app.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_guardian);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();

        //We dont need this.
        mKeys = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddGuardian.class);
                startActivityForResult(intent, GUARDIAN_ADD);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_guardian);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GuardianAdapter(this, new GuardianAdapter.OnItemClick() {
            @Override
            public void onClick(int pos) {
                // Open back note activity with data
                Intent intent = new Intent(getApplicationContext(), AddGuardian.class);
                intent.putExtra(Reference.GUARDIAN_ID, mKeys.get(pos));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);

        mGuardianReference = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.DB_GUARDIAN);
    }


    protected void onStart() {
        super.onStart();

        // listening for changes
        mGuardianReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // clear table
                mKeys.clear();
                mAdapter.clear();
                // load data
                for (DataSnapshot guardianSnapshot : dataSnapshot.getChildren()) {
                    GuardianModel model = guardianSnapshot.getValue(GuardianModel.class);
                    mAdapter.addData(model);
                    mKeys.add(guardianSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // stop listening
            }
        });
    }

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                // signOut();
                Toast.makeText(MainGuardian.this, "Succesfully Logout", Toast.LENGTH_SHORT).show();

                mFirebaseAuth.signOut();
                startActivity(new Intent(MainGuardian.this, LoginActivity.class));

                break;
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //here is the main place where we need to work on.
        int id=item.getItemId();
        switch (id){

            case R.id.nav_home:
                Intent h= new Intent(MainGuardian.this,MainActivity.class);
                startActivity(h);
                break;
            case R.id.nav_pills:
                Intent i= new Intent(MainGuardian.this,MainPills.class);
                startActivity(i);
                break;
            case R.id.nav_appointment:
                Intent g= new Intent(MainGuardian.this,MainAppointment.class);
                startActivity(g);
                break;
            case R.id.nav_history:
                Intent s= new Intent(MainGuardian.this,MainHistory.class);
                startActivity(s);
                break;
            case R.id.nav_guardian:
                Intent t= new Intent(MainGuardian.this,MainGuardian.class);
                startActivity(t);
                break;
            case R.id.nav_other:
                Intent u= new Intent(MainGuardian.this,Patient1.class);
                startActivity(u);
                break;
            case R.id.nav_mail:
                Intent v= new Intent(MainGuardian.this,Profile.class);
                startActivity(v);
                break;
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static String generateHex() {

        SecureRandom random = new SecureRandom();
        String randomCode = new BigInteger(30, random).toString(32).toUpperCase();
        return randomCode;
    }
}
