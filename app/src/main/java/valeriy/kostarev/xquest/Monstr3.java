package valeriy.kostarev.xquest;
//Zinger

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.Random;

/**
 * Created by valerik on 07.12.2014.
 */
public class Monstr3 extends Monstr1 {

    private long shotlastTime;
    private int shotSleepTime, shotMaxSleepTime, bulletMaxSpeed;

    public Monstr3(Game game, int id) {
        super(game, id);
        cost = 300;

        //Задержка между выстрелами
        shotMaxSleepTime = 3000;
        shotSleepTime = new Random().nextInt(shotMaxSleepTime);

        //Скорость пули
        bulletMaxSpeed = game.kvant;
    }


    @Override
    public void draw(Canvas canvas) {
        updatePosition();
        canvas.save();
        canvas.rotate(rotateAngle, gameX + game.getGameScreenX0() + game.monsterWidth / 2, gameY + game.getGameScreenY0() + game.monsterWidth / 2);
        canvas.drawBitmap(game.monstrBitmap[2], gameX + game.getGameScreenX0(), gameY + game.getGameScreenY0(), paint);
        canvas.restore();
    }

    @Override
    public void updatePosition() {
        super.updatePosition();
        if (!game.isRunning() || game.isWaiting()) {
            return;
        }
        //Через случайный промежуток времени стреляем в случайном направлении
        if (shotlastTime < game.time() - shotSleepTime) {
            shotlastTime = game.time();
            Random r = new Random();
            float dxInc = r.nextFloat() * bulletMaxSpeed;
            float dyInc = (float) Math.sqrt(fullSpeed * fullSpeed - speedX * speedX);
            if (r.nextInt(5) > 3) {
                dxInc = -dxInc;
            }
            if (r.nextInt(5) > 3) {
                dyInc = -dyInc;
            }
            shotSleepTime = r.nextInt(shotMaxSleepTime);
            game.newBullet(Bullet.OWNER_ENEMY, gameX, gameY, dxInc, dyInc, Color.GREEN, 1);
            //Звук выстрела
            game.sound.play("shot", 0.1f, (int) gameX, (int) gameY);
        }
    }

}