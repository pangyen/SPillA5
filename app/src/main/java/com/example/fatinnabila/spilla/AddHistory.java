package com.example.fatinnabila.spilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fatinnabila.spilla.data.Reference;
import com.example.fatinnabila.spilla.model.HistoryModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by fatin nabila on 16/3/2018.
 */

public class AddHistory extends AppCompatActivity {


    private EditText mTVHtitle;
    private EditText mTVHDes;

    private DatabaseReference mReference;

    private String mId;

    // Firebase Authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();

        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Binding
        mTVHtitle = (EditText) findViewById(R.id.et_titleHistory);
        mTVHDes = (EditText) findViewById(R.id.et_desHistory);

        mReference = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.DB_HISTORY);

        Intent intent = getIntent();
        // Load record
        if(intent != null) {
            mId = intent.getStringExtra(Reference.HISTORY_ID);
            if(mId != null) {
                mReference.child(mId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HistoryModel model = dataSnapshot.getValue(HistoryModel.class);
                        if(model != null) {
                            mTVHtitle.setText(model.getTitle());
                            mTVHDes.setText(model.getDescription());
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

                // What to do when save
               HistoryModel model = new HistoryModel(
                        mTVHtitle.getText().toString(),
                        mTVHDes.getText().toString()

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
    private void save(HistoryModel model,
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
            Toast.makeText(AddHistory.this, successResourceId, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(AddHistory.this, error.getCode(), Toast.LENGTH_SHORT).show();
        }
    }

}
