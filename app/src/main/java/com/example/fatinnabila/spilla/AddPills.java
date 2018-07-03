package com.example.fatinnabila.spilla;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fatinnabila.spilla.data.Reference;
import com.example.fatinnabila.spilla.model.PillsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddPills extends AppCompatActivity {

    private EditText mTVTitle;
    private EditText mTVDescription;
    private EditText mTVDose;
    private EditText mTVPurpose;
    private EditText mTVEffect;
    private DatabaseReference mReference;
    private String mId;

    // Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    public String[] tablet_list={"1 tablet","2 tablets","3 tablets","4 tablets","5 tablets","6 tablets","7 tablets"};
    public String[] description_list={"Take pills before eat!","Take pills after eat!"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();

        setContentView(R.layout.add_pills);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Binding
        mTVTitle = (EditText) findViewById(R.id.et_title);
        mTVDescription = (EditText) findViewById(R.id.et_description);
        mTVDose=(EditText) findViewById(R.id.et_dose);
        mTVPurpose=(EditText)findViewById(R.id.et_purpose);
        mTVEffect=(EditText)findViewById(R.id.et_effect) ;
        //Spinner pillsSpinner = findViewById(R.id.et_dose);
       // mTVDose = (Spinner) findViewById(R.id.et_dose);

        mReference = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.DB_PILLS);

        Intent intent = getIntent();
        // Load record
        if(intent != null) {
            mId = intent.getStringExtra(Reference.  PILLS_ID);
            if(mId != null) {
                mReference.child(mId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        PillsModel model = dataSnapshot.getValue(PillsModel.class);
                        if(model != null) {
                            mTVTitle.setText(model.getTitle());
                            mTVDescription.setText(model.getDescription());
                            mTVDose.setText(model.getDose());
                            mTVPurpose.setText(model.getPurpose());
                            mTVEffect.setText(model.getEffect());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }



        final ArrayAdapter<String> spinner_tablets = new  ArrayAdapter<String>(AddPills.this,android.R.layout.simple_spinner_dropdown_item, tablet_list);
        final ArrayAdapter<String> spinner_description = new  ArrayAdapter<String>(AddPills.this,android.R.layout.simple_spinner_dropdown_item, description_list);

        mTVDose.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(AddPills.this)
                        .setTitle("How Many Tablets?")
                        .setAdapter(spinner_tablets, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                mTVDose.setText(tablet_list[which].toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });

        mTVDescription.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new AlertDialog.Builder(AddPills.this)
                        .setTitle("Choose")
                        .setAdapter(spinner_description, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                mTVDescription.setText(description_list[which].toString());
                                dialog.dismiss();
                            }
                        }).create().show();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.action_delete);

        if(mId == null) {
            item.setEnabled(false);
            item.setVisible(false);
        } else {
            item.setEnabled(true);
            item.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:

                // What to do when save
                PillsModel model = new PillsModel(
                        mTVTitle.getText().toString(),
                        mTVDescription.getText().toString(),
                        //mTVDose.getText().toString()+"dose",
                      //  mTVDose.getSelectedItem().toString(),
                        mTVDose.getText().toString(),
                        mTVPurpose.getText().toString(),
                        mTVEffect.getText().toString()
                );

                save(model, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        actionNotification(databaseError, R.string.done_saved);
                    }
                });
                break;
            case R.id.action_delete:
                if(!mId.isEmpty()) {
                    mReference.child(mId).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            actionNotification(databaseError, R.string.done_deleted);
                        }
                    });
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //save data
    private void save(PillsModel model,
                      DatabaseReference.CompletionListener listener) {

        if(mId == null) {
            // generate id
            mId = mReference.push().getKey();
        }

        mReference.child(mId).setValue(model, listener);
    }

    private void actionNotification(DatabaseError error, int successResourceId) {
        // close activity
        if(error == null) {
            Toast.makeText(AddPills.this, successResourceId, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(AddPills.this, error.getCode(), Toast.LENGTH_SHORT).show();
        }
    }
}
