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

        initImageLoader();

        AndroidNetworking.initialize(getApplicationContext());

        final EditText searchField = findViewById(R.id.searchET);

        searchField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String pokemon = searchField.getText().toString();
                getDataFromAPI(pokemon);

                return false;
            }
        });
    }

    private void getDataFromAPI(String name) {
        String fullURL = POKEAPI_URL + name.toLowerCase();

        AndroidNetworking.get(fullURL).build().getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("pokedex", "Success! ");
                PokemonData pokemonData = PokemonData.fromJson(response);
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

        final EditText searchField = findViewById(R.id.searchET);
        searchField.setText("");
    }
}
