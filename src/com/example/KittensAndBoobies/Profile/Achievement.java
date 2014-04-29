package com.example.KittensAndBoobies.Profile;

import android.content.Context;
import android.graphics.*;
import com.example.KittensAndBoobies.R;

/**
 * Created by benoc on 29/04/2014.
 */
public class Achievement {
    private int resourceId;
    private String text;
    private Bitmap image;
    private Context context;

    public Achievement(Context context,int resourceId, String text, boolean original) {
        this.context = context;
        this.resourceId = resourceId;
        this.text = text;
        image = getOriginal();
        if(!original){
            image = getGrayscale();
        }
    }

    public Bitmap getOriginal(){
        return BitmapFactory.decodeResource(context.getResources(), resourceId);
    }

    public Bitmap getGrayscale(){
        int width, height;
        height = image.getHeight();
        width = image.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(image, 0, 0, paint);
        return bmpGrayscale;
    }

    public String getText() {
        return text;
    }

    public Bitmap getImage() {
        return image;
    }
}
