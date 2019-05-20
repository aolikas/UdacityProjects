package com.example.buran.myguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoricSitesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic_sites);
        setContentView(R.layout.activity_attraction);

        //Create a list of attraction items
        final ArrayList<Item> listItems = new ArrayList<Item>();
        listItems.add(new Item(getString(R.string.historic_site_1), getString(R.string.historic_site_address_1), R.drawable.hamburg_1));
        listItems.add(new Item(getString(R.string.historic_site_2), getString(R.string.historic_site_address_2), R.drawable.hamburg_2));
        listItems.add(new Item(getString(R.string.historic_site_3), getString(R.string.historic_site_address_3), R.drawable.hamburg_3));
        listItems.add(new Item(getString(R.string.historic_site_4), getString(R.string.historic_site_address_4), R.drawable.hamburg_4));
        listItems.add(new Item(getString(R.string.historic_site_5), getString(R.string.historic_site_address_5), R.drawable.hamburg_5));
        listItems.add(new Item(getString(R.string.historic_site_6), getString(R.string.historic_site_address_6), R.drawable.hamburg_1));
        listItems.add(new Item(getString(R.string.historic_site_7), getString(R.string.historic_site_address_7), R.drawable.hamburg_3));

        ItemAdapter adapter = new ItemAdapter(this, listItems);
        ListView listView = (ListView) findViewById(R.id.main_list);
        listView.setAdapter(adapter);
    }
}
