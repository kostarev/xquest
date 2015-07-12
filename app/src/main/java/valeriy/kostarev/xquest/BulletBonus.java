package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by valerik on 27.12.2014.
 */
public class BulletBonus extends Unit {

    private int liveTime;
    private long lastTime;
    private Paint paintText;

    public BulletBonus(Game game, int id) {
        super(game, id);
        speedX = game.kvant;
        speedY = (int) (0.5 * game.kvant);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);

        paintText = new Paint();
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(2 * game.kvant);

        lastTime = game.time();
        //Время жизни
        liveTime = 20000;

        //Награда за поимку
        cost = 500;
    }

    @Override
    public void draw(Canvas canvas) {
        updatePosition();
        //canvas.drawBitmap(game.monstr1Bitmap, gameX + game.getGameScreenX0(), gameY + game.getGameScreenY0(), paint);
        canvas.drawCircle(gameX + game.getGameScreenX0(), gameY + game.getGameScreenY0(), 2 * game.kvant, paint);
        canvas.drawText((liveTime / 1000) + "", gameX + game.getGameScreenX0() - (int) paint.measureText("9"), gameY + game.getGameScreenY0() + (int) paint.getTextSize() / 2, paintText);
        if (liveTime < 0) {
            killMe(true);
            return;
        }
    }

    //Хаотичное движение
    public void updatePosition() {
        if (!game.isRunning() || game.isWaiting()) {
            return;
        }


        liveTime = (int) (liveTime - (game.time() - lastTime));
        lastTime = game.time();

        gameX = gameX + speedX;
        gameY = gameY + speedY;

        //Столкновение со стенами
        if (gameX < 0) {
            gameX = 0;
            speedX = -speedX;
        } else if (gameX > game.getGameScreenWidth() - game.monsterWidth) {
            gameX = game.getGameScreenWidth() - game.monsterWidth;
            speedX = -speedX;
        }
        if (gameY < 0) {
            gameY = 0;
            speedY = -speedY;
        } else if (gameY > game.getGameScreenHeight() - game.monsterWidth) {
            gameY = game.getGameScreenHeight() - game.monsterWidth;
            speedY = -speedY;
        }

        rect.set((int) gameX + game.getGameScreenX0(),
                (int) gameY + game.getGameScreenY0(),
                (int) gameX + game.monsterWidth + game.getGameScreenX0(),
                (int) gameY + game.monsterWidth + game.getGameScreenY0());
    }

    public void hero() {
        killMe(false);
        try {
            game.bulletTimer.setRunning(false);
            game.bulletThread.join();
            game.bulletThread = new Thread(game.bulletTimer);
            game.bulletThread.start();
        } catch (Exception e) {

        }

        Random r = new Random();
        int tmp = r.nextInt(100);

        if (tmp < 50) {
            game.hero.setBulletType(Hero.BULLET_2);
        } else if (tmp < 80) {
            game.hero.setBulletType(Hero.BULLET_BACK);
        } else {
            game.hero.setBulletType(Hero.BULLET_3);
        }

    }

    @Override
    public void killMe(boolean withExplosion) {
        //Уничтожаемся
        game.monsters[id] = null;
        //Создаём взрыв
        if (withExplosion) {
            game.newExplode(gameX + rect.width() / 2, gameY + rect.height() / 2, 1);
        } else {
            //Добавляем очки
            game.incPoints(cost);
        }
    }

    //Что бы пуля не сбивала
    @Override
    public boolean hit() {
        return false;
    }

    @Override
    public boolean intersect() {
        return game.hero.heroRect.intersect(this.rect);
    }
}
