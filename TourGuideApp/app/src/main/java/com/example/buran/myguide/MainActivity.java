package com.example.buran.myguide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the View that shows this category
        TextView generalInfo = (TextView) findViewById(R.id.general_info);

        //Set a click listener on tht View
        generalInfo.setOnClickListener(new View.OnClickListener() {
            //This code will be executed when the general info category is clicked on
            @Override
            public void onClick(View v) {
                Intent generalInfoIntent = new Intent(MainActivity.this, GeneralInfoActivity.class);
                startActivity(generalInfoIntent);

            }
        });

        //Find the View that shows this category
        TextView attraction = (TextView) findViewById(R.id.attraction);

        //Set a click listener on tht View
        attraction.setOnClickListener(new View.OnClickListener() {
            //This code will be executed when the general info category is clicked on
            @Override
            public void onClick(View v) {
                Intent attractionIntent = new Intent(MainActivity.this, AttractionActivity.class);
                startActivity(attractionIntent);
            }
        });

        //Find the View that shows this category
        TextView cafe = (TextView) findViewById(R.id.cafe);

        //Set a click listener on tht View
        cafe.setOnClickListener(new View.OnClickListener() {
            //This code will be executed when the general info category is clicked on
            @Override
            public void onClick(View v) {
                Intent cafeIntent = new Intent(MainActivity.this, CafeActivity.class);
                startActivity(cafeIntent);
            }
        });

        //Find the View that shows this category
        TextView historicSites = (TextView) findViewById(R.id.historic_sites);

        //Set a click listener on tht View
        historicSites.setOnClickListener(new View.OnClickListener() {
            //This code will be executed when the general info category is clicked on
            @Override
            public void onClick(View v) {
                Intent historicSitesIntent = new Intent(MainActivity.this, HistoricSitesActivity.class);
                startActivity(historicSitesIntent);
            }
        });

        //Find the View that shows this category
        TextView topToDo = (TextView) findViewById(R.id.top_to_do);

        //Set a click listener on tht View
        topToDo.setOnClickListener(new View.OnClickListener() {
            //This code will be executed when the general info category is clicked on
            @Override
            public void onClick(View v) {
                Intent topToDoIntent = new Intent(MainActivity.this, TopToDoActivity.class);
                startActivity(topToDoIntent);
            }
        });
    }
}
