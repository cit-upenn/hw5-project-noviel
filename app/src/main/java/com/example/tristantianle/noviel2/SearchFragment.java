package com.example.tristantianle.noviel2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TristanTianle on 12/7/2015.
 */

public class SearchFragment extends Fragment {
    private String url;
    private ArrayAdapter<String> mForecastAdapter;
    JSONObject bookJson;
    FetchWeatherTask weatherTask;

    private final String LOG_TAG = SearchFragment.class.getSimpleName();


    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.

        setHasOptionsMenu(true);
        weatherTask = new FetchWeatherTask();
//        SearchList activity = (SearchList) getActivity();
//        String message = activity.getMyData();
        String message = "Harry Potter";
        weatherTask.execute(message);
        url = weatherTask.getURL();
        Log.d(LOG_TAG, "return url: " + url);
        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_result, // The name of the layout ID.
                        R.id.list_item_forecast_textview, // The ID of the textview to populate.
                        weekForecast);

        View rootView = inflater.inflate(R.layout.fragment_search_list, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_list, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {
//                "Mon 6/23 - Sunny - 31/17",
//                "Tue 6/24 - Foggy - 21/8",
//                "Wed 6/25 - Cloudy - 22/17",
//                "Thurs 6/26 - Rainy - 18/11",
//                "Fri 6/27 - Foggy - 21/10",
//                "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
//                "Sun 6/29 - Sunny - 20/7"
        };
        List<String> weekForecast = new ArrayList<String>(Arrays.asList(data));

        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_result, // The name of the layout ID.
                        R.id.list_item_forecast_textview, // The ID of the textview to populate.
                        weekForecast);

        View rootView = inflater.inflate(R.layout.fragment_search_list, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String urlStr = url;
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, url);
                Log.d(LOG_TAG, "Pass in url: " + url);
                startActivity(intent);
//                Intent intent = new Intent(getActivity(), DetailActivity.class)
//                        .putExtra(Intent.EXTRA_POS, position);
//                intent.putExtra(Intent.EXTRA_URL, url);
//                startActivity(intent);
            }
        });

        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        public URL url;

        public String getURL() {
            Log.d(LOG_TAG, "getURL: "+ url);
            return url.toString();
        }

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


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
//                final String FORECAST_BASE_URL =
//                        "http://api.openweathermap.org/data/2.5/forecast/daily?";
//                final String QUERY_PARAM = "q";
//                final String FORMAT_PARAM = "mode";
//                final String UNITS_PARAM = "units";
//                final String DAYS_PARAM = "cnt";
//                final String APPID_PARAM = "APPID";
//
//                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
//                        .appendQueryParameter(QUERY_PARAM, params[0])
//                        .appendQueryParameter(FORMAT_PARAM, format)
//                        .appendQueryParameter(UNITS_PARAM, units)
//                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
//                        .appendQueryParameter(APPID_PARAM, "2e33e361aa2042dc70879814286cef25")
//                        .build();

                final String NYTIMES_BASE_URL = "http://api.nytimes.com/svc/books/v2/lists/best-sellers/history.json?";
                final String TITLE_PARAM = "title";
                final String APPKEY_PARAM = "api-key";

                Uri builtUri = Uri.parse(NYTIMES_BASE_URL).buildUpon()
                        .appendQueryParameter(TITLE_PARAM, params[0])
                        .appendQueryParameter(APPKEY_PARAM, "de340fc0244bbb1503a07c665b9903dc:15:65808863")
                        .build();

                url = new URL(builtUri.toString());

                // print out the query in debug mode
                Log.d(LOG_TAG, "url =" + url);

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
