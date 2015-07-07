package valeriy.kostarev.xquest;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by valerik on 26.12.2014.
 */
public class BulletTimer extends Thread {
    private Game game;
    private boolean running;
    private long lastSpawnTime;
    private int sleepTime;


    public BulletTimer(Game game) {
        this.game = game;
        //Время жизни типа пуль
        sleepTime = 30000;
    }

    @Override
    public void run() {
        setRunning(true);
        lastSpawnTime = System.currentTimeMillis();

        while(running) {
            while(!game.isRunning()){
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                    lastSpawnTime = lastSpawnTime + 100;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //Время истекло
            if(lastSpawnTime+sleepTime<System.currentTimeMillis()) {
                setRunning(false);
                game.hero.setBulletType(game.hero.BULLET_NORMAL);
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public long getTime(){
        return lastSpawnTime+sleepTime-System.currentTimeMillis();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
