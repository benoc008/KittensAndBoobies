package com.example.KittensAndBoobies.Objects;

/**
 * Created by benoc on 01/05/2014.
 */
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

import android.app.Activity;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import com.example.KittensAndBoobies.GameScheduler;
import com.example.KittensAndBoobies.R;
import com.example.KittensAndBoobies.myRenderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


/**
 * A two-dimensional square for use as a drawn object in OpenGL ES 2.0.
 */
public class Bomb extends GameObject {

    public Bomb(int texture){
        super(texture);
    }

    public Bomb clone(){
        Bomb temp = new Bomb(getmTextureDataHandle());
        temp.setColor(getColor());
        temp.setPosition(getPosition());
        return temp;
    }

    public void onCollision(GameObject player, GameScheduler gs){
        //gs.getEh().getToRemove().addAll(gs.getEh().getEnemies());
        //gs.getEh().getToAdd().add(new Enemy());
        for(GameObject o : gs.getEh().getEnemies()){
            o.setLife(0);
        }
    }
}