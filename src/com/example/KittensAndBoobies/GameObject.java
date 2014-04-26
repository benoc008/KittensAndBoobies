package com.example.KittensAndBoobies;

/**
 * Created by benoc on 27/04/2014.
 * All kinds of enemies and buffs and stuff must extend from this
 */
public abstract class GameObject {
    private float color[] = { 0.1f, 0.1f, 0.1f, 1.0f };
    private float position[] = {0f, 0f, 0f};
    private float scale[] = {0.2f, 0.2f, 0.2f};
    private float speed = 0.01f;


    public abstract Square clone();

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
}
