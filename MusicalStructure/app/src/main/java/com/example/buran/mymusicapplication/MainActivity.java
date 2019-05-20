package com.example.buran.mymusicapplication;

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

        TextView albums = (TextView) findViewById(R.id.albums);
        albums.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent albumsIntent = new Intent(MainActivity.this, AlbumsActivity.class);
                startActivity(albumsIntent);
            }
        });

        TextView artists = (TextView) findViewById(R.id.artists);
        artists.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent artistsIntent = new Intent(MainActivity.this, ArtistsActivity.class);
                startActivity(artistsIntent);
            }
        });

        TextView myCollection = (TextView) findViewById(R.id.my_collection);
        myCollection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myCollectionIntent = new Intent(MainActivity.this, MyCollectionActivity.class);
                startActivity(myCollectionIntent);
            }
        });

        TextView myPlaylist = (TextView) findViewById(R.id.my_playlist);
        myPlaylist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myPlaylistIntent = new Intent(MainActivity.this, MyPlaylistActivity.class);
                startActivity(myPlaylistIntent);
            }
        });

        TextView myDownloadedSongs = (TextView) findViewById(R.id.dowload_songs);
        myDownloadedSongs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myDownloadedSongsIntent = new Intent(MainActivity.this, DownloadActivity.class);
                startActivity(myDownloadedSongsIntent);
            }
        });
    }
}

