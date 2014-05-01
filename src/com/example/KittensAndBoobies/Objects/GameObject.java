package com.example.KittensAndBoobies.Objects;

import com.example.KittensAndBoobies.GameScheduler;

import java.util.Comparator;

/**
 * Created by benoc on 27/04/2014.
 * All kinds of enemies and buffs and stuff must extend from this
 */
public abstract class GameObject implements Comparator<GameObject> {
    private float color[] = { 0.1f, 0.1f, 0.1f, 1.0f };
    private float position[] = {0f, 0f, 0f};
    private float scale[] = {0.2f, 0.2f, 0.2f};
    private float speed = 0.01f;
    private int life = -1;          //means disabled


    public abstract GameObject clone();

    public abstract void onCollision(GameObject player, GameScheduler gs);

    public void onDeath(GameObject player, GameScheduler gs) {

    }

    public abstract void draw(float[] m);

    @Override
    public int compare(GameObject a, GameObject b){
        return (a.getPosition()[0] < b.getPosition()[0])? -1
                : (a.getPosition()[0] > b.getPosition()[0])? 1
                : 0;
    }

    public void setPosition(float position[]){
        this.position = position;
    }

    public float[] getPosition(){
        return position;
    }

    public void setColor(float[] color){
        this.color = color;
    }

    public float[] getColor(){
        return this.color;
    }

    public float[] getScale() {
        return scale;
    }

    public void setScale(float scale[]) {
        this.scale = scale;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }
}
