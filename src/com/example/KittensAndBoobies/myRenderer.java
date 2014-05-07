package com.example.KittensAndBoobies;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import com.example.KittensAndBoobies.Objects.Enemy;
import com.example.KittensAndBoobies.Objects.GameObject;
import com.example.KittensAndBoobies.Objects.Player;
import com.example.KittensAndBoobies.Objects.Square;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by benoc on 19/04/2014.
 */
public class myRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "myRenderer";

    private Player player;
    private EnemyHandler eh;
    private GameScheduler gs;

    private Square lifeBackground;
    private Square life;
    private Square revtime;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private int width = 540;
    private int height = 960;
    private float ratio = 1;

    private long endTime;
    private long dt;
    private long startTime;

    private GameActivity activity;

    private Map<String, Integer> textures;


    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.1f, 0.5f, 0.2f, 1.0f);

        //set up textures
        textures = new HashMap<String, Integer>();
        textures.put("Player", TextureHelper.loadTexture(activity, R.drawable.kitti1));
        textures.put("Enemy", TextureHelper.loadTexture(activity, R.drawable.kasa));
        textures.put("Bomb", TextureHelper.loadTexture(activity, R.drawable.bomb));
        textures.put("Revers", TextureHelper.loadTexture(activity, R.drawable.reverse));
        textures.put("Heal", TextureHelper.loadTexture(activity, R.drawable.nutella));

        player = new Player(textures.get("Player"));

        lifeBackground = new Square();
        float lbgscale[] = {0.5f, 0.04f, 0f};
        lifeBackground.setScale(lbgscale);
        float black[] = {0f, 0f, 0f, 1f};
        lifeBackground.setColor(black);
        float lbgpos[] = {-0.25f, 0.95f, 0f};
        lifeBackground.setPosition(lbgpos);

        life = new Square();
        float lifescale[] = {0.5f, 0.03f, 0f};
        life.setScale(lifescale);
        float red[] = {1f, 0f, 0f, 1f};
        life.setColor(red);
        float lifepos[] = {-0.25f, 0.95f, 0f};
        life.setPosition(lifepos);

        revtime = new Square();
        float revtimescale[] = {0.5f, 0.01f, 0f};
        revtime.setScale(revtimescale);
        float blue[] = {0f, 0f, 1f, 1f};
        revtime.setColor(blue);
        float revtimepos[] = {-0.25f, 0.94f, 0f};
        revtime.setPosition(revtimepos);


        // nasty shit
        gs = new GameScheduler(this);
        eh = new EnemyHandler(this, gs);
        gs.setEh(eh);

        eh.getEnemies().add(new Enemy(textures.get("Enemy")));

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

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);

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

        //draw the life bar's background
        Matrix.setIdentityM(mMVPMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        Matrix.translateM(mMVPMatrix, 0,
                lifeBackground.getPosition()[0],
                lifeBackground.getPosition()[1],
                lifeBackground.getPosition()[2]);
        Matrix.scaleM(mMVPMatrix, 0,
                lifeBackground.getScale()[0],
                lifeBackground.getScale()[1],
                lifeBackground.getScale()[2]);
        lifeBackground.draw(mMVPMatrix);

        //draw the life bar
        Matrix.setIdentityM(mMVPMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        Matrix.translateM(mMVPMatrix, 0, life.getPosition()[0], life.getPosition()[1], life.getPosition()[2]);
        Matrix.scaleM(mMVPMatrix, 0, life.getScale()[0], life.getScale()[1], life.getScale()[2]);
        life.draw(mMVPMatrix);

        //draw the reverse time bar
        Matrix.setIdentityM(mMVPMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        Matrix.translateM(mMVPMatrix, 0, revtime.getPosition()[0], revtime.getPosition()[1], revtime.getPosition()[2]);
        Matrix.scaleM(mMVPMatrix, 0, revtime.getScale()[0], revtime.getScale()[1], revtime.getScale()[2]);
        revtime.draw(mMVPMatrix);
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
                if(eh.getEnemies().size() == 1) { // workaround for empty list --> enemy handler (bomb)
                    eh.addNew(eh.getEnemies(), new Enemy(textures.get("Enemy")));
                }
                eh.getEnemies().remove(s);
            }

            for (GameObject s : eh.getToAdd()) {
                GameObject temp = s.clone();
                eh.getEnemies().add(temp);
            }

            eh.getToRemove().removeAll(eh.getToRemove());
            eh.getToAdd().removeAll(eh.getToAdd());

            //set the lifebar's size and some ugly positioning
            float t[] = life.getScale();
            t[0] = player.getLife()/100f/2f - 0.02f;
            if(t[0]<0)
                t[0] = 0;
            life.setScale(t);
            float lifepos[] = life.getPosition();
            lifepos[0] = -0.01f - t[0]/2f;
            life.setPosition(lifepos);

            //set the revtimebar size
            t = revtime.getScale();
            t[0] = player.getReversed()/100f/2f - 0.02f;
            if(t[0]<0)
                t[0] = 0;
            revtime.setScale(t);
            float revtimepos[] = revtime.getPosition();
            revtimepos[0] = -0.01f - t[0]/2f;
            revtime.setPosition(revtimepos);

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
        pos[0] = calcPos(trans) * ((player.getReversed()>0)?-1:1);
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

    public GameActivity getActivity() {
        return activity;
    }

    public void setActivity(GameActivity activity) {
        this.activity = activity;
    }

    public Map<String, Integer> getTextures() {
        return textures;
    }
}
