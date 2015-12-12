package com.example.tristantianle.noviel2;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailActivity activity = (DetailActivity) getActivity();
//        String message = activity.getStringData();
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

        private HashMap<String,String> getBookDataFromJson(JSONObject titleJsonStr) throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TITLE = "title";
            final String AUTHOR = "author";
            final String DESC = "description";

            JSONObject bookJson = titleJsonStr;
            String title = bookJson.getString(TITLE);
            String author = bookJson.getString(AUTHOR);
            String desc = bookJson.getString(DESC);
            HashMap<String, String> results = new HashMap<String,String>();
            results.put(TITLE,title);
            results.put(AUTHOR,author);
            results.put(DESC,desc);

            return results;

        }
        @Override
        protected HashMap<String,String> doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String titleJsonStr = null;

            try {

                DetailActivity activity = (DetailActivity) getActivity();
                String message = activity.getStringData();
                Log.d(LOG_TAG, "url =" + message);

                URL url = new URL(message);

                // print out the query in debug mode

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                titleJsonStr = buffer.toString();

//                Log.d(LOG_TAG, "query results: \n" + titleJsonStr);

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
                Log.d(LOG_TAG, "results at pos " + jo.toString());
                return getBookDataFromJson(jo);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(HashMap<String,String> result) {
            if (result != null) {
                TextView titleBlock = (TextView) getActivity().findViewById(R.id.book_title);
                titleBlock.append(result.get("title"));
                TextView authorBlock = (TextView) getActivity().findViewById(R.id.book_author);
                authorBlock.append("By " + result.get("author"));
                TextView descBlock = (TextView) getActivity().findViewById(R.id.book_description);
                descBlock.append("Description: " + result.get("description"));
//                Log.d(LOG_TAG, "onPostExecute: " + result.get("title"));
            }
        }
    }

}
