package com.example.buran.myguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class CafeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cafe);
        setContentView(R.layout.activity_attraction);

        //Create a list of attraction items
        final ArrayList<Item> listItems = new ArrayList<Item>();
        listItems.add(new Item(getString(R.string.cafe_1), getString(R.string.cost_1), R.drawable.food_1));
        listItems.add(new Item(getString(R.string.cafe_2),getString(R.string.cost_1), R.drawable.food_2));
        listItems.add(new Item(getString(R.string.cafe_3), getString(R.string.cost_1), R.drawable.food_3));
        listItems.add(new Item(getString(R.string.cafe_4), getString(R.string.cost_2), R.drawable.food_4));
        listItems.add(new Item(getString(R.string.cafe_5), getString(R.string.cost_2), R.drawable.food_5));
        listItems.add(new Item(getString(R.string.cafe_6), getString(R.string.cost_1), R.drawable.food_1));
        listItems.add(new Item(getString(R.string.cafe_7), getString(R.string.cost_2), R.drawable.food_3));

        ItemAdapter adapter = new ItemAdapter(this, listItems);
        ListView listView = (ListView) findViewById(R.id.main_list);
        listView.setAdapter(adapter);
    }
}
