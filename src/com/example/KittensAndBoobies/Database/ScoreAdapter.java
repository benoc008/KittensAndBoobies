package com.example.KittensAndBoobies.Database;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.KittensAndBoobies.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by benoc on 28/04/2014.
 */
public class ScoreAdapter extends BaseAdapter{

    Context context;
    List<Record> values;
    private static LayoutInflater inflater = null;

    public ScoreAdapter(Context context, List<Record> values) {
        this.context = context;
        this.values = values;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position)
    {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row, null);

        // score
        TextView score = (TextView) vi.findViewById(R.id.score);
        score.setText(""+values.get(position).getScore());

        //duration
        Date durationDate = new Date(values.get(position).getDuration() - TimeZone.getDefault().getRawOffset());
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String durationFormatted = formatter.format(durationDate);

        TextView duration = (TextView) vi.findViewById(R.id.duration);
        duration.setText("Duration: " + durationFormatted);

        // start
        Date startDate = new Date(values.get(position).getStart_time() - TimeZone.getDefault().getRawOffset());
        DateFormat startformatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String startFormatted = startformatter.format(startDate);

        TextView start = (TextView) vi.findViewById(R.id.start);
        start.setText("Start: " + startFormatted);
        return vi;
    }
}
