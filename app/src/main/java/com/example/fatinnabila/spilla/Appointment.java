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


public class Appointment extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private AppointmentAdapter mAdapter;
    private final static int APPOINTMENT_ADD = 1000;

    private DatabaseReference mAppointmentReference;

    private ArrayList<String> mKeys;

    // Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
    // its a good pratice to clean and rebuild your project now and then.
    // now lets run our app.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();


        setContentView(R.layout.appointment_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //We dont need this.
        mKeys = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddAppointment.class);
                startActivityForResult(intent, APPOINTMENT_ADD);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



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
                for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                    AppointmentModel model = noteSnapshot.getValue(AppointmentModel.class);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //here is the main place where we need to work on.
        int id=item.getItemId();
        switch (id){

            case R.id.nav_home:
                Intent h= new Intent(Appointment.this,MainActivity.class);
                startActivity(h);
                break;
            case R.id.nav_pills:
                Intent i= new Intent(Appointment.this,Pills.class);
                startActivity(i);
                break;
            case R.id.nav_appointment:
                Intent g= new Intent(Appointment.this,Appointment.class);
                startActivity(g);
                break;
            case R.id.nav_history:
                Intent s= new Intent(Appointment.this,HealthHistory.class);
                startActivity(s);
                break;
            case R.id.nav_guardian:
                Intent t= new Intent(Appointment.this,Guardian.class);
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
