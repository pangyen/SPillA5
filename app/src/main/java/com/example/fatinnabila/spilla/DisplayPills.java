package com.example.fatinnabila.spilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fatinnabila.spilla.adapter.PillsAdapter;
import com.example.fatinnabila.spilla.model.PatientModel;
import com.example.fatinnabila.spilla.model.PillsModel;
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

public class DisplayPills extends AppCompatActivity {

    private final static String

            TAG = DisplayPills.class.getSimpleName();

    //    private OtherAdapter mAdapter;
    private PillsAdapter mAdapter;

    // Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseDatabase mDatabase, childRef;
    private FirebaseFirestore mFirestore;

    private DatabaseReference mOtherReference;

    private TextView mTVTitle;
    private TextView mTVDescription;

    CardView mycard ;
    Intent i,j,k,l,m,n,o;


//    RecyclerView rv;
//    ListView listViewother;

    //RecyclerView<OtherModel> other;
    //  RecyclerView recyclerView1;

    private ArrayList<String> mKeys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_displaypills);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance();


        //cardview
        mycard= (CardView) findViewById(R.id.cvv_monday);
        i = new Intent(this,DisplayBox1.class);
        mycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });

        //cardview
        mycard= (CardView) findViewById(R.id.cvv_tuesday);
        j = new Intent(this,DisplayBox2.class);
        mycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(j);
            }
        });
        //cardview
        mycard= (CardView) findViewById(R.id.cvv_wed);
        k = new Intent(this,DisplayBox3.class);
        mycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(k);
            }
        });

        //cardview
        mycard= (CardView) findViewById(R.id.cvv_thu);
        l = new Intent(this,DisplayBox4.class);
        mycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(l);
            }
        });

        //cardview
        mycard= (CardView) findViewById(R.id.cvv_fri);
        m = new Intent(this,DisplayBox5.class);
        mycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(m);
            }
        });


        //cardview
        mycard= (CardView) findViewById(R.id.cvv_sat);
        n= new Intent(this,DisplayBox6.class);
        mycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(n);
            }
        });


        //cardview
        mycard= (CardView) findViewById(R.id.cvv_sun);
        o= new Intent(this,DisplayBox7.class);
        mycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(o);
            }
        });

        // Load guardian
     //   loadOther();


//      // mAdapter = new PillsAdapter(this,null);
//        //recycler view
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_displaypills);
//        recyclerView.setLayoutManager(new LinearLayoutManager(DisplayPills.this));
//
//        mAdapter = new PillsAdapter(this, new PillsAdapter.OnItemClick() {
//            @Override
//            public void onClick(int pos) {
//                // Open back note activity with data
//               Intent intent = new Intent(getApplicationContext(), DisplayPills.class);
//                //          intent.putExtra(Reference.PILLS_ID, mKeys.get(pos));
//                //startActivity(intent);
//            }
//        });
//        recyclerView.setAdapter(mAdapter);
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
                            loadUser(patient, "box_1");
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
                        for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {

                            Log.d(TAG, dataSnapshot.toString());
                            PillsModel model = noteSnapshot.getValue(PillsModel.class);
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
