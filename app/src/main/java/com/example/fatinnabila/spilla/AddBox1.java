package com.example.fatinnabila.spilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fatinnabila.spilla.data.Reference;
import com.example.fatinnabila.spilla.model.Box1Model;
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

/**
 * Created by fatin nabila on 1/4/2018.
 */

public class AddBox1 extends AppCompatActivity{

    private EditText mTVTitle;
    private EditText mTVDescription;
    private DatabaseReference mReference;
    private String mId;

    Button sendI;

    // Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();

        setContentView(R.layout.add_box1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Binding
        mTVTitle = (EditText) findViewById(R.id.et_titleG);
        mTVDescription = (EditText) findViewById(R.id.et_descriptionG);

        mReference = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.DB_GUARDIAN);


        Intent intent = getIntent();
        // Load record
        if(intent != null) {
            mId = intent.getStringExtra(Reference.GUARDIAN_ID);
            if(mId != null) {
                mReference.child(mId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GuardianModel model = dataSnapshot.getValue(GuardianModel.class);
                        if(model != null) {
                            mTVTitle.setText(model.getTitle());
                            mTVDescription.setText(model.getDescription());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
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
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:


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

    /***
     * Save record to firebase
     * @param model
     */
    private void save(Box1Model model,
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
            Toast.makeText(AddBox1.this, successResourceId, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(AddBox1.this, error.getCode(), Toast.LENGTH_SHORT).show();
        }
    }


    public static String generateHex() {

        SecureRandom random = new SecureRandom();
        String randomCode = new BigInteger(30, random).toString(32).toUpperCase();
        return randomCode;
    }
}
