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

        pokeName = findViewById(R.id.pokename);
        pokeSprite = findViewById(R.id.pokeSprite);
        pokeID = findViewById(R.id.id);
        type1 = findViewById(R.id.type1);
        type2 = findViewById(R.id.type2);

        Intent intent = getIntent();
        PokemonData pokemonData = intent.getParcelableExtra("pokemon");
        updateUI(pokemonData);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(DisplayActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    private void updateUI(PokemonData pokemonData) {
        ImageLoader imageLoader = ImageLoader.getInstance();

        pokeName.setText(pokemonData.getName());
        pokeID.setText(pokemonData.getId() + "");

        int resourceID = getResources().getIdentifier(pokemonData.getType1(), "drawable", getPackageName());
        type1.setImageResource(resourceID);
        resourceID = getResources().getIdentifier(pokemonData.getType2(), "drawable", getPackageName());
        type2.setImageResource(resourceID);

        String SpriteURL = pokemonData.getSpriteURL();
        imageLoader.displayImage(SpriteURL, pokeSprite);
    }
}
