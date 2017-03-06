package com.example.fady.movienanoapp.Fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.fady.movienanoapp.Adapter.GridAdapter;
import com.example.fady.movienanoapp.BuildConfig;
import com.example.fady.movienanoapp.Model.Movie;
import com.example.fady.movienanoapp.R;

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
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    GridView gridView;
    List<Movie> movieList4;
    GridAdapter gridAdapter;

    @Override
    public void onStart() {
        super.onStart();
        RequestMovies requestMovies = new RequestMovies();
        requestMovies.execute("https://api.themoviedb.org/3/movie/popular");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyClick click = (MyClick) getActivity();
                click.itemSelected(movieList4.get(position));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) root.findViewById(R.id.myGridview);
        movieList4 = new ArrayList<>();
        gridAdapter = new GridAdapter(movieList4, getContext());
        gridView.setAdapter(gridAdapter);
        return root;
    }


    public class RequestMovies extends AsyncTask<String, Void, List<Movie>> {
        HttpURLConnection connection;
        InputStream inputStream;
        BufferedReader reader;
        StringBuffer buffer;

        @Override
        protected List<Movie> doInBackground(String... params) {
            String BaseUrl = params[0];
            String ApiKey = "?api_key=" + BuildConfig.OPEN_Movie_API_KEY;
            String full_url = BaseUrl.concat(ApiKey);
            try {
                URL url = new URL(full_url);
                Response(url);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieList4;
        }

        void Response(URL url) throws IOException, JSONException {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            buffer = new StringBuffer();
            String js = null;
            while ((js = reader.readLine()) != null) {
                buffer.append(js + "\n");
            }
            String json = buffer.toString();
            List_of_movies(json);
            Log.e("hello", json);
        }

        void List_of_movies(String json) throws JSONException {
            Movie movie;
            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject myobject = jsonArray.getJSONObject(i);
                movie = new Movie(myobject.getString("title"), myobject.getString("overview"),
                        myobject.getString("poster_path"), myobject.getString("release_date"), myobject.getString("vote_average"));
                Log.e("hello", movie.getPoster_path());
                movieList4.add(movie);
            }
            Log.e("Hello", String.valueOf(movieList4.size()));
        }


        @Override
        protected void onPostExecute(List<Movie> movieList) {
            super.onPostExecute(movieList);
            movieList4.addAll(movieList);
            gridAdapter.notifyDataSetChanged();
        }
    }

    public interface MyClick {
        void itemSelected(Movie movie);
    }
}
