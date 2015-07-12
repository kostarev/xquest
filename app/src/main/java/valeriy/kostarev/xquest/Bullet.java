package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by valerik on 14.12.2014.
 */
public class Bullet {

    //Владелец пули
    public static final byte OWNER_HERO = 0;
    public static final byte OWNER_ENEMY = 1;

    private Paint paint;
    private float x, y, speedX, speedY;
    private int radius;
    private Game game;
    private Rect bulletRect;
    private int id, liveTime;
    private long burnTime;
    private int owner;


    public Bullet(Game game, int owner, int id, float x, float y, float speedX, float speedY, int color, int radius) {
        this.id = id;
        this.owner = owner;
        this.game = game;
        this.x = x;
        this.y = y;
        this.speedX = 2 * speedX;
        this.speedY = 2 * speedY;
        bulletRect = new Rect((int) x, (int) y, (int) (x + radius), (int) (y + radius));

        //Время жизни пули, мс
        liveTime = 20000;
        //Время рождения пули
        burnTime = game.time();

        paint = new Paint();
        this.radius = radius * game.kvant / 10 + 1;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
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
        if (burnTime + liveTime < game.time()) {
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

        //Если пуля принадлежит герою
        if (owner == OWNER_HERO) {
            //Монстры
            for (Unit unit : game.monsters) {
                if (unit != null) {
                    if (bulletRect.intersect(unit.rect)) {
                        //Ударяем врага
                        if (unit.hit()) {
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
                        //Ударяем мину
                        mine.hit();
                        //Уничтожаем пулю
                        killMe();
                    }
                }
            }
        } else
            //Если пуля принадлежит врагу
            if (owner == OWNER_ENEMY) {
                //Ударяем героя
                if (bulletRect.intersect(game.hero.hitRect)) {
                    //Уничтожаем пулю
                    killMe();
                    //Убиваем героя
                    game.hero.killMe();
                }
            }
    }

    //Уничтожение пули
    public void killMe() {

        //Создаём взрыв
        game.newExplode(x - radius / 2, y - radius / 2, 0);
        game.bullets[id] = null;

        //Звук взрыва
        game.sound.play("explode", 0.3f, (int) x, (int) y);
    }
}
