package com.example.KittensAndBoobies.Objects;

import android.app.Activity;
import com.example.KittensAndBoobies.GameScheduler;
import com.example.KittensAndBoobies.R;

/**
 * Created by benoc on 27/04/2014.
 */
public class Player extends GameObject {

    private int reversed;

    private int resourceId = R.drawable.kitti1;

    public Player(Activity activity) {
        super(activity);
        setResourceId(resourceId);
        float playerPos[] = {0f, 0.8f, 0f};
        setPosition(playerPos);
        float playerScale[] = {0.2f, 0.2f, 0.2f};
        setScale(playerScale);
        setLife(100);
        reversed = 1;
    }

    public Enemy clone(){
        Enemy temp = new Enemy(getActivity());
        temp.setColor(getColor());
        temp.setPosition(getPosition());
        return temp;
    }

    public void onCollision(GameObject player, GameScheduler gs){
        if(player.getLife() > 0){
            player.setLife(player.getLife() - 1);
            // that's not needed anymore, but i leave it here for the case we want to put in some blood
//            float color[] = player.getColor();
//            color[0] += 0.01f;
//            player.setColor(color);
        } else {
            gs.setRunning(false);
        }

        //Log.i(TAG, "EnemyHandler: Boobies won!");
    }

    public int getReversed() {
        return reversed;
    }

    public void setReversed(int reversed) {
        this.reversed = reversed;
    }

}
