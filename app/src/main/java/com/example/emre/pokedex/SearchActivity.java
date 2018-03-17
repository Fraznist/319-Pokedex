package com.example.emre.pokedex;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.json.JSONObject;

import java.io.File;

public class SearchActivity extends AppCompatActivity {

    // Constants
    private final String POKEAPI_URL = "http://pokeapi.co/api/v2/pokemon/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initImageLoader();      // Group of functions to set up Android Universal Image Loader

        // Fast Android Networking src: https://github.com/amitshekhariitbhu/Fast-Android-Networking
        // Library to make http requests, used only for get requests in this project.
        AndroidNetworking.initialize(getApplicationContext());

        final EditText searchField = findViewById(R.id.searchET);

        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String pokemon = searchField.getText().toString();  // Get name or id from textField
                getDataFromAPI(pokemon);    // Request the string from pokeapi.co

                return false;
            }
        });
    }

    private void getDataFromAPI(String name) {
        String fullURL = POKEAPI_URL + name.toLowerCase();  // Construct full URL

        // Try to get a lengthy JSON object as an answer from pokeapi
        // The HTTP get request is made in this activity rather than DisplayActivity
        // because getting an answer takes a couple seconds, and I preferred to stay at
        // this activity, rather than switching activites and showing an empty screen until an answer arrives.
        AndroidNetworking.get(fullURL).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("pokedex", "Success! ");
                PokemonData pokemonData = new PokemonData(response);   // Construct object from JSON

                // Send the object to the DisplayActivity
                Intent intent = new Intent(SearchActivity.this, DisplayActivity.class);
                intent.putExtra("pokemon", pokemonData);
                startActivity(intent);
            }

            @Override
            public void onError(ANError anError) {
                Log.e("pokedex", "Failed: " + anError.getErrorBody());
                Toast.makeText(SearchActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Android Universal Image Loader src: https://github.com/nostra13/Android-Universal-Image-Loader
    // Used in this project to download images at runtime,
    // Having local drawables for 800+ pokemon was a hassle.
    // This function initializes the singleton ImageLoader object, which should persist throughout the apps lifetime.
    // The functions inside are copied from an example snippet in the git page of the library.
    private void initImageLoader() {
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800)
                .build();

        ImageLoader.getInstance().init(config);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final EditText searchField = findViewById(R.id.searchET);   // Simply clear the textField
        searchField.setText("");
    }
}
