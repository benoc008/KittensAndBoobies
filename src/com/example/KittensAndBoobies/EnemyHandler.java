package com.example.KittensAndBoobies;



import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EnemyHandler {

    private static final String TAG = "EnemyHandler";

    private int hit;
    private myRenderer renderer;
    private GameScheduler gs;

    private List<Square> enemies;
    private List<Square> toAdd;
    private List<Square> toRemove;

    public EnemyHandler(myRenderer renderer, GameScheduler gs){
        this.renderer = renderer;
        this.gs = gs;
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
            gs.setRunning(false);
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

}
