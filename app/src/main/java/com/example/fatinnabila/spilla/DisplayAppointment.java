package com.example.fatinnabila.spilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.fatinnabila.spilla.adapter.AppointmentAdapter;
import com.example.fatinnabila.spilla.model.AppointmentModel;
import com.example.fatinnabila.spilla.model.PatientModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

/**
 * Created by fatin nabila on 30/4/2018.
 */

public class DisplayAppointment extends AppCompatActivity {


    private final static String

            TAG = DisplayPills.class.getSimpleName();

    //    private OtherAdapter mAdapter;
    private AppointmentAdapter mAdapter;

    // Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseDatabase mDatabase, childRef;
    private FirebaseFirestore mFirestore;

    private DatabaseReference mOtherReference;

    private TextView mTVTitle;
    private TextView mTVDescription;


//    RecyclerView rv;
//    ListView listViewother;

    //RecyclerView<OtherModel> other;
    //  RecyclerView recyclerView1;

    private ArrayList<String> mKeys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_displayappointment);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        // Load guardian
        loadOther();


        // mAdapter = new PillsAdapter(this,null);
        //recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_displayappointment);
        recyclerView.setLayoutManager(new LinearLayoutManager(DisplayAppointment.this));

        mAdapter = new AppointmentAdapter(this, new AppointmentAdapter.OnItemClick() {
            @Override
            public void onClick(int pos) {
                // Open back note activity with data
                Intent intent = new Intent(getApplicationContext(), DisplayAppointment.class);
                //          intent.putExtra(Reference.PILLS_ID, mKeys.get(pos));
                //startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);
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
                            loadUser(patient, "appointment");
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
    private void loadUser(String id, String col) {

        mDatabase
                .getReference(id)
                .child(col)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // clear table
                        // Log.d(TAG, dataSnapshot.toString());
                        for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {

                            Log.d(TAG, dataSnapshot.toString());
                            AppointmentModel model = appointmentSnapshot.getValue(AppointmentModel.class);
                            mAdapter.addData(model);
//                            mKeys.add(noteSnapshot.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}
