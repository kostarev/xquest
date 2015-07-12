package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by valerik on 12.12.2014.
 */
public class Mine {

    public Rect rect;
    protected Paint paint;
    protected Game game;
    private int health, id, x, y, setkaX, setkaY;
    private float rotateAngle, rotateAngleInc;
    private int rotationWaitTime;
    private long lastRotationTime;


    //Установка мины в случайное место карты
    public Mine(Game game, int id) {
        this.id = id;
        this.game = game;
        paint = new Paint();

        setkaX = 0;
        setkaY = 0;

        //Здоровье
        health = 3;

        rotationWaitTime = 1;
        rotateAngleInc = -1;

        setRadomPlace();
    }

    //Установка мины в заданном месте карты
    public Mine(Game game, int id, int setkaX, int setkaY) {
        this.id = id;
        this.game = game;
        paint = new Paint();

        //Здоровье
        health = 3;

        rotationWaitTime = 1;
        rotateAngleInc = -1;

        setMine(setkaX, setkaY);
    }

    //Установка координат мины
    public void setMine(int setkaX, int setkaY) {
        //Определяем координаты
        this.x = setkaX * game.elementWidth;
        this.y = setkaY * game.elementWidth;
        //Занимаем место
        game.setka[setkaX][setkaY] = Game.MINE;

        rect = new Rect(this.x + game.getGameScreenX0(), this.y + game.getGameScreenY0(), this.x + game.setkaWidth + game.getGameScreenX0(), this.y + game.setkaHeight + game.getGameScreenY0());
    }

    public void setRadomPlace() {
        Random r = new Random();
        //Выбираем свободное место
        setkaX = (r.nextInt(game.setkaWidth) + 0);
        setkaY = (r.nextInt(game.setkaHeight) + 0);

        //Ищем свободное место
        boolean ok = false;
        while (!ok) {
            setkaX = (r.nextInt(game.setkaWidth) + 0);
            setkaY = (r.nextInt(game.setkaHeight) + 0);
            if (game.setka[setkaX][setkaY] == 0) {
                ok = true;
            }
        }

        //Определяем координаты
        x = setkaX * game.elementWidth;
        y = setkaY * game.elementWidth;
        //Занимаем место
        game.setka[setkaX][setkaY] = Game.MINE;
        rect = new Rect(x + game.getGameScreenX0(), y + game.getGameScreenY0(), x + game.setkaWidth + game.getGameScreenX0(), y + game.setkaHeight + game.getGameScreenY0());
    }

    public void draw(Canvas canvas) {

        if (lastRotationTime + rotationWaitTime < game.time()) {
            lastRotationTime = game.time();
            rotateAngle++;
        }

        canvas.save();
        canvas.rotate(rotateAngle, x + game.getGameScreenX0() + game.elementWidth / 2, y + game.getGameScreenY0() + game.elementWidth / 2);
        game.mineAnimation.draw(canvas, game.getGameScreenX0() + x, game.getGameScreenY0() + y);
        canvas.restore();
        rect.set(x + game.getGameScreenX0(), y + game.getGameScreenY0(), x + game.setkaWidth + game.getGameScreenX0(), y + game.setkaHeight + game.getGameScreenY0());
    }

    public void hit() {
        health--;
        if (health == 0) {
            killMe();
        }
    }

    public void killMe() {
        //Создаём взрыв
        game.newExplode(x + rect.width() / 2, y + rect.height() / 2, 1);
        //Звук взрыва
        game.sound.play("explode", 1f, x, y);

        game.setka[setkaX][setkaY] = 0;
        game.mines[id] = null;
    }
}
