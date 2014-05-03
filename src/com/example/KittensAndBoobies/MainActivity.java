package com.example.KittensAndBoobies;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;
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

        //music setup
        SharedPreferences spm = getSharedPreferences("MUSIC", MODE_WORLD_READABLE);
        Boolean isFirstRunMusic = sp.getBoolean("firstRun",true);
        if(isFirstRunMusic){
            SharedPreferences.Editor editor = spm.edit();
            editor.putBoolean("firstRun", false);
            editor.putBoolean("Music", false);
            editor.commit();
        }
    }

    public void musicClick(View view){
        boolean on = ((ToggleButton) view).isChecked();

        SharedPreferences spm = getSharedPreferences("MUSIC", MODE_WORLD_READABLE);
        SharedPreferences.Editor editor = spm.edit();
        if (on) {
            editor.putBoolean("Music", true);
        } else {
            editor.putBoolean("Music", false);
        }
        editor.commit();
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
