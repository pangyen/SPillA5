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
import android.widget.Button;

import com.example.fatinnabila.spilla.adapter.PillsAdapter;
import com.example.fatinnabila.spilla.data.Reference;
import com.example.fatinnabila.spilla.model.PillsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Pills extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PillsAdapter mAdapter;


    private DatabaseReference mNotesReference;

    private ArrayList<String> mKeys;

    // Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;


    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
    Button box1;

    private final static int PILLS_ADD = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();

        setContentView(R.layout.pills_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mKeys = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddPills.class);
                startActivityForResult(intent, PILLS_ADD);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_pills);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PillsAdapter(this, new PillsAdapter.OnItemClick() {
            @Override
            public void onClick(int pos) {
                // Open back note activity with data
                Intent intent = new Intent(getApplicationContext(), AddPills.class);
                intent.putExtra(Reference.PILLS_ID, mKeys.get(pos));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);

        mNotesReference = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.DB_PILLS);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // listening for changes
        mNotesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // clear table
                mKeys.clear();
                mAdapter.clear();
                // load data
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    PillsModel model = noteSnapshot.getValue(PillsModel.class);
                    mAdapter.addData(model);
                    mKeys.add(noteSnapshot.getKey());
                }
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
                mFirebaseAuth.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return true;
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //here is the main place where we need to work on.
        int id=item.getItemId();
        switch (id){

            case R.id.nav_home:
                Intent h= new Intent(Pills.this,MainActivity.class);
                startActivity(h);
                break;
            case R.id.nav_pills:
                Intent i= new Intent(Pills.this,Pills.class);
                startActivity(i);
                break;
            case R.id.nav_appointment:
                Intent g= new Intent(Pills.this,Appointment.class);
                startActivity(g);
                break;
            case R.id.nav_history:
                Intent s= new Intent(Pills.this,HealthHistory.class);
                startActivity(s);
                break;
            case R.id.nav_guardian:
                Intent t= new Intent(Pills.this,Guardian.class);
                startActivity(t);
                break;


            // after this lets start copying the above.
            // FOLLOW MEEEEE>>>
            //copy this now.
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
