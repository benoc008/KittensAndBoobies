package com.example.KittensAndBoobies;

import Database.DataSource;
import Database.Record;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by benoc on 28/04/2014.
 */
public class ProfileActivity extends Activity {

    private DataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        datasource = new DataSource(this);
        loadStats();
        loadAchies();
    }

    public void loadStats(){
        datasource.open();
        List<Record> values = datasource.getAllRecords();
        if(values.size() == 0) {
            return;
        }
        long bestScore = values.get(0).getScore();
        long numGames = values.size();
        long sumDuration = 0;
        for(Record r : values){
            sumDuration += r.getDuration();
        }

        TextView name = (TextView) findViewById(R.id.textName);
        name.setText("Mr. Meowington");

        TextView games = (TextView) findViewById(R.id.textGames);
        games.setText("" + numGames);

        TextView score = (TextView) findViewById(R.id.textScore);
        score.setText("" + bestScore);

        //duration
        Date durationDate = new Date(sumDuration - TimeZone.getDefault().getRawOffset());
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        TextView duration = (TextView) findViewById(R.id.textTime);
        duration.setText(formatter.format(durationDate));
    }

    public void loadAchies(){
        
    }

    public void changePicture(View view){

    }
}
