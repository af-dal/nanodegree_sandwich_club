package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private static final String DECIMAL_POINT = ", ";

    private ImageView ingredientsIv;
    private TextView originTv;
    private TextView alsoKnowTv;
    private TextView ingredientsTv;
    private TextView descriptionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ingredientsIv = findViewById(R.id.image_iv);
        originTv = findViewById(R.id.origin_tv);
        alsoKnowTv = findViewById(R.id.also_known_tv);
        ingredientsTv = findViewById(R.id.ingredients_tv);
        descriptionTv = findViewById(R.id.description_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        final int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        final String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        final String json = sandwiches[position];
        final Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(@NonNull final Sandwich sandwich) {
        Picasso.with(this)
            .load(sandwich.getImage())
            .into(ingredientsIv);

        originTv.setText(getStringOrEmptyValueState(sandwich.getPlaceOfOrigin()));
        descriptionTv.setText(getStringOrEmptyValueState(sandwich.getDescription()));
        alsoKnowTv.setText(splitListToString(sandwich.getAlsoKnownAs()));
        ingredientsTv.setText(splitListToString(sandwich.getIngredients()));
    }

    @NonNull
    private String splitListToString(final List<String> alsoKnowAsList) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (final String alsoKnowAsString : alsoKnowAsList) {
            stringBuilder.append(alsoKnowAsString);

            if (alsoKnowAsList.indexOf(alsoKnowAsString) < alsoKnowAsList.size() - 1) {
                stringBuilder.append(DECIMAL_POINT);
            }
        }

        return getStringOrEmptyValueState(stringBuilder.toString());
    }

    @NonNull
    private String getStringOrEmptyValueState(@Nullable final String string) {
        if (!TextUtils.isEmpty(string)) {
            return string;
        } else {
            return getString(R.string.detail_empty_value_state);
        }
    }

}
