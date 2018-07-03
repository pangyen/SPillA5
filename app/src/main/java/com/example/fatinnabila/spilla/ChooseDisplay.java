package com.example.fatinnabila.spilla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChooseDisplay extends AppCompatActivity {


    Button viewPills,viewAppointment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_display);

        viewPills = (Button) findViewById(R.id.btn_viewpill);
        viewAppointment=(Button)findViewById(R.id.btn_viewappointment);

        viewPills.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Intent intent = new Intent(getApplicationContext(), DisplayPills.class);
               //          intent.putExtra(Reference.PILLS_ID, mKeys.get(pos));
                                            startActivity(intent);


                                        }

                                    }
        );

        viewAppointment.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {

                                             Intent intent = new Intent(getApplicationContext(), DisplayAppointment.class);
                                             //          intent.putExtra(Reference.PILLS_ID, mKeys.get(pos));
                                             startActivity(intent);


                                         }

                                     }
        );

    }
}
