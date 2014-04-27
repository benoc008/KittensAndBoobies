package com.example.KittensAndBoobies;

import android.content.Context;
import android.graphics.Point;
import android.nfc.Tag;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;

/**
 * Created by benoc on 19/04/2014.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final myRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new myRenderer();
        setRenderer(mRenderer);
        mRenderer.setActivity((GameActivity)context);

        // Render the view only when there is a change in the drawing data
       // setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                //float amount = ((x) * (float)(1f/size.x) -0.5f) * -1;

                mRenderer.setTrans(x);  // = 180.0f / 320
                requestRender();
        }

        return true;
    }

    @Override
    public void onPause(){
        super.onPause();
        mRenderer.stopGame();
    }
}
