package com.example.emre.pokedex;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Had to implement Parcelable, in order to make the object shareable via intents
public class PokemonData implements Parcelable {

    private String name;
    private int id;
    private String type1 = "";
    private String type2 = "";
    private String spriteURL;

    // Dummy constructor
    public PokemonData(JSONObject json) {
        try {
            String tempName = json.getString("name");
            this.name = Character.toUpperCase(tempName.charAt(0)) + tempName.substring(1);
            this.id = json.getInt("id");
            JSONArray typeArray = json.getJSONArray("types");
            if (typeArray.length() == 1)
                this.type1 = typeArray.getJSONObject(0).getJSONObject("type").getString("name");
            else {
                this.type1 = typeArray.getJSONObject(0).getJSONObject("type").getString("name");
                this.type2 = typeArray.getJSONObject(1).getJSONObject("type").getString("name");
            }
            this.spriteURL = json.getJSONObject("sprites").getString("front_default");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Required constructor for Parcelable interface, never really used in source code.
    public PokemonData(Parcel in) {
        name = in.readString();
        id = in.readInt();
        type1 = in.readString();
        type2 = in.readString();
        spriteURL = in.readString();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getType1() {
        return type1;
    }

    public String getType2() {
        return type2;
    }

    public String getSpriteURL() {
        return spriteURL;
    }

    // Dummy implementations for Parcelable interface
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(id);
        parcel.writeString(type1);
        parcel.writeString(type2);
        parcel.writeString(spriteURL);
    }

    public static final Parcelable.Creator<PokemonData> CREATOR = new Parcelable.Creator<PokemonData>() {

        public PokemonData createFromParcel(Parcel in) {
            return new PokemonData(in);
        }

        public PokemonData[] newArray(int size) {
            return new PokemonData[size];
        }
    };
}
