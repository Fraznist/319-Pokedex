package com.example.emre.pokedex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class DisplayActivity extends AppCompatActivity {

    // UI Members
    TextView pokeName;
    ImageView pokeSprite;
    TextView pokeID;
    ImageView type1;
    ImageView type2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        // Cache UI members
        pokeName = findViewById(R.id.pokename);
        pokeSprite = findViewById(R.id.pokeSprite);
        pokeID = findViewById(R.id.id);
        type1 = findViewById(R.id.type1);
        type2 = findViewById(R.id.type2);

        // Grab PokemonData object from SearchActivity
        Intent intent = getIntent();
        PokemonData pokemonData = intent.getParcelableExtra("pokemon");
        // set UI components according to the received PokemonData
        updateUI(pokemonData);
    }

    @Override
    public void onBackPressed() {   // Simpy go back to SearchActivity
        super.onBackPressed();

        Intent intent = new Intent(DisplayActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    private void updateUI(PokemonData pokemonData) {
        ImageLoader imageLoader = ImageLoader.getInstance();    // Get singleton instance

        pokeName.setText(pokemonData.getName());
        pokeID.setText(pokemonData.getId() + "");

        // Names of drawables representing types match the names of types of pokeapi.co
        int resourceID = getResources().getIdentifier(pokemonData.getType1(), "drawable", getPackageName());
        type1.setImageResource(resourceID);
        resourceID = getResources().getIdentifier(pokemonData.getType2(), "drawable", getPackageName());
        type2.setImageResource(resourceID);

        // pokeapi.co provides several sprites of a pokemon as URL, we use default_front sprite
        String SpriteURL = pokemonData.getSpriteURL();
        // Universal Image Loader function, downloads an image from URL and sets the image of the provided ImageView to it.
        imageLoader.displayImage(SpriteURL, pokeSprite);
    }
}
