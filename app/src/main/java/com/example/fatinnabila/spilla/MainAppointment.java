package com.example.fatinnabila.spilla;

/**
 * Created by fatin nabila on 28/2/2018.
 */

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

import com.example.fatinnabila.spilla.adapter.AppointmentAdapter;
import com.example.fatinnabila.spilla.data.Reference;
import com.example.fatinnabila.spilla.model.AppointmentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class MainAppointment extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AppointmentAdapter mAdapter;
    private final static int APPOINTMENT_ADD = 1000;
    //Firebase
    private DatabaseReference mAppointmentReference;
    private ArrayList<String> mKeys;

    // Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drawer_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mKeys = new ArrayList<>();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddAppointment.class);
                startActivityForResult(intent, APPOINTMENT_ADD);
            }
        });
        //drawer_patient layout
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_appointment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AppointmentAdapter(this, new AppointmentAdapter.OnItemClick() {
            @Override
            public void onClick(int pos) {
                // Open back note activity with data
                Intent intent = new Intent(getApplicationContext(), AddAppointment.class);
                intent.putExtra(Reference.APPOINTMENT_ID, mKeys.get(pos));
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);
        mAppointmentReference = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.DB_APPOINTMENT);
    }

    protected void onStart() {
        super.onStart();

        // listening for changes
        mAppointmentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // clear table
                mKeys.clear();
                mAdapter.clear();
                // load data
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    AppointmentModel model = appointmentSnapshot.getValue(AppointmentModel.class);
                    mAdapter.addData(model);
                    mKeys.add(appointmentSnapshot.getKey());
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                // signOut();
                Toast.makeText(MainAppointment.this, "Succesfully Logout", Toast.LENGTH_SHORT).show();

                mFirebaseAuth.signOut();
                startActivity(new Intent(MainAppointment.this, LoginActivity.class));

                break;
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    //handle navigation
    public boolean onNavigationItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){

            case R.id.nav_home:
                Intent h= new Intent(MainAppointment.this,MainActivity.class);
                startActivity(h);
                break;
            case R.id.nav_pills:
                Intent i= new Intent(MainAppointment.this,MainPills.class);
                startActivity(i);
                break;
            case R.id.nav_appointment:
                Intent g= new Intent(MainAppointment.this,MainAppointment.class);
                startActivity(g);
                break;
            case R.id.nav_history:
                Intent s= new Intent(MainAppointment.this,MainHistory.class);
                startActivity(s);
                break;
            case R.id.nav_guardian:
                Intent t= new Intent(MainAppointment.this,MainGuardian.class);
                startActivity(t);
                break;
            case R.id.nav_other:
                Intent u= new Intent(MainAppointment.this,Patient1.class);
                startActivity(u);
                break;
            case R.id.nav_mail:
                Intent v= new Intent(MainAppointment.this,Profile.class);
                startActivity(v);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
