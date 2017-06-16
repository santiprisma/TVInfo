package deveando.net.tvinfo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Created by Santiago on 16/06/2017.
 */

public class SettingsActivity extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final int CURRENT_POSITION = 6;
    private int spinnerValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        /* Drawer */
        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                CURRENT_POSITION);
        /* Drawer */

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        final Switch switchActivity = (Switch) findViewById(R.id.switchSettings);
        switchActivity.setChecked(prefs.getBoolean("start_activity", false));

        Spinner spinner = (Spinner) findViewById(R.id.spinnerGenero);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.item_spinner,
                getResources().getStringArray(R.array.secciones)
        );
        spinner.setAdapter(adapter);
        spinner.setSelection(prefs.getInt("main_activity", 0));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerValue = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btnGuardar = (Button) findViewById(R.id.buttonGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("main_activity", spinnerValue);
                editor.putBoolean("start_activity", switchActivity.isChecked());
                editor.commit();

                Toast.makeText(getApplicationContext(), getResources().getText(R.string.configuracion_guardada), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

    }
}
