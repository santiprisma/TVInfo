package deveando.net.tvinfo.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.IOException;

import deveando.net.tvinfo.iface.MovieIFace;
import deveando.net.tvinfo.util.Constants;
import deveando.net.tvinfo.util.JSONUtil;

/**
 * Created by Santiago on 16/06/2017.
 */

public class MovieTask extends AsyncTask<String, Void, JSONObject> {
    protected MovieIFace activity = null;
    protected ProgressDialog dialog = null;

    public MovieTask(MovieIFace activity) {
        this.activity = activity;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            return JSONUtil.getObjectFromURL(Constants.SERVICE_URL + params[0] + "&api_key=" + Constants.API_KEY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show((Context) activity, "Procesando", "Espere por favor");
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        dialog.dismiss();
        activity.movieResponse(json);
    }
}

