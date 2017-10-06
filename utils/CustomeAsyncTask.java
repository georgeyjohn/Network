package com.ip.barcodescanner.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by deepak on 4/8/15.
 */
public class CustomeAsyncTask extends AsyncTask<Void, Void, Void> {

    private ProgressDialog pd;
    private Context context;

    public CustomeAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(context);
        pd.setMessage("Processing...");
        pd.show();
        preExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        executeInBackground();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        postExecute();
        if (pd.isShowing()) {
            pd.dismiss();
        }
    }

    public void executeInBackground() {

    }

    public void postExecute() {

    }

    public void preExecute() {

    }
}