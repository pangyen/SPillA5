package com.example.fatinnabila.spilla;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.fatinnabila.spilla.adapter.Box1Adapter;
import com.example.fatinnabila.spilla.data.Reference;
import com.example.fatinnabila.spilla.model.Box1Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainBox1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

   // public static final String PILL_NAME = "com.example.fatinnabila.spilla.box1Id";
    public static final String DOSE_ID = "com.example.fatinnabila.spilla.title";

    //firebase database reference
    private DatabaseReference rootRef, demoRef;
    private DatabaseReference pillsRef;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    private String mId;
    private DatabaseReference box1Reference, test;

    //Drawer layout
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar = null;

    //alarm manager
    AlarmManager alarmManager1;
    private TextView alarmTimePicker;
    private TextView alarmTextView;
    private AlarmReceiver1 alarm;
    private PendingIntent pending_intent1;

    //a list to store all  from firebase database
    List<Box1Model> boxs1;
    ListView listViewBox1;
    Spinner spinner;
    ImageButton addpills;
    TextView statusPills;
    MainBox2 inst;
    Context context;
    int hour, minute;

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minuteOfHour) {
            hour = hourOfDay;
            minute = minuteOfHour;
            alarmTimePicker.setText(setTime(hour, minute));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_box1);
        this.context = this;
        boxs1 = new ArrayList<>();

        addpills = (ImageButton) findViewById(R.id.btn_addBox1);
        spinner = (Spinner) findViewById(R.id.spn_pills);
        listViewBox1 = (ListView) findViewById(R.id.listViewBox1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        statusPills = (TextView) findViewById(R.id.tv_statusBox1);
        final Calendar calendar = Calendar.getInstance();// set the alarm to the time that you picked
        alarmTimePicker = (TextView) findViewById(R.id.alarmTimePicker2);
        Button start_alarm = (Button) findViewById(R.id.start_alarm);
        final Intent myIntent = new Intent(this.context, AlarmReceiver1.class);
        alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);// Get the alarm manager service
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        test = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.TIME1);
        demoRef = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.DB_MESSAGE);
        box1Reference = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.DB_BOX1);

        //toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //listview
        pillsRef = FirebaseDatabase.getInstance().getReference(mCurrentUser.getUid()).child(Reference.DB_PILLS);
        pillsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> pills = new ArrayList<String>();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    String pillName = areaSnapshot.child("title").getValue(String.class);
                    String pillDose = areaSnapshot.child("dose").getValue(String.class); //
                    pills.add(pillName +"   "+pillDose);//
                }

                Spinner pillsSpinner = (Spinner) findViewById(R.id.spn_pills);
                ArrayAdapter<String> pillsAdapter = new ArrayAdapter<String>(MainBox1.this, android.R.layout.simple_spinner_dropdown_item, pills);
                pillsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pillsSpinner.setAdapter(pillsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //display current status pills
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

        //displau time
        test.child("time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String viewtime = dataSnapshot.getValue(String.class);
                alarmTimePicker.setText(viewtime);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        //display status alarm text
        test.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String viewtime = dataSnapshot.getValue(String.class);
                alarmTextView.setText(viewtime);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });

        //add button pills
        addpills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addBox1();
            }

        });


////        //delete
////        deletepills.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////               if(!mId.isEmpty()) {
//        mReference.child(mId).removeValue(new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                actionNotification(databaseError, R.string.done_deleted);
//            }
//        });
////            }
////
////        });

        alarmTimePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(MainBox1.this,
                        //R.style.Theme_AppCompat_Dialog,
                        t,
                        hour,
                        minute,
                        false).show();
            }
        });
        alarmTimePicker.setText(setTime(hour, minute));


        start_alarm.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)

            @Override
            public void onClick(View v) {

                Log.e("MyActivity", "In the receiver with " + hour + " and " + minute);
                setAlarmText("Already set alarm " + hour + " hours " + minute + "minutes");
                test.child("time").setValue(alarmTimePicker.getText().toString());
                test.child("status").setValue(alarmTextView.getText().toString());

                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 1);
                }

                if (calendar.after(Calendar.getInstance())) {
                    myIntent.putExtra("extra", "yes");
                    pending_intent1 = PendingIntent.getBroadcast(MainBox1.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager1.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmManager1.INTERVAL_DAY*7,pending_intent1);
                }

            }

        });

        Button stop_alarm = (Button) findViewById(R.id.stop_alarm);
        stop_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAlarmText("Already set alarm " + hour + " hours " + minute + " minutes");
                // test.child("time").setValue("");
                test.child("status").setValue("Alarm Cancelled");
                myIntent.putExtra("extra", "no");
                sendBroadcast(myIntent);

                alarmManager1.cancel(pending_intent1);
                setAlarmText("Alarm canceled");
            }
        });


    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }


    @Override
    public void onStart() {
        super.onStart();
        //inst = this;

        // listening for changes
        box1Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // clear table
                boxs1.clear();
                // load data
                for (DataSnapshot addBox1Snapshot : dataSnapshot.getChildren()) {

                    Box1Model model = addBox1Snapshot.getValue(Box1Model.class);
                    boxs1.add(model);
                }

                //creating adapter
                Box1Adapter box1Adapter = new Box1Adapter(MainBox1.this, boxs1);
                //attaching adapter to the listview
                listViewBox1.setAdapter(box1Adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // stop listening
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("MyActivity", "on Destroy");
    }


    public void createNotification1() {
        Intent notification_intent = new Intent(this, MainBox1.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notification_intent, 0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.ic_pills2);
        notificationBuilder.setContentTitle("Pills is still Not Taken");
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }
    private void addBox1() {
        String pillstype = spinner.getSelectedItem().toString();
        String id = box1Reference.push().getKey();
        Box1Model box1 = new Box1Model(id, pillstype);
        box1Reference.child(id).setValue(box1);
        Toast.makeText(this, "Pills added", Toast.LENGTH_LONG).show();
    }

    public String setTime(int hour, int minute) {
        String am_pm = (hour < 12) ? "am" : "pm";
        int nonMilitaryHour = hour % 12;
        if (nonMilitaryHour == 0)
            nonMilitaryHour = 12;
        String minuteWithZero;
        if (minute < 10)
            minuteWithZero = "0" + minute;
        else
            minuteWithZero = "" + minute;
        return nonMilitaryHour + ":" + minuteWithZero + am_pm;
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.nav_home:
                Intent h = new Intent(MainBox1.this, MainActivity.class);
                startActivity(h);
                break;
            case R.id.nav_pills:
                Intent i = new Intent(MainBox1.this, MainPills.class);
                startActivity(i);
                break;
            case R.id.nav_appointment:
                Intent g = new Intent(MainBox1.this, MainAppointment.class);
                startActivity(g);
                break;
            case R.id.nav_history:
                Intent s = new Intent(MainBox1.this, MainHistory.class);
                startActivity(s);
                break;
            case R.id.nav_guardian:
                Intent t = new Intent(MainBox1.this, MainGuardian.class);
                startActivity(t);
                break;
            case R.id.nav_mail:
                Intent v= new Intent(MainBox1.this,Profile.class);
                startActivity(v);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
