package valeriy.kostarev.xquest;
//Miner

import android.graphics.Canvas;

/**
 * Created by valerik on 07.12.2014.
 */
public class Monstr5 extends Monstr1 {

    private long lastMineTime;
    private int mineSleepTime;

    public Monstr5(Game game, int id) {
        super(game, id);
        sleepTime = 2000;
        cost = 600;
        dAngle = 10;
        //Задержка перед установкой мины
        mineSleepTime = 10000;
        lastMineTime = game.time();
    }

    public void draw(Canvas canvas) {
        updatePosition();
        putMine();
        canvas.save();
        canvas.rotate(rotateAngle, gameX + game.getGameScreenX0() + game.monsterWidth / 2, gameY + game.getGameScreenY0() + game.monsterWidth / 2);
        canvas.drawBitmap(game.monstrBitmap[4], gameX + game.getGameScreenX0(), gameY + game.getGameScreenY0(), paint);
        canvas.restore();
    }

    //Установка мины
    public void putMine() {
        if (lastMineTime + mineSleepTime > game.time()) {
            return;
        }

        lastMineTime = game.time();
        game.putMine((int) gameX, (int) gameY);

    }

}