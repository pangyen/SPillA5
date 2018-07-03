package com.example.fatinnabila.spilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by fatin nabila on 21/3/2018.
 */

public class AddGuardian extends AppCompatActivity{


    private EditText mTVTitle;
    private EditText mTVDescription;
    private EditText mTVGemail;
    private TextView code;


    private DatabaseReference mReference;

    private String mId;

    Button addEmail;

    // Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();

        setContentView(R.layout.add_guardian);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //addEmail = (Button) findViewById(R.id.btn_addEmail);
        code=(TextView)findViewById(R.id.et_code);

        code.setText(mCurrentUser.getUid());

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Binding
        mTVTitle = (EditText) findViewById(R.id.et_titleG);
        mTVDescription = (EditText) findViewById(R.id.et_descriptionG);
        mTVGemail=(EditText)findViewById(R.id.et_emailG);

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
                            mTVGemail.setText(model.getEmail());
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
        getMenuInflater().inflate(R.menu.menu_guardian, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_send:

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{mTVGemail.getText().toString()});
                i.putExtra(Intent.EXTRA_SUBJECT, "SPillA Invite Code");
                i.putExtra(Intent.EXTRA_TEXT   , "Dear Friend, I would like to be my SPillA apps friends.Get SPillA app at..Your invite code is  "  + generateHex() +" " +
                        "If you are aggree to be my guardian for my pills schedule, please insert " +
                        "this code  "+ mCurrentUser.getUid() );

                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(AddGuardian.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }


                GuardianModel model = new GuardianModel(
                        mTVTitle.getText().toString(),
                        mTVDescription.getText().toString(),
                        mTVGemail.getText().toString()
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

    /***
     * Save record to firebase
     * @param model
     */
    private void save(GuardianModel model,
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
            Toast.makeText(AddGuardian.this, successResourceId, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(AddGuardian.this, error.getCode(), Toast.LENGTH_SHORT).show();
        }
    }


    public static String generateHex() {

        SecureRandom random = new SecureRandom();
        String randomCode = new BigInteger(30, random).toString(32).toUpperCase();
        return randomCode;
    }

}
