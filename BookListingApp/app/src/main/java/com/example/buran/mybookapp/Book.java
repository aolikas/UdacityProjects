package com.example.buran.mybookapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by buran on 06.07.17.
 */

public class Book implements Parcelable {
    //var - the name of book
    private String mTitle;
    // var - the name of author(s) of a book
    private String mAuthors;

    //create a new Book object
    public Book(String title, String author) {
        mTitle = title;
        mAuthors = author;
    }

    //get the name of book
    public String getTitle() {
        return mTitle;
    }


    //get the array of authors
    public String getAuthors() {
        return mAuthors;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mAuthors);
    }

    protected Book(Parcel in) {
        mTitle = in.readString();
        mAuthors = in.readString();
    }

    public static final Parcelable.Creator<Book> CREATOR = new Parcelable.Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            return new Book(source);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}


