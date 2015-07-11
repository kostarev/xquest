package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by valerik on 12.12.2014.
 */
public class Mine{

    public Rect rect;
    protected Paint paint;
    protected Game game;
    private int health, id, x, y, setkaX, setkaY;
    private float rotateAngle, rotateAngleInc;
    private int rotationWaitTime;
    private long lastRotationTime;



    public Mine(Game game, int id) {
        this.id = id;
        this.game = game;
        paint = new Paint();
        Random r = new Random();

        //Выбираем свободное место для кристала
        setkaX = 0;
        setkaY = 0;

        setkaX = (r.nextInt(game.setkaWidth) + 0);
        setkaY = (r.nextInt(game.setkaHeight) + 0);

        //Ищем свободное место
        boolean check = false;
        while (!check) {
            setkaX = (r.nextInt(game.setkaWidth) + 0);
            setkaY = (r.nextInt(game.setkaHeight) + 0);
            if (game.setka[setkaX][setkaY] == 0) {
                check = true;
            }
        }


        //Определяем координаты
        x = setkaX * game.elementWidth;
        y = setkaY * game.elementWidth;
        //Занимаем место
        game.setka[setkaX][setkaY] = Game.ASTEROID;

        rect = new Rect(x + game.getGameScreenX0(), y + game.getGameScreenY0(), x + game.setkaWidth + game.getGameScreenX0(), y + game.setkaHeight + game.getGameScreenY0());


        //Здоровье астеройда
        health = 3;

        rotationWaitTime = 10;
        rotateAngleInc = -1;
    }

    public void draw(Canvas canvas) {

        if(lastRotationTime+rotationWaitTime<System.currentTimeMillis()) {
            lastRotationTime = System.currentTimeMillis();
            rotateAngle++;
        }

        canvas.save();
        canvas.rotate(rotateAngle, x + game.getGameScreenX0()+game.elementWidth/2, y + game.getGameScreenY0()+game.elementWidth/2);
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
        game.newExplode(x+ rect.width()/2,y+ rect.height()/2,1);
        //Звук взрыва
        game.sound.play("explode", 1f, x, y);

        game.setka[setkaX][setkaY] = 0;
        game.mines[id] = null;
    }
}
