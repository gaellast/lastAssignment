package fr.gaellast.lastassignment.model;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by gaellast on 07/05/2016.
 */
public class ListApp {
    private JSONArray jsonArray;
    private SharedPreferences preferences;


    public ListApp(@NonNull SharedPreferences sharedPreferences) {
        String obj = sharedPreferences.getString("data", null);

        this.preferences = sharedPreferences;
        if (obj == null)
            this.jsonArray = new JSONArray();
        else {
            try {
                jsonArray = new JSONArray(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * sauvegarde les datas dans les SharedPreferences
     */
    public void saveData() {
        String obj;

        if (jsonArray == null || (obj = this.jsonArray.toString()) == null || obj.equals("")) {
            Log.e("save data error", "nothing to save");
            return;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("data");
        editor.putString("data", obj);
        editor.apply();
    }

    /**
     * ajoute un package au app
     */
    public boolean addApp(String _package) {
        if (this.alredyHere(_package))
            return false;
        this.jsonArray.put(_package);
        return true;
    }

    /**
     * cherche si _package est deja present
     */
    public boolean alredyHere(String _package) {
        int     i = 0;

        while (i < jsonArray.length()) {
            try {
                if (jsonArray.get(i).equals(_package))
                    return true;
            } catch (JSONException e) {
                Log.e("error searsh", e.getMessage());
            }
            ++i;
        }
        return false;
    }

    /**
     * return json array
     */
    public final JSONArray getJsonArray() {
        return jsonArray;
    }

    /**
     * remove app with package name @packageName
     */
    public void removeApp(String packageName) {
        int     i = 0;

        while (i < jsonArray.length()) {
            try {
                if (jsonArray.get(i).equals(packageName)) {
                    jsonArray.remove(i);
                    return;
                }
            } catch (JSONException e) {
                Log.e("error remove app", e.getMessage());
            }
            ++i;
        }
    }
}