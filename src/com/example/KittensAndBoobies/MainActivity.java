package com.example.KittensAndBoobies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.example.KittensAndBoobies.Profile.ProfileActivity;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    private final String PREF_NAME = "MyKitten";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //kitty chooser setup
        SharedPreferences sp = getSharedPreferences(PREF_NAME, MODE_WORLD_READABLE);
        Boolean isFirstRun = sp.getBoolean("firstRun",true);
        if(isFirstRun) {
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("MyKittenId", 0);
            editor.putString("KittenName", "Mr. Meowington");
            editor.putBoolean("firstRun", false);
            editor.commit();
        }
    }

    public void startNewGame(View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void startProfile(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void startHighScores(View view){
        Intent intent = new Intent(this, ScoresActivity.class);
        startActivity(intent);
    }
}
