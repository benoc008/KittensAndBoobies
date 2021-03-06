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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.app.Activity;
import android.opengl.GLES20;
import android.util.Log;
import com.example.KittensAndBoobies.GameActivity;
import com.example.KittensAndBoobies.GameScheduler;
import com.example.KittensAndBoobies.R;
import com.example.KittensAndBoobies.myRenderer;

/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Revers extends GameObject {

    private boolean activated;

    public Revers(int texture){
        super(texture);
    }

    public Revers clone(){
        Revers temp = new Revers(getmTextureDataHandle());
        temp.setColor(getColor());
        temp.setPosition(getPosition());
        return temp;
    }

    public void onCollision(GameObject player, GameScheduler gs){
        if(getLife() == -1){
            //((Player) player).setReversed(((Player) player).getReversed() * -1);
            ((Player) player).setReversed(100);
            setLife(100);
            activated = true;
        }
    }

    @Override
    public void onDeath(GameObject player, GameScheduler gs){
//        if(activated){
//            ((Player) player).setReversed(((Player) player).getReversed() * -1);
//        }
    }
}