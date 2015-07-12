package valeriy.kostarev.xquest;

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
        //Время действия новых пуль
        sleepTime = 30000;
    }

    @Override
    public void run() {
        setRunning(true);
        lastSpawnTime = game.time();

        while (running) {
            //Время истекло
            if (lastSpawnTime + sleepTime < game.time()) {
                setRunning(false);
                game.hero.setBulletType(Hero.BULLET_NORMAL);
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public long getTime() {
        return lastSpawnTime + sleepTime - game.time();
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
