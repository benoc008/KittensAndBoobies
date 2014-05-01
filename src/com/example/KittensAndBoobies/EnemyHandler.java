package com.example.KittensAndBoobies;



import com.example.KittensAndBoobies.Objects.GameObject;
import com.example.KittensAndBoobies.Objects.Square;

import java.util.ArrayList;
import java.util.Collections;
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

            if (temp[1] > 1.0f + s.getScale()[1]/2 && s.getLife() < 0) {
                s.onDeath(renderer.getPlayer(), gs);
                toRemove.add(s);
                //addNew(toAdd, new Square());      //let the GameScheduler handle this
            }

            if(s.getLife() > 0){
                s.setLife(s.getLife() - 1);
            }

            if(s.getLife() == 0){
                s.onDeath(renderer.getPlayer(), gs);
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
    public GameObject getMePosition(GameObject curr){
        //Creating a temporary list with the potential objects
        List<GameObject> coll = new ArrayList<GameObject>();
        coll.addAll(toAdd);
        for(GameObject o : enemies){
            if(o.getPosition()[1] < -0.5){
                coll.add(o);
            }
        }
        Collections.sort(enemies, new Square());            //maybe we should create an own comparator class for this

        boolean success = false;
        GameObject newtry = curr.clone();

        for (int i = 0; i < 10; i++) {                      //try it 10 times, then return null
            newtry.setPosition(renderer.Spawn());
            if(checkOne(coll, newtry)){
                success = true;
                break;
            }
        }

        if(!success){
            return null;
        } else {
            return newtry;
        }
    }

    public boolean checkOne(List<GameObject> coll, GameObject curr){
        List<GameObject> temp = new ArrayList<GameObject>();
        temp.addAll(coll);
        temp.add(curr);
        Collections.sort(temp, new Square());                       //too much sorting
                                                                    //TODO maybe we should use TREESET....
        boolean result = false;
        float prev = -renderer.getRatio();

        // first check the last element, next to the wall, if it fits, we can skip the iteration
        if(temp.get(temp.size() - 1).getPosition()[0] >
                renderer.getRatio() - enemies.get(enemies.size() - 1).getScale()[0]/2f - renderer.getPlayer().getScale()[0] - 0.05f) {
            // otherwise check the gaps
            // actually checking the new object and its neighbours would be enough
            for (GameObject o : temp) {
                if (o.getPosition()[0] - o.getScale()[0] / 2f > prev + o.getScale()[0] / 2f + renderer.getPlayer().getScale()[0] + 0.05f) {
                    result = true;
                    break;
                }
                prev = o.getPosition()[0];
            }
        } else {
            result = true;
        }
        return result;
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
        GameObject newpos = getMePosition(newborn);
        if(newpos != null){
            list.add(newpos);
        }
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
