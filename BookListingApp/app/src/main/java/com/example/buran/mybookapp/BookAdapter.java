package com.example.buran.mybookapp;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by buran on 06.07.17.
 */
public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        TextView titleBook = (TextView) listItemView.findViewById(R.id.title);
        titleBook.setText(currentBook.getTitle());

        TextView authorBook = (TextView) listItemView.findViewById(R.id.author);
        authorBook.setText(currentBook.getAuthors());

        return listItemView;
    }
}

