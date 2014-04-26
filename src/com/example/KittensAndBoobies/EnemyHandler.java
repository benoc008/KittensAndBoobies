package com.example.KittensAndBoobies;



import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EnemyHandler implements Runnable{

    private static final String TAG = "EnemyHandler";

    private boolean running;
    private int level;
    private int timer;
    private int hit;
    private myRenderer renderer;

    private List<Square> enemies;
    private List<Square> toAdd;
    private List<Square> toRemove;

    private boolean lock = false;

    public EnemyHandler(myRenderer renderer){
        this.renderer = renderer;
        running = true;
        level = 1;
        timer = 0;
        hit = 0;

        enemies = new ArrayList<Square>();
        toAdd = new ArrayList<Square>();
        toRemove = new ArrayList<Square>();
    }

    public void moveEnemies(){
        for(Square s : enemies) {
            float temp[] = s.getPosition();
            temp[1] += s.getSpeed();

            if (temp[1] > 1.0f + s.getScale()[1]/2) {                    //  - 1.0 - the half of the size
                toRemove.add(s);
                addNew(toAdd);
            }

            if(checkPlayerField(s, temp)) {
                s.setPosition(temp);
            } else {
                collision();
            }
        }
    }

    public boolean checkNewField(Square curr){
        for(Square s : enemies){            //iterate through enemies
            if(     // the collision detection with padding, actually the top's check is not needed
                curr.getPosition()[1] - curr.getScale()[1]/2f < s.getPosition()[1] + s.getScale()[1]/2f &&
                curr.getPosition()[1] + curr.getScale()[1]/2f > s.getPosition()[1] - s.getScale()[1]/2f &&
                curr.getPosition()[0] + curr.getScale()[0]/2f > s.getPosition()[0] - s.getScale()[0]/2f &&
                curr.getPosition()[0] - curr.getScale()[0]/2f < s.getPosition()[0] + s.getScale()[0]/2f) {
                return false;
            }
        }
        return true;
    }

    public boolean checkPlayerField(Square curr, float newPos[]){
        if(     // the collision detection
            newPos[1] - curr.getScale()[1]/2f < renderer.getPlayer().getPosition()[1] + renderer.getPlayer().getScale()[1]/2f &&
            newPos[1] + curr.getScale()[1]/2f > renderer.getPlayer().getPosition()[1] - renderer.getPlayer().getScale()[1]/2f &&
            newPos[0] + curr.getScale()[0]/2f > renderer.getPlayer().getPosition()[0] - renderer.getPlayer().getScale()[0]/2f &&
            newPos[0] - curr.getScale()[0]/2f < renderer.getPlayer().getPosition()[0] + renderer.getPlayer().getScale()[0]/2f)
        {
            collision();
        } else {

        }
        return true;                       //means it's empty
    }

    public void collision(){
        if(hit < 100){
            hit++;
            float color[] = renderer.getPlayer().getColor();
            color[0] += 0.01f;
            renderer.getPlayer().setColor(color);
        } else {
            running = false;
        }

        //Log.i(TAG, "EnemyHandler: Boobies won!");
    }

    /**
     * Handles the birth of the field elements
     * It creates the element then try to put it in an empty place. If not succeed try again 10 times, then drop it
     * TODO algorithm needs some optimisation
     * @param list
     */
    public void addNew(List<Square> list){
        Square newborn = new Square();
        int tries = 10;
        while(tries > 0) {
            newborn.setPosition(renderer.Spawn());
            if(checkNewField(newborn)){
                tries = 0;
                float color[] = { 0.2f, 0.898039216f, 0.709803922f, 1.0f };
                newborn.setColor(color);
                list.add(newborn);
            } else {
                tries--;
            }
        }
        return;

    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(running){
            synchronized (this) {
                while (lock) {
                    try {
                        ((Object)this).wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock = true;
                //Log.i(TAG, "pwnd: run");
                moveEnemies();
                if (timer++ > 100) {
                    timer = 0;
                    if (level < 20)
                        level++;
                    addNew(toAdd);
                }
                lock = false;
                ((Object) (this)).notify();
            }
            try{
                Thread.sleep(75 - level * 3);       //the timing needs some optimization
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) { this.running = running; }

    public List<Square> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Square> enemies) {
        this.enemies = enemies;
    }

    public List<Square> getToRemove() {
        return toRemove;
    }

    public List<Square> getToAdd() {
        return toAdd;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

}
