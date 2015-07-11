package valeriy.kostarev.xquest;
//Grunger
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by valerik on 07.12.2014.
 */
public class Monstr1 extends Enemy {

    private float rotateAngle;
    private int rotationWaitTime;
    private long lastRotationTime;

    public Monstr1(Game game, int id) {
        super(game, id);
        speedX = game.kvant/6+1;
        speedY = game.kvant/6+1;
        fullSpeed = (int) Math.sqrt(speedX * speedX + speedY * speedY);
        rect = new Rect((int) gameX, (int) gameY, (int) gameX + game.monsterWidth, (int) gameY + game.monsterWidth);
        rotateAngle = 0;
        rotationWaitTime = 10;
        cost = 200;
    }


    public void draw(Canvas canvas) {
        updatePosition();
        canvas.save();
        canvas.rotate(rotateAngle, gameX + game.getGameScreenX0()+game.monsterWidth/2, gameY + game.getGameScreenY0()+game.monsterWidth/2);
        canvas.drawBitmap(game.monstrBitmap[0], gameX + game.getGameScreenX0(), gameY + game.getGameScreenY0(), paint);
        canvas.restore();
    }

    //Хаотичное движение
    public void updatePosition() {
        if(!game.isRunning() || game.isWaiting()){
            return;
        }
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

        rect.set((int) gameX + game.getGameScreenX0(), (int) gameY + game.getGameScreenY0(), (int) gameX + game.monsterWidth + game.getGameScreenX0(), (int) gameY + game.monsterWidth + game.getGameScreenY0());

        //Через случайный промежуток времени меняем скорости по осям на случайные
        if (lastTime < System.currentTimeMillis() - sleepTime) {
            lastTime = System.currentTimeMillis();


            Random r = new Random();

            speedX = r.nextFloat() * fullSpeed;
            speedY = (float) Math.sqrt(fullSpeed * fullSpeed - speedX * speedX);

            if (r.nextInt(5) > 3) {
                speedX = -speedX;
            }
            if (r.nextInt(5) > 3) {
                speedY = -speedY;
            }

            sleepTime = r.nextInt(30000) + 3000;

        }

        if(lastRotationTime+rotationWaitTime<System.currentTimeMillis()) {
            lastRotationTime = System.currentTimeMillis();
            rotateAngle++;
        }
    }

    public void hero() {
        game.hero.killMe();
        killMe();
    }

    public boolean intersect() {
        return game.hero.hitRect.intersect(this.rect);
    }
}