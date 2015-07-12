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
        if (!game.isRunning()) {
            return;
        }
        setRunning(true);

        while (running) {
            while (!game.isRunning()) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (lastSpawnTime + sleepTime < game.time()) {

                Random r = new Random();
                //Случайный портал (левый или правый)
                boolean leftSide = r.nextBoolean();
                leftSide = true;
                if (leftSide) {
                    game.portal.setLeftCharger(true);
                } else {
                    game.portal.setRightCharger(true);
                }
                //Ждём 1 секунду перед спауном, показываем анимацию разрядников
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Выключаем разрядники
                game.portal.setLeftCharger(false);
                game.portal.setRightCharger(false);


                int x = r.nextInt(100);
                //Процент спауна пуль
                if (x < 15) {
                    game.portal.spawn(Portal.BULLETS, leftSide);
                } else {
                    x = r.nextInt(5);

                    switch (x) {
                        case 0:
                            game.portal.spawn(Portal.MONSTR1, leftSide);
                            break;
                        case 1:
                            game.portal.spawn(Portal.MONSTR2, leftSide);
                            break;
                        case 2:
                            game.portal.spawn(Portal.MONSTR3, leftSide);
                            break;
                        case 3:
                            game.portal.spawn(Portal.MONSTR4, leftSide);
                            break;
                        case 4:
                            game.portal.spawn(Portal.MONSTR5, leftSide);
                            break;
                    }
                }
                lastSpawnTime = game.time();
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
