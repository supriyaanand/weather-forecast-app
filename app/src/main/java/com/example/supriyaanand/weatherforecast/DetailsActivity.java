package com.example.supriyaanand.weatherforecast;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DetailsActivity extends AppCompatActivity {

    FragmentTab1 hoursFragment = new FragmentTab1();
    FragmentTab2 daysFragment = new FragmentTab2();
    Intent intent;
    String state;
    String city;
    String unit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        setContentView(R.layout.content_details);

        intent = getIntent();
        state = intent.getStringExtra("state");
        city = intent.getStringExtra("city");
        unit = intent.getStringExtra("unit");
        String details = "More details for " + city + ", " + state;
        TextView t = new TextView(this);
        t = (TextView)findViewById(R.id.detailsString);
        t.setText(details);
        Button hours = (Button) findViewById(R.id.hours);
        hours.setBackground(getResources().getDrawable(R.drawable.buttonshape));
//        CardView hoursC = (CardView) findViewById(R.id.hoursCard);
//        hoursC.setRadius(15);
        Button days = (Button) findViewById(R.id.days);
        days.setBackground(getResources().getDrawable(R.drawable.buttonshapeplain));
//        CardView daysC = (CardView) findViewById(R.id.daysCard);
//        daysC.setRadius(6);


        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }


            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            hoursFragment.setArguments(getIntent().getExtras());
            daysFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction().add(R.id.fragment_container, hoursFragment).commit();
            //getFragmentManager().beginTransaction().add(R.id.fragment_container, daysFragment).commit();
        }
    }

    public void onDaysClicked(View view){
        Button hours = (Button) findViewById(R.id.hours);
        hours.setBackground(getResources().getDrawable(R.drawable.buttonshapeplain));
//        CardView hoursC = (CardView) findViewById(R.id.hoursCard);
//        hoursC.setRadius(6);
        Button days = (Button) findViewById(R.id.days);
        days.setBackground(getResources().getDrawable(R.drawable.buttonshape));
//        CardView daysC = (CardView) findViewById(R.id.daysCard);
//        daysC.setRadius(15);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, daysFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void onHoursClicked(View view){
        Button hours = (Button) findViewById(R.id.hours);
        hours.setBackground(getResources().getDrawable(R.drawable.buttonshape));
//        CardView hoursC = (CardView) findViewById(R.id.hoursCard);
//        hoursC.setRadius(15);
        Button days = (Button) findViewById(R.id.days);
        days.setBackground(getResources().getDrawable(R.drawable.buttonshapeplain));
//        CardView daysC = (CardView) findViewById(R.id.daysCard);
//        daysC.setRadius(6);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, hoursFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }


}
