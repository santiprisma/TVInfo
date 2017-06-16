package deveando.net.tvinfo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import deveando.net.tvinfo.model.Movie;
import deveando.net.tvinfo.sql.MovieDB;
import deveando.net.tvinfo.util.Constants;

/**
 * Created by Santiago on 16/06/2017.
 */

public class MovieActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        if (getIntent().getSerializableExtra("movieInfo") != null) {
            final Movie movie = (Movie) getIntent().getSerializableExtra("movieInfo");
            final SQLiteDatabase db = new MovieDB(this).getReadableDatabase();
            boolean enFavorito = false;

            TextView textTitle = (TextView) findViewById(R.id.textTitle);
            textTitle.setText(movie.getTitle());

            TextView textRelease = (TextView) findViewById(R.id.textRelease);
            textRelease.setText(movie.getReleaseDate());

            TextView textDescription = (TextView) findViewById(R.id.textDescription);
            textDescription.setText(movie.getOverview());

            Button buttonCompartir = (Button) findViewById(R.id.buttonShare);
            buttonCompartir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("sms:1234"));  // This ensures only SMS apps respond
                    intent.putExtra("sms_body", "Te recomiendo la película: "+ movie.getTitle());
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });

            Button buttonFavorito = (Button) findViewById(R.id.buttonFavorito);
            buttonFavorito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues values = new ContentValues();
                    values.put("id", movie.getId());
                    values.put("name", movie.getTitle());
                    db.insert(Constants.TABLE_NAME, null, values);

                    Button buttonFavorito = (Button) findViewById(R.id.buttonFavorito);
                    Button buttonFavoritoRemove = (Button) findViewById(R.id.buttonFavoritoRemove);
                    buttonFavorito.setVisibility(View.GONE);
                    buttonFavoritoRemove.setVisibility(View.VISIBLE);
                }
            });

            Button buttonFavoritoRemove = (Button) findViewById(R.id.buttonFavoritoRemove);
            buttonFavoritoRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String selection = "id = ?";
                    String[] selectionArgs = { String.valueOf(movie.getId()) };
                    db.delete(Constants.TABLE_NAME, selection, selectionArgs);

                    Button buttonFavorito = (Button) findViewById(R.id.buttonFavorito);
                    Button buttonFavoritoRemove = (Button) findViewById(R.id.buttonFavoritoRemove);
                    buttonFavorito.setVisibility(View.VISIBLE);
                    buttonFavoritoRemove.setVisibility(View.GONE);
                }
            });

            Cursor cursor = db.query(Constants.TABLE_NAME, new String[] { "name" }, "id = " + movie.getId(), null, null, null, null);
            while (cursor.moveToNext()) {
                enFavorito = true;
                break;
            }

            if (enFavorito) {
                buttonFavorito.setVisibility(View.GONE);
                buttonFavoritoRemove.setVisibility(View.VISIBLE);
            } else {
                buttonFavorito.setVisibility(View.VISIBLE);
                buttonFavoritoRemove.setVisibility(View.GONE);
            }

        } else {
            finish();
        }
    }
}
