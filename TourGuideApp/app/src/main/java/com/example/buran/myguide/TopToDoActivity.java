package com.example.buran.myguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class TopToDoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_to_do);
        setContentView(R.layout.activity_attraction);

        //Create a list of attraction items
        final ArrayList<Item> listItems = new ArrayList<Item>();
        listItems.add(new Item(getString(R.string.historic_site_1), getString(R.string.historic_site_address_1), R.drawable.hamburg_1));
        listItems.add(new Item(getString(R.string.historic_site_2), getString(R.string.historic_site_address_2), R.drawable.hamburg_2));
        listItems.add(new Item(getString(R.string.historic_site_3), getString(R.string.historic_site_address_3), R.drawable.hamburg_3));
        listItems.add(new Item(getString(R.string.cafe_1), getString(R.string.cost_1), R.drawable.food_1));
        listItems.add(new Item(getString(R.string.cafe_2), getString(R.string.cost_1), R.drawable.food_2));
        listItems.add(new Item(getString(R.string.cafe_3), getString(R.string.cost_1), R.drawable.food_3));

        ItemAdapter adapter = new ItemAdapter(this, listItems);
        ListView listView = (ListView) findViewById(R.id.main_list);
        listView.setAdapter(adapter);
    }
}
