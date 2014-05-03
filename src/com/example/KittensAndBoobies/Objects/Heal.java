package com.example.KittensAndBoobies.Objects;

import android.app.Activity;
import android.opengl.GLES20;
import com.example.KittensAndBoobies.GameScheduler;
import com.example.KittensAndBoobies.R;
import com.example.KittensAndBoobies.myRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by benoc on 27/04/2014.
 */
public class Heal extends GameObject {

    private int resourceId = R.drawable.nutella;

    public Heal(Activity activity){
        super(activity);
        setResourceId(resourceId);
    }

    public Heal clone(){
        Heal temp = new Heal(getActivity());
        temp.setColor(getColor());
        temp.setPosition(getPosition());
        return temp;
    }

    public void onCollision(GameObject player, GameScheduler gs){
        if(getLife() == -1){
            setLife(25);
        }
        if(player.getLife() < 100){
            player.setLife(player.getLife() + 1);
//            float color[] = player.getColor();
//            color[0] -= 0.01f;
//            player.setColor(color);
        }
        gs.setPoints(gs.getPoints()+gs.getLevel());
    }
}
