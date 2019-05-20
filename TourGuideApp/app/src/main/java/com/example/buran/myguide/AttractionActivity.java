package com.example.buran.myguide;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AttractionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attraction);


        //Create a list of attraction items
        final ArrayList<Item> listItems = new ArrayList<Item>();
        listItems.add(new Item(getString(R.string.place_to_visit_1), R.drawable.hamburg_1));
        listItems.add(new Item(getString(R.string.place_to_visit_2), R.drawable.hamburg_2));
        listItems.add(new Item(getString(R.string.place_to_visit_3), R.drawable.hamburg_3));
        listItems.add(new Item(getString(R.string.place_to_visit_4), R.drawable.hamburg_4));
        listItems.add(new Item(getString(R.string.place_to_visit_5), R.drawable.hamburg_5));
        listItems.add(new Item(getString(R.string.place_to_visit_6), R.drawable.hamburg_2));
        listItems.add(new Item(getString(R.string.place_to_visit_7), R.drawable.hamburg_3));

        //Create an ItemAdapter .It knows how to create the list items for each activity
        ItemAdapter adapter = new ItemAdapter(this, listItems);
        ArrayAdapter<Item> itemsAdapter = new ArrayAdapter<Item>(this, R.layout.list_item, listItems);

        //find the ListView object
        ListView listView = (ListView) findViewById(R.id.main_list);
        listView.setAdapter(adapter);
    }
}
