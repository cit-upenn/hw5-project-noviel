package com.example.tristantianle.noviel2;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class DetailFragment extends Fragment {
    String title;
    HashMap<String, String> results;
    int count;
    String movieTitle = "original_title";
    String movieDate = "release_date";
    String movieOverview = "overview";
    String moviePoster = "poster_path";
    String movieRating = "vote_average";

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailActivity activity = (DetailActivity) getActivity();
        String position = Integer.toString(activity.getIntData());
        DataProcessing2 dp = new DataProcessing2();
        dp.execute(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public class DataProcessing2 extends AsyncTask<String, Void, HashMap<String,String>> {

        private final String LOG_TAG = DataProcessing2.class.getSimpleName();

        private void getBookDataFromJson(JSONObject titleJsonStr) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TITLE = "title";
            final String AUTHOR = "author";
            final String DESC = "description";

            JSONObject bookJson = titleJsonStr;
            title = bookJson.getString(TITLE);
            String author = bookJson.getString(AUTHOR);
            String desc = bookJson.getString(DESC);
            results = new HashMap<String,String>();
            results.put(TITLE,title);
            results.put(AUTHOR,author);
            results.put(DESC,desc);

//            return results;

        }

        private void getMovieDataFromJson(JSONArray movieArr, int count) throws JSONException {
            if (count>10) {
                count = 10;
            }
            movieTitle = "original_title";
            movieDate = "release_date";
            movieOverview = "overview";
            moviePoster = "poster_path";
            movieRating = "vote_average";
            for (int i=0;i<count;i++) {
                JSONObject mo = movieArr.getJSONObject(i);
                results.put(movieTitle+i,mo.getString(movieTitle));
                results.put(movieDate+i,mo.getString(movieDate));
                results.put(movieOverview+i,mo.getString(movieOverview));
                results.put(moviePoster+i,mo.getString(moviePoster));
                results.put(movieRating+i,mo.getString(movieRating));
            }

        }
        @Override
        protected HashMap<String,String> doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String titleJsonStr = null;

            try {
                DetailActivity activity = (DetailActivity) getActivity();
                String message = activity.getStringData();
                Log.d(LOG_TAG, "url =" + message);
                // print out the query in debug mode
                URL url = new URL(message);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                titleJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                JSONObject jo = new JSONObject(titleJsonStr);
                jo = jo.getJSONArray("results").getJSONObject(Integer.parseInt(params[0]));
//                Log.d(LOG_TAG, "results at pos " + jo.toString());
                // need to change this!!
                getBookDataFromJson(jo);
//                return ;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // Query the movie data
            urlConnection = null;
            reader = null;
            String movieJsonStr = null;

            try {
                final String NYTIMES_BASE_URL = "http://api.themoviedb.org/3/search/movie/?";
                final String TITLE_PARAM = "query";
                final String APPKEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(NYTIMES_BASE_URL).buildUpon()
                        .appendQueryParameter(TITLE_PARAM, title)
                        .appendQueryParameter(APPKEY_PARAM, "001da01ae11dcca44743bde0ad8301cd")
                        .build();

                URL url = new URL(builtUri.toString());

//                URL url = new URL("http://api.themoviedb.org/3/search/movie?query=HARRY+POTTER&api_key=001da01ae11dcca44743bde0ad8301cd");
                Log.d(LOG_TAG, "movie url = " + (url.toString()).equals("http://api.themoviedb.org/3/search/movie?query=HARRY%20POTTER&api_key=001da01ae11dcca44743bde0ad8301cd"));
                Log.d(LOG_TAG, "https://api.themoviedb.org/3/search/movie?query=HARRY%20POTTER&api_key=001da01ae11dcca44743bde0ad8301cd");
                Log.d(LOG_TAG, ""+url);
//                Log.d(LOG_TAG, "movie url = " + url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return results;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
//                Log.d(LOG_TAG, "reader: " + reader.readLine());
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    Log.d(LOG_TAG, "buffer is empty");

                    return results;
                }
                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                JSONObject jo = new JSONObject(movieJsonStr);
                count = jo.getInt("total_results");
                Log.d(LOG_TAG, "onBackGround: " + jo.toString());

                JSONArray joa = jo.getJSONArray("results");
                if (count == 0) {
                    return results;
                }
                getMovieDataFromJson(joa,count);
                return results;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String,String> result) {
            for (String re:result.keySet()) {
                Log.d(LOG_TAG, "hashmap: " + re);
            }
            if (result != null) {
                TextView titleBlock = (TextView) getActivity().findViewById(R.id.book_title);
                titleBlock.append(result.get("title"));
                TextView authorBlock = (TextView) getActivity().findViewById(R.id.book_author);
                authorBlock.append("By " + result.get("author"));
                TextView descBlock = (TextView) getActivity().findViewById(R.id.book_description);
                descBlock.append("Description: " + result.get("description"));
            }
            Log.d(LOG_TAG, "onPostExecute: " + count);

            if (count!=0) {
                if (count>10) {
                    count = 10;
                }
                Log.d(LOG_TAG, "onPostExecute: " + count);
                for (int i=0;i<count;i++) {
                    String mt = "movie_title"+i;
                    String md = "movie_date"+i;
                    String mo = "movie_overview"+i;
                    String mr = "movie_rate"+i;
                    int resID1 = getResources().getIdentifier(mt, "id", "com.example.tristantianle.noviel2");
                    TextView titleBlock = (TextView) getActivity().findViewById(resID1);
                    titleBlock.append("Movie Name: " + result.get(movieTitle+i));
                    Log.d(LOG_TAG, "mvoie titel: " + movieTitle+i);
                    int resID2 = getResources().getIdentifier(md, "id", "com.example.tristantianle.noviel2");
                    TextView authorBlock = (TextView) getActivity().findViewById(resID2);
                    authorBlock.append("Release Date: " + result.get(movieDate+i));
                    int resID3 = getResources().getIdentifier(mo, "id", "com.example.tristantianle.noviel2");
                    TextView descBlock = (TextView) getActivity().findViewById(resID3);
                    descBlock.append("Overview: " + result.get(movieOverview+i));
                    int resID4 = getResources().getIdentifier(mr, "id", "com.example.tristantianle.noviel2");
                    TextView rateBlock = (TextView) getActivity().findViewById(resID4);
                    rateBlock.append("Movie Rating: " + result.get(movieRating+i));
                }
            }
        }
    }
}
