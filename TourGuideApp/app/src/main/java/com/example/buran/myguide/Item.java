package com.example.buran.myguide;

import android.widget.ImageView;

/**
 * Created by buran on 01.07.17.
 */

public class Item {

    //Name of the place
    private String mPlaceName;

    //Name of variable that will be describe place
    private String mPlaceDescription;

    //Image resource ID for the place
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    //Constant value that represents no image was provided for this place
    private static final int NO_IMAGE_PROVIDED = -1;

    /**
     * Create a new Item object
     *
     * @param placeName       is a name for some particular place in categories like attraction, cafe
     * @param imageResourceId is the drawable resource id for the image associated with the place
     */
    public Item(String placeName, int imageResourceId) {
        mPlaceName = placeName;
        mImageResourceId = imageResourceId;
    }

    /**
     * Create a new Item object
     *
     * @param placeName        is a name for some particular place in categories like attraction, cafe
     * @param descriptionPlace is a name that will be describe a place
     * @param imageResourceId  is the drawable resource id for the image associated with the place
     */
    public Item(String placeName, String descriptionPlace, int imageResourceId) {
        mPlaceName = placeName;
        mPlaceDescription = descriptionPlace;
        mImageResourceId = imageResourceId;
    }

    //get the place
    public String getPlace() {
        return mPlaceName;
    }

    //get the describtion of the place
    public String getPlaceDescription() {
        return mPlaceDescription;
    }

    //return the image resource ID for the place
    public int getImageResourceId() {
        return mImageResourceId;
    }

    //return whether or not there is an image for the place
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

}
