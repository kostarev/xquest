package valeriy.kostarev.xquest;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by valerik on 26.12.2014.
 */
public class SpawnTimer extends Thread {
    private Game game;
    private boolean running;
    private long lastSpawnTime;
    private int sleepTime;

    public SpawnTimer(Game game) {
        this.game = game;

        //Время задержки между спауном врагов
        sleepTime = 10000;
    }

    @Override
    public void run() {
        if(!game.isRunning()){
            return;
        }
        setRunning(true);

        while(running) {
            while(!game.isRunning()){
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                    lastSpawnTime = lastSpawnTime + 100;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(lastSpawnTime+sleepTime<System.currentTimeMillis()) {
                lastSpawnTime = System.currentTimeMillis();
                Random r = new Random();
                int x = r.nextInt(100);
                //Процент спауна пуль
                if(x<15) {
                    game.portal.spawn(Portal.BULLETS);
                }else{
                    game.portal.spawn(Portal.MONSTR1);
                }
            }

            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
