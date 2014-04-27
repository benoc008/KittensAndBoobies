package com.example.KittensAndBoobies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by benoc on 27/04/2014.
 */
public class GameOverActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String message = intent.getStringExtra(GameActivity.EXTRA_MESSAGE);
        setContentView(R.layout.game_over);
        TextView view = (TextView)findViewById(R.id.textView2);
        view.append(message);
    }

    public void startNewGame(View view){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void exit(View view){
        finish();
    }
}
