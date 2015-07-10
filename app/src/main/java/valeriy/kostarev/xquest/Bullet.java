package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by valerik on 14.12.2014.
 */
public class Bullet {
    private Paint paint;
    private float x, y, speedX, speedY;
    private int radius;
    private Game game;
    private Rect bulletRect;
    private int id, liveTime;
    private long burnTime;

    public Bullet(Game game, int id, float x, float y, float speedX, float speedY) {
        this.id = id;
        this.game = game;
        this.x = x;
        this.y = y;
        this.speedX = 2 * speedX;
        this.speedY = 2 * speedY;
        bulletRect = new Rect((int) x, (int) y, (int) (x + radius), (int) (y + radius));

        //Время жизни пули, мс
        liveTime = 20 * 1000;
        //Время рождения пули
        burnTime = System.currentTimeMillis();

        paint = new Paint();
        radius = game.kvant / 6 + 1;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
    }

    public void draw(Canvas canvas) {
        updatePosition();
        canvas.drawCircle(x + game.getGameScreenX0(), y + game.getGameScreenY0(), radius, paint);
    }

    public void updatePosition() {
        if (!game.isRunning() || game.isWaiting()) {
            return;
        }

        //Если пуля живёт дольше liveTime, убиваем пулю
        if (burnTime + liveTime < System.currentTimeMillis()) {
            game.bullets[id] = null;
        }

        x = x + speedX;
        y = y + speedY;
        bulletRect.set((int) (x + game.getGameScreenX0()), (int) (y + game.getGameScreenY0()), (int) (x + radius + game.getGameScreenX0()), (int) (y + radius + game.getGameScreenY0()));

        //Обработка столкновений
        //Стены
        if (game.border.isIntersect(bulletRect)) {
            killMe();
        }

        //Монстры
        for (Enemy enemy : game.monsters) {
            if (enemy != null) {
                if (bulletRect.intersect(enemy.rect)) {
                    //Ударяем врага
                    if (enemy.hit()) {
                        //Уничтожаем пулю
                        killMe();
                    }
                }
            }
        }


        //Мины
        for (Mine mine : game.mines) {
            if (mine != null) {
                if (bulletRect.intersect(mine.rect)) {
                    //Ударяем астероид
                    mine.hit();
                    //Уничтожаем пулю
                    killMe();
                }
            }
        }
    }

    //Уничтожение пули
    public void killMe() {

        //Создаём взрыв
        game.newExplode(x - radius / 2, y - radius / 2, 0);
        game.bullets[id] = null;

        //Звук взрыва
        game.sound.play("explode", 1, (int) x, (int) y);
    }
}
