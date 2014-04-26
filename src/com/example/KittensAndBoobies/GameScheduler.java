package com.example.KittensAndBoobies;

/**
 * Created by benoc on 26/04/2014.
 */
public class GameScheduler implements Runnable {

    private boolean running;
    private boolean lock;
    private int level;
    private int timer;
    private int points;

    private myRenderer renderer;
    private EnemyHandler eh;

    public GameScheduler(myRenderer renderer){
        this.renderer = renderer;

        running = true;
        lock = false;
        level = 0;
        timer = 0;
        points = 0;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while(running){
            synchronized (eh) {
                while (lock) {
                    try {
                        ((Object)eh).wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock = true;
                //Log.i(TAG, "pwnd: run");
                eh.moveEnemies();
                if (timer++ > 100) {
                    timer = 0;
                    if (level < 20)
                        level++;
                    eh.addNew(eh.getToAdd());
                }
                lock = false;
                ((Object) (eh)).notify();
            }
            try{
                Thread.sleep(75 - level * 3);       //the timing needs some optimization
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setEh(EnemyHandler eh) {
        this.eh = eh;
    }

}
