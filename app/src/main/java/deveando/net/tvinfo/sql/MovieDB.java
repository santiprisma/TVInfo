package deveando.net.tvinfo.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import deveando.net.tvinfo.util.Constants;

/**
 * Created by Santiago on 16/06/2017.
 */

public class MovieDB extends SQLiteOpenHelper {

    public MovieDB(Context context) {
        super(context,"MOVIE_DB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE "+ Constants.TABLE_NAME +" ( id INTEGER, name VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
