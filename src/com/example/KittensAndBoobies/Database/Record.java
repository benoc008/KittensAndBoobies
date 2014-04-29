package com.example.KittensAndBoobies.Database;


/**
 * Created by benoc on 28/04/2014.
 */
public class Record implements Comparable<Record>{
    private long id;
    private long score;
    private long start_time;
    private long duration;

    @Override
    public int compareTo(Record another) {
        return (score - another.getScore() > 0)? 1
              :(score - another.getScore() < 0)? -1
              : 0;
    }

    @Override
    public String toString() {
        return start_time + "\n" + score;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
