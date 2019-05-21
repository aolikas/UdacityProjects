package com.example.buran.mybookapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private BookAdapter mBookAdapter;

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * URL to query the Book dataset for app
     */
    private static final String BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    ArrayList<Book> booksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "in oncreate()");
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null || !savedInstanceState.containsKey("keyBookList")) {
            booksList = new ArrayList<>();
        } else {
            booksList = savedInstanceState.getParcelableArrayList("keyBookList");
        }

        final EditText inputString = (EditText) findViewById(R.id.search_book);

        Button searchButton = (Button) findViewById(R.id.find_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = inputString.getText().toString().replace(" ", "+");
                String url = (BOOK_REQUEST_URL + search);
                if (isOnline(getApplicationContext())) {
                    BookAsyncTask task = new BookAsyncTask();
                    task.execute(url);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_text), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ListView bookListView = (ListView) findViewById(R.id.book_list_view);
        bookListView.setEmptyView(findViewById(R.id.empty_list));

        mBookAdapter = new BookAdapter(this, booksList);
        bookListView.setAdapter(mBookAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(LOG_TAG, "in onSaveInstanceState()");
        outState.putParcelableArrayList("keyBookList", booksList);
        super.onSaveInstanceState(outState);
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    private class BookAsyncTask extends AsyncTask<String, Void, ArrayList<Book>> {


        /**
         * {@link AsyncTask} to perform the network request on a background thread, and then
         * update the UI with books in the response.
         */
        @Override
        protected ArrayList<Book> doInBackground(String... urls) {

            // Create URL object
            URL url = createUrl(urls[0]);

            // Perform HTTP request to the URL and receive a JSON response back
            String jsonResponse = "";
            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, getString(R.string.io_exception_e), e);
            }
            // Extract relevant fields from the JSON response and create a Book object
            ArrayList<Book> books = extractFeatureFromJson(jsonResponse);
            // Return the Book object as the result fo the {@link BookAsyncTask}
            return books;
        }

        /**
         * Update the screen with the given books(titles, authors) (which was the result of the
         * {@link BookAsyncTask}).
         */
        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            mBookAdapter.clear();

            if (books != null) {
                mBookAdapter.addAll(books);
            }
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private URL createUrl(String stringUrl) {
            URL url;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException exception) {
                Log.e(LOG_TAG, getString(R.string.malformed_url_exception), exception);
                return null;
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.connect();

                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, getString(R.string.url_connection_error) + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, getString(R.string.http_request_io_exception), e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Return an {@link Book} object by parsing out information
         * about the first book from the input bookJSON string.
         */
        private ArrayList<Book> extractFeatureFromJson(String bookJSON) {

            if (TextUtils.isEmpty(bookJSON)) {
                return null;
            }

            booksList = new ArrayList<>();

            try {
                JSONObject baseJsonResponse = new JSONObject(bookJSON);
                JSONArray itemsArray = baseJsonResponse.getJSONArray("items");
                if (itemsArray.length() > 0) {
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsArrayJSONObject = itemsArray.getJSONObject(i);
                        JSONObject volumeInfo = itemsArrayJSONObject.getJSONObject("volumeInfo");
                        String title = volumeInfo.getString("title");

                        StringBuilder authors = new StringBuilder();
                        if (volumeInfo.has("authors")) {
                            JSONArray authorsArray = volumeInfo.getJSONArray("authors");

                            for (int j = 0; j < authorsArray.length(); j++) {
                                if (j > 0) {
                                    authors.append(", ");
                                }
                                authors.append(authorsArray.getString(j));
                            }
                            Book book = new Book(title, authors.toString());
                            booksList.add(book);
                        }
                    }
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, getString(R.string.json_exception), e);
            }
            return booksList;
        }
    }
}
