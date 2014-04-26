package com.example.KittensAndBoobies;



import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EnemyHandler implements Runnable{

    private static final String TAG = "EnemyHandler";

    private boolean running;
    private int level;
    private int timer;
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

        enemies = new ArrayList<Square>();
        toAdd = new ArrayList<Square>();
        toRemove = new ArrayList<Square>();
    }

    public void moveEnemies(){
        for(Square s : enemies) {
            float temp[] = s.getPosition();
            temp[1] -= s.getSpeed();

            if (temp[1] < -1.0f - s.getScale()[1]/2) {                    //  - 1.0 - the half of the size
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

//    public boolean checkFieldHorizontally(Square curr, float newPos[]){
//        for(Square s : enemies){            //iterate through enemies
//            float spos[] = s.getPosition();
//            float sscale[] = s.getScale();
//            float currscale[] = curr.getScale();
//            if(newPos[0] < spos[0]+sscale[0]*width ...){
//
//            }
//        }
//        return false;                       //means it's empty
//    }


    private boolean test = false;
    public boolean checkPlayerField(Square curr, float newPos[]){
        // TODO : get these constants from player class
        //if(newPos[1] - curr.getScale()[1]/2 < -0.8f + 0.2f/2f && newPos[1] + curr.getScale()[1]/2 > -0.8f - 0.2f/2f){
        if(     // the collision detection
                newPos[1] - curr.getScale()[1]/2f < renderer.getPlayer().getPosition()[1] + renderer.getPlayer().getScale()[1]/2f &&
                newPos[1] + curr.getScale()[1]/2f > renderer.getPlayer().getPosition()[1] - renderer.getPlayer().getScale()[1]/2f &&
                newPos[0] + curr.getScale()[0]/2f > renderer.getPlayer().getPosition()[0] - renderer.getPlayer().getScale()[0]/2f &&
                newPos[0] - curr.getScale()[0]/2f < renderer.getPlayer().getPosition()[0] + renderer.getPlayer().getScale()[0]/2f)
        {

            Log.i(TAG, "mellete");
            test = true;
            running = false;
        } else {
//            if (test)
//                running = false;
        }
        return true;                       //means it's empty
    }

    public void collision(){
        Log.i(TAG, "EnemyHandler: Boobies won!");
    }

    public void addNew(List<Square> list){
        Square newborn = new Square();
        float pos[] = renderer.Spawn();
        pos[1] = 1f;
        newborn.setPosition(pos);
        float color[] = { 0.2f, 0.898039216f, 0.709803922f, 1.0f };
        newborn.setColor(color);
        list.add(newborn);
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
                    //addNew(toAdd); TODO visszatenni
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
