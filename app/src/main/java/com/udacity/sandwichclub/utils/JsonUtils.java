package com.udacity.sandwichclub.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String TAG = "JsonUtils";

    private JsonUtils() {
    }

    public static Sandwich parseSandwichJson(@Nullable final String jsonString) {
        if (jsonString == null) return null;

        Sandwich sandwich = null;
        try {
            final JSONObject sandwichObject = new JSONObject(jsonString);
            final JSONObject nameObject = sandwichObject.getJSONObject(Sandwich.TAG_NAME);

            sandwich = new Sandwich(nameObject.getString(Sandwich.KEY_MAIN_NAME),
                parseArrayList(nameObject.getJSONArray(Sandwich.KEY_ALSO_KNOWN_AS)),
                sandwichObject.getString(Sandwich.KEY_PLACE_OF_ORIGIN),
                sandwichObject.getString(Sandwich.KEY_DESCRIPTION),
                sandwichObject.getString(Sandwich.KEY_IMAGE),
                parseArrayList(sandwichObject.getJSONArray(Sandwich.KEY_INGREDIENTS)));

        } catch (final JSONException e) {
            Log.d(TAG, "Error on Parsing Sandwich Object : " + e);
        }

        return sandwich;
    }

    private static List<String> parseArrayList(final JSONArray jsonArray) throws JSONException {
        final List<String> alsoKnownAsList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            alsoKnownAsList.add(jsonArray.getString(i));
        }
        return alsoKnownAsList;
    }
}
