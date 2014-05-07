/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.KittensAndBoobies.Objects;

import android.app.Activity;
import com.example.KittensAndBoobies.GameScheduler;
import com.example.KittensAndBoobies.R;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Enemy extends GameObject {

    public Enemy(int texture){
        super(texture);
    }

    public Enemy clone(){
        Enemy temp = new Enemy(getmTextureDataHandle());
        temp.setColor(getColor());
        temp.setPosition(getPosition());
        return temp;
    }

    public void onCollision(GameObject player, GameScheduler gs){
        if(player.getLife() > 0){
            player.setLife(player.getLife() - 1);
        } else {
            gs.setRunning(false);
            // TODO game over should not be implemented like this
            // i mean a method in gs that throws up a window with points, restart option and more
        }

        //Log.i(TAG, "EnemyHandler: Boobies won!");
    }
}