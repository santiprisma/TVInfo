package deveando.net.tvinfo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final int CURRENT_POSITION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 1);
        }

        if (prefs.getInt("main_activity", -1) > 0) {
            if (prefs.getBoolean("start_activity", false) == true) {
                Intent intent;
                switch (prefs.getInt("main_activity", -1)) {
                    case 1:
                        intent = new Intent(this, PopularesActivity.class);
                        break;

                    case 2:
                        intent = new Intent(this, DramaActivity.class);
                        break;

                    case 3:
                        intent = new Intent(this, AccionActivity.class);
                        break;

                    case 4:
                        intent = new Intent(this, InfantilActivity.class);
                        break;

                    case 5:
                        intent = new Intent(this, RegionalActivity.class);
                        break;

                    case 6:
                        intent = new Intent(this, SettingsActivity.class);
                        break;

                    default:
                        intent = new Intent(this, PopularesActivity.class);
                }

                startActivity(intent);
                finish();
            }
        }

        setContentView(R.layout.activity_main);

        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                CURRENT_POSITION);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
    }
}
