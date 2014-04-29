package com.example.KittensAndBoobies.Profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.KittensAndBoobies.R;

import java.util.List;

/**
 * Created by benoc on 29/04/2014.
 */
public class AchiAdapter extends BaseAdapter {

    private Context context;
    private List<Achievement> values;

    public AchiAdapter(Context context, List<Achievement> values) {
        this.context = context;
        this.values = values;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);
            gridView = inflater.inflate(R.layout.achievement, null);

            ImageView imageView = (ImageView) gridView.findViewById(R.id.achiImage);

            Bitmap achi = values.get(position).getImage();
            imageView.setImageBitmap(achi);

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
