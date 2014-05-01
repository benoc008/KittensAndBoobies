package com.example.KittensAndBoobies;

import android.opengl.Matrix;
import com.example.KittensAndBoobies.Database.DataSource;
import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * Created by benoc on 19/04/2014.
 */
public class GameActivity extends Activity {

    private GLSurfaceView mGLView;
    public final static String EXTRA_MESSAGE = "com.example.KittensAndBoobies.MESSAGE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }

    public void gameOver(int points, long start, long end){
        //get the intent && start the activity
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra(EXTRA_MESSAGE, " " + points);
        startActivity(intent);

        // save the scores
        DataSource datasource = new DataSource(this);
        datasource.open();
        datasource.createRecord(points, start, end-start);
        datasource.close();

        finish();
    }

    @Override
    public void onBackPressed() {
        mGLView.onPause();
    }
}
