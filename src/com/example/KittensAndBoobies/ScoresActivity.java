package com.example.KittensAndBoobies;

import android.app.ListActivity;
import com.example.KittensAndBoobies.Database.*;
import android.os.Bundle;
import android.widget.ListAdapter;

import java.util.List;

/**
 * Created by benoc on 28/04/2014.
 */
public class ScoresActivity extends ListActivity {

    private DataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores);
        datasource = new DataSource(this);
        datasource.open();
        List<Record> values = datasource.getAllRecords();
//        ArrayAdapter<Record> adapter = new ArrayAdapter<Record>(this,
//                android.R.layout.simple_selectable_list_item, values);
        ListAdapter adapter = new ScoreAdapter(this, values);
        setListAdapter(adapter);
    }
    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }
    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }
}
