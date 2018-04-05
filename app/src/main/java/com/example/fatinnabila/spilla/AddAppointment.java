package com.example.fatinnabila.spilla;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fatinnabila.spilla.data.Reference;
import com.example.fatinnabila.spilla.model.AppointmentModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * Created by fatin nabila on 15/3/2018.
 */

public class AddAppointment extends AppCompatActivity {


    private EditText mTVAtitle;
    private EditText mTVADate;
    private EditText mTVAtime;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialog;

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

        setContentView(R.layout.activity_appointment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Binding
        mTVAtitle = (EditText) findViewById(R.id.et_titleAppointment);
        mTVADate = (EditText) findViewById(R.id.et_dateAppointment);
        mTVAtime = (EditText)findViewById(R.id.et_timeAppointment);

        mReference = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.DB_APPOINTMENT);

        Intent intent = getIntent();
        // Load record
        if(intent != null) {
            mId = intent.getStringExtra(Reference.APPOINTMENT_ID);
            if(mId != null) {
                mReference.child(mId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        AppointmentModel model = dataSnapshot.getValue(AppointmentModel.class);
                        if(model != null) {
                            mTVAtitle.setText(model.getTitle());
                            mTVADate.setText(model.getDate());
                            mTVAtime.setText(model.getTime());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }


        mTVAtime.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AddAppointment.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hour,
                                                  int minutes) {

                                // set day of month , month and year value in the edit text
                               mTVAtime.setText(hour + ":" + minutes);

                            }
                        }, hour, minute, false);


                timePickerDialog.show();

            }

        });

        mTVADate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(AddAppointment.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                mTVADate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
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
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:

                // What to do when save
                AppointmentModel model = new AppointmentModel(
                        mTVAtitle.getText().toString(),
                        mTVADate.getText().toString(),
                        mTVAtime.getText().toString()

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
    private void save(AppointmentModel model,
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
            Toast.makeText(AddAppointment.this, successResourceId, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(AddAppointment.this, error.getCode(), Toast.LENGTH_SHORT).show();
        }
    }
}