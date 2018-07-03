package com.example.fatinnabila.spilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fatinnabila.spilla.adapter.OtherAdapter;
import com.example.fatinnabila.spilla.model.OtherModel;
import com.example.fatinnabila.spilla.model.PatientModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Patient1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private final static String

            TAG = Patient1.class.getSimpleName();

//    private OtherAdapter mAdapter;
    private OtherAdapter mAdapter;

    // Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseDatabase mDatabase, childRef;
    private FirebaseFirestore mFirestore;

    private DatabaseReference mOtherReference;

    private TextView mTVTitle;
    private TextView mTVDescription;


    //drawer_patient layout
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
//    RecyclerView rv;
//    ListView listViewother;

    //RecyclerView<OtherModel> other;
  //  RecyclerView recyclerView1;

    private ArrayList<String> mKeys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mKeys = new ArrayList<>();
        setContentView(R.layout.drawer_patient);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



       // childRef=mDatabase.child("pills");
        // Binding
       // mTVTitle = (TextView) findViewById(R.id.tv_titlev);
//        mTVDescription = (TextView) findViewById(R.id.tv_descriptionv);
//
//        listViewother =findViewById(R.id.listViewOther);

//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        childRef=mDatabase.child("pills");


//        //recycler view
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_other);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        recyclerView.setAdapter(mAdapter);
//        mOtherReference = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.DB_OTHER);



        // Load guardian
        loadOther();


        //mAdapter = new PillsAdapter(this, null);


        //drawer_patient layout
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_other);
        recyclerView.setLayoutManager(new LinearLayoutManager(Patient1.this));

        mAdapter = new OtherAdapter(this, new OtherAdapter.OnItemClick() {
            @Override
            public void onClick(int pos) {
                // Open back note activity with data
                Intent intent = new Intent(getApplicationContext(), ChooseDisplay.class);
                //          intent.putExtra(Reference.PILLS_ID, mKeys.get(pos));
                startActivity(intent);
            }
        });


        recyclerView.setAdapter(mAdapter);

       // mOtherReference = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.DB_PILLS);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_code:

                // What to do when save

//                @Override
//                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Artist artist = artists.get(i);
                showUpdateDeleteDialog();



                break;
            case R.id.action_delete:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void showUpdateDeleteDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_code, null);
        dialogBuilder.setView(dialogView);


        final ImageButton buttonCancel = (ImageButton) dialogView.findViewById(R.id.btnCancel);
        final EditText codeinvite = (EditText) dialogView.findViewById(R.id.code);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);

      //  final Button buttoncancel=(Button)dialogView.findViewById(R.id.btnCancel);

//        dialogBuilder.setTitle(artistName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> docData = new HashMap<>();

                docData.put("patients", Arrays.asList(codeinvite.getText().toString()));


                mFirestore.collection("guardian").document(mCurrentUser.getUid())
                        .set(docData)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(getApplicationContext(),"Data saved", Toast.LENGTH_SHORT).show();
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
            }
        });




        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

              b.dismiss();
            }
        });

    }



    /***
     * Load guardians list
     */
    private void loadOther() {

        mFirestore
                .collection("guardian")
                .document(mCurrentUser.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        PatientModel model = documentSnapshot.toObject(PatientModel.class);
                        for(String patient: model.getPatients()) {
                            loadUser(patient, "profile");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    /***
     * Load 3rd party user
     * @param id
     */
    private void loadUser(String id,String col) {

       mDatabase
               .getReference(id)
               .child(col)
               .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // clear table
                       //Log.d(TAG, dataSnapshot.toString());
                        for (DataSnapshot otherSnapshot : dataSnapshot.getChildren()) {

                            Log.d(TAG, dataSnapshot.toString());
                            OtherModel model = otherSnapshot.getValue(OtherModel.class);
                            mAdapter.addData(model);
//                            mKeys.add(noteSnapshot.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){

            case R.id.nav_home:
                Intent h= new Intent(Patient1.this,MainActivity.class);
                startActivity(h);
                break;
            case R.id.nav_pills:
                Intent i= new Intent(Patient1.this,MainPills.class);
                startActivity(i);
                break;
            case R.id.nav_appointment:
                Intent g= new Intent(Patient1.this,MainAppointment.class);
                startActivity(g);
                break;
            case R.id.nav_history:
                Intent s= new Intent(Patient1.this,MainHistory.class);
                startActivity(s);
                break;
            case R.id.nav_guardian:
                Intent t= new Intent(Patient1.this,MainGuardian.class);
                startActivity(t);
                break;
            case R.id.nav_other:
                Intent u= new Intent(Patient1.this,Patient1.class);
                startActivity(u);
                break;
            case R.id.nav_mail:
                Intent v= new Intent(Patient1.this,Profile.class);
                startActivity(v);
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
