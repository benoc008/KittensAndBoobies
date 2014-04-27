package com.example.KittensAndBoobies;



import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EnemyHandler {

    private static final String TAG = "EnemyHandler";

    private myRenderer renderer;
    private GameScheduler gs;

    private List<GameObject> enemies;
    private List<GameObject> toAdd;
    private List<GameObject> toRemove;

    public EnemyHandler(myRenderer renderer, GameScheduler gs){
        this.renderer = renderer;
        this.gs = gs;

        enemies = new ArrayList<GameObject>();
        toAdd = new ArrayList<GameObject>();
        toRemove = new ArrayList<GameObject>();
    }

    public void moveEnemies(){
        for(GameObject s : enemies) {
            float temp[] = s.getPosition();
            temp[1] += s.getSpeed();

            if (temp[1] > 1.0f + s.getScale()[1]/2) {
                toRemove.add(s);
                addNew(toAdd, new Square());
            }

            if(s.getLife() > 0){
                s.setLife(s.getLife() - 1);
            } else if(s.getLife() == 0){
                toRemove.add(s);
            }

            if(checkPlayerField(s, temp)) {
                s.setPosition(temp);
            } else {
                s.onCollision(renderer.getPlayer(), gs);
            }
        }
    }

    /**
     * when a new object is added, this method makes sure that the game won't be impossible
     * we are checking the top 0.5 for enough space horizontally
     * @param curr
     * @return
     */
    public boolean checkNewField(GameObject curr){
        float width = renderer.getRatio()*2;
        float prev = -renderer.getRatio();
        boolean decide = false;
        if(enemies.get(enemies.size() - 1).getPosition()[0] >
                width - enemies.get(enemies.size() - 1).getScale()[0]/2f - renderer.getPlayer().getScale()[0] - 0.05) {
            for (GameObject s : enemies) {            //iterate through enemies
                if (s.getClass() != Heal.class) {
//                if (     // the collision detection with padding, actually the top's check is not needed
//                    curr.getPosition()[1] - curr.getScale()[1] / 2f < s.getPosition()[1] + s.getScale()[1] / 2f &&
//                    curr.getPosition()[1] + curr.getScale()[1] / 2f > s.getPosition()[1] - s.getScale()[1] / 2f &&
//                    curr.getPosition()[0] + curr.getScale()[0] / 2f > s.getPosition()[0] - s.getScale()[0] / 2f &&
//                    curr.getPosition()[0] - curr.getScale()[0] / 2f < s.getPosition()[0] + s.getScale()[0] / 2f) {
//                    return false;
//                }
                    if (s.getPosition()[1] < -0.3f) {
                        if (s.getPosition()[0] > prev + s.getScale()[0]/2f + renderer.getPlayer().getScale()[0] + 0.05f) {
                            decide = true;
                        }
                    }
                    return decide;
                }
            }
        }
        return true;
    }

    public boolean checkPlayerField(GameObject curr, float newPos[]){
        if(     // the collision detection
            newPos[1] - curr.getScale()[1]/2f < renderer.getPlayer().getPosition()[1] + renderer.getPlayer().getScale()[1]/2f &&
            newPos[1] + curr.getScale()[1]/2f > renderer.getPlayer().getPosition()[1] - renderer.getPlayer().getScale()[1]/2f &&
            newPos[0] + curr.getScale()[0]/2f > renderer.getPlayer().getPosition()[0] - renderer.getPlayer().getScale()[0]/2f &&
            newPos[0] - curr.getScale()[0]/2f < renderer.getPlayer().getPosition()[0] + renderer.getPlayer().getScale()[0]/2f)
        {
            return false;
        }
        return true;                       //means it's empty
    }

    /**
     * Handles the birth of the field elements
     * It creates the element then try to put it in an empty place. If not succeed try again 10 times, then drop it
     * TODO algorithm needs some optimisation
     * @param list
     */
    public void addNew(List<GameObject> list, GameObject newborn){
        int tries = 10;
        while(tries > 0) {
            newborn.setPosition(renderer.Spawn());
            if(checkNewField(newborn)){
                tries = 0;
                list.add(newborn);
            } else {
                tries--;
            }
        }
        return;

    }

    public List<GameObject> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<GameObject> enemies) {
        this.enemies = enemies;
    }

    public List<GameObject> getToRemove() {
        return toRemove;
    }

    public List<GameObject> getToAdd() {
        return toAdd;
    }

}
