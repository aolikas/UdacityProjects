package com.example.buran.myguide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by buran on 02.07.17.
 */

public class ItemAdapter extends ArrayAdapter<Item> {

    /**
     * Create a new object
     *
     * @param context   is a current context(Activity) that the adapter is being created in
     * @param listItems is a list that can be displayed
     */

    public ItemAdapter(Context context, ArrayList<Item> listItems) {
        super(context, 0, listItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get the link of Item object located at this position in the list
        Item currentItem = getItem(position);

        //Find the TextView in the list_item.xml layout with the ID place_name
        TextView placeTextView = (TextView) listItemView.findViewById(R.id.place_name);

        //Get the place name from the current Item object and set the text
        placeTextView.setText(currentItem.getPlace());

        //Find the TextView in the list_item.xml layout with the ID description
        TextView descriptionTextView = (TextView) listItemView.findViewById(R.id.description);

        //Get the description of place from the current Item object and set the text
        descriptionTextView.setText(currentItem.getPlaceDescription());

        //Find the ImageView in the list_item layout with id image
        ImageView imagePlace = (ImageView) listItemView.findViewById(R.id.image_view);

        //Check if the image is provided or not
        if (currentItem.hasImage()) {
            //if the image is available - display it
            imagePlace.setImageResource(currentItem.getImageResourceId());
            //Make sure that image is visible
            imagePlace.setVisibility(View.VISIBLE);
        } else {
            //otherwise will hide the image
            imagePlace.setVisibility(View.GONE);
        }

        //return the whole list item layout
        return listItemView;
    }
}
