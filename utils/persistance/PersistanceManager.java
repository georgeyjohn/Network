package com.ip.barcodescanner.utils.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import com.ip.barcodescanner.utils.JSONHelper;


public class PersistanceManager {

    public static final String PREFS_NAME = "ip_barcode_persistance_file";

    private SharedPreferences sPref;
    private SharedPreferences.Editor editor;

    private Context context;

    public PersistanceManager(Context cxt) {
        context = cxt;
        sPref = context.getSharedPreferences(PREFS_NAME, 0);
        editor = sPref.edit();
    }

    public Object getValue(String key, Class<?> type) {
        return JSONHelper.Deserialize(sPref.getString(key, null), type);
    }

    public String getValue(String key) {
        return sPref.getString(key, null);
    }

    public void setValue(String key, String name) {
        editor.putString(key, name);
        editor.commit();
    }

}
