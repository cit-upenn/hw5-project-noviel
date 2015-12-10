package com.example.tristantianle.noviel2;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailFragment extends Fragment {

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DetailActivity activity = (DetailActivity) getActivity();
        String message = activity.getStringData();
        String position = Integer.toString(activity.getIntData());
        DataProcessing dp = new DataProcessing();
        dp.execute(position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    public class DataProcessing extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = DataProcessing.class.getSimpleName();
        private String[] getBookDataFromJson(String titleJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String RESULTS = "results";
            final String TITLE = "title";
            final String AUTHOR = "author";
//            final String OWM_MIN = "min";
//            final String OWM_DESCRIPTION = "main";

            bookJson = new JSONObject(titleJsonStr);
            int numResults = bookJson.getInt("num_results");
            JSONArray bookTitleArray = bookJson.getJSONArray(RESULTS);
            if (numResults > 20) {
                numResults = 20;
            }
            String[] resultStrs = new String[numResults];
            for (int i=0;i<numResults;i++) {
                resultStrs[i] = bookTitleArray.getJSONObject(i).getString(TITLE) + "\nBy ";
                resultStrs[i] += bookTitleArray.getJSONObject(i).getString(AUTHOR);
            }

            return resultStrs;

        }
        @Override
        protected String[] doInBackground(String... params) {

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

//            String format = "json";
//            String units = "metric";
//            int numDays = 7;

            try {
                final String NYTIMES_BASE_URL = "http://api.nytimes.com/svc/books/v2/lists/best-sellers/history.json?";
                final String TITLE_PARAM = "title";
                final String APPKEY_PARAM = "api-key";

                Uri builtUri = Uri.parse(NYTIMES_BASE_URL).buildUpon()
                        .appendQueryParameter(TITLE_PARAM, params[0])
                        .appendQueryParameter(APPKEY_PARAM, "de340fc0244bbb1503a07c665b9903dc:15:65808863")
                        .build();

                URL url = new URL(builtUri.toString());

                // print out the query in debug mode
                Log.d(LOG_TAG, "url =" + builtUri);

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
                return getBookDataFromJson(titleJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                mForecastAdapter.clear();
                for(String bookTitleStr : result) {
                    mForecastAdapter.add(bookTitleStr);
                }
            }
        }
    }

}
