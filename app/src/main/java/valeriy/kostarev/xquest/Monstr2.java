package valeriy.kostarev.xquest;
//Zippo

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.util.Random;

/**
 * Created by valerik on 07.12.2014.
 */
public class Monstr2 extends Enemy {

    private double angleRad, dAngle;
    private float rotateAngle, x, y;
    private int rotationWaitTime, radius;
    private long lastRotationTime;


    public Monstr2(Game game, int id) {
        super(game, id);
        speedX = game.kvant / 10 + 1;
        speedY = game.kvant / 10 + 1;

        fullSpeed = (int) Math.sqrt(speedX * speedX + speedY * speedY);
        rect = new Rect((int) gameX, (int) gameY, (int) gameX + game.monsterWidth, (int) gameY + game.monsterWidth);
        rotateAngle = 0;
        rotationWaitTime = 10;
        cost = 300;

        Random r = new Random();
        radius = game.kvant * r.nextInt(20) + 10;

        angleRad = 0;
        dAngle = 0.02d;
        if (r.nextBoolean()) {
            dAngle = -dAngle;
        }
    }


    public void draw(Canvas canvas) {
        updatePosition();
        canvas.save();
        canvas.rotate(rotateAngle, gameX + game.getGameScreenX0() + game.monsterWidth / 2, gameY + game.getGameScreenY0() + game.monsterWidth / 2);
        canvas.drawBitmap(game.monstrBitmap[1], gameX + game.getGameScreenX0(), gameY + game.getGameScreenY0(), paint);
        canvas.restore();
        /*
        paint.setColor(Color.rgb(255, 0, 0));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x + game.getGameScreenX0(), y + game.getGameScreenY0(), radius, paint);
        canvas.drawPoint(x + game.getGameScreenX0(), y + game.getGameScreenY0(), paint);
        */
    }

    @Override
    public void setGameX(float gameX) {
        super.setGameX(gameX);
        setCenter();
    }

    @Override
    public void setGameY(float gameY) {
        super.setGameY(gameY);
    }

    private void setCenter() {
        Random r = new Random();
        boolean ok = false;
        while (!ok) {
            //Выбираем центр вращения, что бы объект был внутри игровой области
            angleRad = r.nextInt(365) * Math.PI / 180;
            x = (float) (gameX + radius * Math.cos(angleRad));
            y = (float) (gameY + radius * Math.sin(angleRad));

            if (x < 0 || x > game.getGameScreenWidth() || y < 0 || y > game.getGameScreenHeight()) {
                continue;
            } else {
                ok = true;
            }
        }
    }

    //Хаотичное движение центра
    public void updatePosition() {
        if (!game.isRunning() || game.isWaiting()) {
            return;
        }
        x = x + speedX;
        y = y + speedY;
        angleRad = angleRad + dAngle;
        if (angleRad > 2 * Math.PI) {
            angleRad = 0;
        }
        gameX = (float) (radius * Math.cos(angleRad) + x);
        gameY = (float) (radius * Math.sin(angleRad) + y);

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
            Log.i("random", "speedX=" + speedX + " speedY=" + speedY);
        }

        if (lastRotationTime + rotationWaitTime < System.currentTimeMillis()) {
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