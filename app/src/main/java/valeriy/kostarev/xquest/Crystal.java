package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by valerik on 06.12.2014.
 */
public class Crystal {

    public Rect rect;
    protected Paint paint;
    protected Game game;
    protected int id, x,y, setkaX, setkaY, objectCost;

    public Crystal(Game game, int id) {
        this.id = id;
        this.game = game;
        paint = new Paint();
        Random r = new Random();

        //Выбираем свободное место для кристала
        setkaX = 0;
        setkaY = 0;

        //Стоимость кристалла
        objectCost = 200;

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


        //Определяем координаты кристала
        x = setkaX * game.elementWidth;
        y = setkaY * game.elementWidth;
        //Занимаем место кристалом
        game.setka[setkaX][setkaY] = Game.CRYSTAL;

        rect = new Rect(x+game.getGameScreenX0(),y+game.getGameScreenY0(),x+game.setkaWidth+game.getGameScreenX0(),y+game.setkaHeight+game.getGameScreenY0());

    }

    public void draw(Canvas canvas) {
        game.crystallAnimation.draw(canvas,game.getGameScreenX0()+x,game.getGameScreenY0()+y);
        rect.set(x + game.getGameScreenX0(), y + game.getGameScreenY0(), x + game.setkaWidth + game.getGameScreenX0(), y + game.setkaHeight + game.getGameScreenY0());
        //canvas.drawBitmap(game.crystalBitmap,game.getGameScreenX0()+x,game.getGameScreenY0()+y,paint);
    }

    public void killMe() {
        game.setka[setkaX][setkaY] = 0;
        game.crystals[id] = null;
        //Звук кристалла
        game.sound.play("crystall", 0.2f);
    }
}
