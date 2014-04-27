package com.example.KittensAndBoobies;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by benoc on 19/04/2014.
 * TODO this whole class should be overhauled. it's task is to render, nothing else
 */
public class myRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "myRenderer";

    private Player player;
    private EnemyHandler eh;
    private GameScheduler gs;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private int width = 540;
    private int height = 940;
    private float ratio = 1;

    private long endTime;
    private long dt;
    private long startTime;


    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.1f, 0.5f, 0.2f, 1.0f);

        player = new Player();

        // nasty shit
        gs = new GameScheduler(this);
        eh = new EnemyHandler(this, gs);
        gs.setEh(eh);

        eh.getEnemies().add(new Square());

        for(GameObject s : eh.getEnemies()){
            s.setPosition(Spawn());
        }


        Thread t1 = new Thread(gs);
        t1.start();

        startTime = System.currentTimeMillis();
    }

    public void onDrawFrame(GL10 unused) {
        endTime = System.currentTimeMillis();
        dt = endTime - startTime;
        if (dt < 33)
            try {
                Thread.sleep(33 - dt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        startTime = System.currentTimeMillis();

        calcGame();
        renderGame();
    }

    public void renderGame(){
        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        Matrix.setIdentityM(mMVPMatrix, 0);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //Matrix.setIdentityM(mMVPMatrix, 0); // reset transformations
        //Matrix.setRotateM(mMVPMatrix, 0, 0, 1.0f, 0, 0); // set rotation
        Matrix.translateM(mMVPMatrix, 0, player.getPosition()[0], player.getPosition()[1], player.getPosition()[2]); // apply translation
        Matrix.scaleM(mMVPMatrix, 0, player.getScale()[0], player.getScale()[1], player.getScale()[2]); // apply scale


        player.draw(mMVPMatrix);

        synchronized (eh) {
            while(gs.isLock()){
                try {
                    ((Object)eh).wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gs.setLock(true);
            //Log.i(TAG, "pwnd: renderGame");
            for (GameObject s : eh.getEnemies()) {
                Matrix.setIdentityM(mMVPMatrix, 0);
                Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
                Matrix.translateM(mMVPMatrix, 0, s.getPosition()[0], s.getPosition()[1], s.getPosition()[2]); // apply translation
                //Log.i(TAG,"pos: " + s.getPosition()[0] + " " +  s.getPosition()[1] + " " + s.getPosition()[2] );
                Matrix.scaleM(mMVPMatrix, 0, s.getScale()[0], s.getScale()[0], s.getScale()[0]); // apply scale
                s.draw(mMVPMatrix);
            }
            gs.setLock(false);
            ((Object)eh).notify();
        }
    }

    public void calcGame(){
        synchronized (eh) {
            while(gs.isLock()){
                try {
                    ((Object)eh).wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            gs.setLock(true);
            //Log.i(TAG, "pwnd: calcGame");
            for (GameObject s : eh.getToRemove()) {
                eh.getEnemies().remove(s);
            }

            for (GameObject s : eh.getToAdd()) {
                GameObject temp = s.clone();
                eh.getEnemies().add(temp);
            }

            eh.getToRemove().removeAll(eh.getToRemove());
            eh.getToAdd().removeAll(eh.getToAdd());

            gs.setLock(false);
            ((Object)eh).notify();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

        this.width = width;
        this.height = height;
        Log.i(TAG,"Width: " + width);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public void setTrans(float trans) {
        float pos[] = player.getPosition();
        pos[0] = calcPos(trans);
        player.setPosition(pos);
    }

    public float calcPos(float trans){
        float ret = (trans * (1f/width)*2*ratio -ratio)*-1;
        //Log.i(TAG,"pos: " + ret);

        return ret;
    }

    // ennek az enemyHandlerben kellene lennie....
    public float[] Spawn(){
        //a kepernyo felso felere x, y, z koordinata.
        //float rand[] = {calcPos((float) (Math.random()*width)), calcPos((float) (Math.random()*height/2f)), 0f};
        float rand[] = {calcPos((float) (Math.random()*width)), -1f, 0f};
        return rand;
    }

    public void stopGame(){
        gs.setRunning(false);
    }

    public void resumeGame(){
        gs.setRunning(true);
    }

    public Player getPlayer() {
        return player;
    }

    public float getRatio() {
        return ratio;
    }
}
