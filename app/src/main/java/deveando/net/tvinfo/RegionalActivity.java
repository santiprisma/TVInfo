package deveando.net.tvinfo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import deveando.net.tvinfo.iface.MovieIFace;
import deveando.net.tvinfo.model.Movie;
import deveando.net.tvinfo.task.MovieTask;
import deveando.net.tvinfo.util.Constants;

/**
 * Created by Santiago on 16/06/2017.
 */

public class RegionalActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks, MovieIFace, LocationListener {
    private static final int CURRENT_POSITION = 5;
    private List<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genero);

        /* Drawer */
        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                CURRENT_POSITION);
        /* Drawer */

        TextView textTitle = (TextView) findViewById(R.id.textGenero);
        textTitle.setText(getResources().getStringArray(R.array.secciones)[CURRENT_POSITION]);

        MovieTask task = new MovieTask(this);
        task.execute("sort_by=popularity.desc&with_genres=16");

        ListView listView = (ListView) findViewById(R.id.listResult);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MovieActivity.class);
                intent.putExtra("movieInfo", movies.get(position));
                startActivity(intent);
            }
        });

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }

    @Override
    public void movieResponse(JSONObject object) {
        List<String> listArray = new ArrayList<String>();

        try {
            JSONArray results = object.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                if (i >= Constants.MAX_RESULTS)
                    break;

                JSONObject current = results.getJSONObject(i);

                Movie movie = new Movie();
                movie.setAdult(current.getBoolean("adult"));
                movie.setId(current.getInt("id"));
                movie.setOriginalLang(current.getString("original_language"));
                movie.setPopularity(current.getLong("popularity"));
                movie.setPosterPath(current.getString("poster_path"));
                movie.setReleaseDate(current.getString("release_date"));
                movie.setOverview(current.getString("overview"));
                movie.setVoteCount(current.getInt("vote_count"));
                movie.setVideo(current.getBoolean("video"));
                movie.setTitle(current.getString("title"));
                movies.add(movie);

                listArray.add(movie.getTitle());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListView listView = (ListView) findViewById(R.id.listResult);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.item_list,
                listArray
        );
        listView.setAdapter(adapter);
    }

    @Override
    public void onLocationChanged(Location location) {
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getCountryCode());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}