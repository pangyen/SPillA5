package com.example.fatinnabila.spilla;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class DisplayBox5 extends AppCompatActivity {

    private final static String

            TAG = DisplayBox1.class.getSimpleName();

    //    private OtherAdapter mAdapter;
    private PillsAdapter mAdapter;

    // Firebase Authentication
    private DatabaseReference rootRef, demoRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    private FirebaseDatabase mDatabase, childRef;
    private FirebaseFirestore mFirestore;

    private DatabaseReference mOtherReference;

    private TextView mTVTitle;
    private TextView mTVDescription;
    TextView statusPills;


//    RecyclerView rv;
//    ListView listViewother;

    //RecyclerView<OtherModel> other;
    //  RecyclerView recyclerView1;

    private ArrayList<String> mKeys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_displayb1);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        // Load guardian
        loadOther();


        // mAdapter = new PillsAdapter(this,null);
        //recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_displaypb1);
        recyclerView.setLayoutManager(new LinearLayoutManager(DisplayBox5.this));

        mAdapter = new PillsAdapter(this, new PillsAdapter.OnItemClick() {
            @Override
            public void onClick(int pos) {
                // Open back note activity with data
                Intent intent = new Intent(getApplicationContext(), DisplayPills.class);
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
                            loadUser(patient, "box_5");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


        //display current status pills
        statusPills = (TextView) findViewById(R.id.tvv_statusBox1);
        rootRef = FirebaseDatabase.getInstance().getReference();
        // demoRef = rootRef.child("PB1381001237");
        demoRef = rootRef;
        demoRef.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                statusPills.setText(value);

                if (value.equals("Pills Not Taken")) {
                    createNotification1();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
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
                        for (DataSnapshot boxSnapshot : dataSnapshot.getChildren()) {

                            Log.d(TAG, dataSnapshot.toString());
                            PillsModel model = boxSnapshot.getValue(PillsModel.class);
                            mAdapter.addData(model);
//                            mKeys.add(noteSnapshot.getKey());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


    public void createNotification1() {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent notification_intent = new Intent(this, DisplayBox1.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notification_intent, 0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.ic_save_white_24dp);
        notificationBuilder.setContentTitle("Pills is still Not Taken");
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);
        //Notification noti = new Notification.Builder(this)
        //.setContentTitle("New mail from " + "test@gmail.com")
//                .setContentText("Subject").setSmallIcon(R.drawable.icon)
//                .setContentIntent(pendingIntent)
//                .addAction(R.drawable.icon, "Call", pendingIntent)
//                .addAction(R.drawable.icon, "More", pendingIntent)
//                .addAction(R.drawable.icon, "And more", pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        // notificationBuilder.flags |= Notification.FLAG_AUTO_CANCEL;

        //notificationManager.notify(0, noti);

        //NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());


//         Create the NotificationChannel, but only on API 26+ because
//         the NotificationChannel class is new and not in the support library

    }
}
