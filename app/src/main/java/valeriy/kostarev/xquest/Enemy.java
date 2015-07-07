package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by valerik on 07.12.2014.
 */
public class Enemy{
    protected Game game;
    protected int health, id ,sleepTime, cost;
    protected float gameX,gameY,speedX,speedY ,fullSpeed;
    protected Paint paint;
    protected long lastTime;
    protected Rect rect;

    public Enemy(Game game,int id){
        this.game = game;
        this.id = id;

        gameX = 0;
        gameY = game.getGameScreenHeight()/2;
        paint = new Paint();
        rect = new Rect((int)(gameX+game.getGameScreenX0()),
                (int)(gameY+game.getGameScreenY0()),
                (int)(gameX+game.getGameScreenX0()+game.monsterWidth),
                (int)(gameY+game.getGameScreenY0()+game.monsterWidth));

        speedX = 20*game.kvant;
        speedY = 20*game.kvant;
        health = 1;
        cost = 100;
        //Меняет направление движения через
        sleepTime = 1000;

        lastTime = System.currentTimeMillis();

    }

    public boolean hit(){
        health--;
        if(health == 0){
            killMe();
        }
        return true;
    }

    public void killMe() {
        //Добавляем очки
        game.incPoints(cost);
        //Уничтожаем врага
        game.monsters[id] = null;
        //Создаём взрыв
        game.newExplode(gameX+ rect.width()/2,gameY+ rect.height()/2,1);
    }

    public void killMe(boolean withExplosion) {
        //Добавляем очки
        game.incPoints(cost);
        //Уничтожаем врага
        game.monsters[id] = null;
        //Создаём взрыв
        if(withExplosion) {
            game.newExplode(gameX + rect.width() / 2, gameY + rect.height() / 2, 1);
        }
    }

    public void draw(Canvas canvas){

    }

    public void setGameX(float gameX) {
        this.gameX = gameX;
    }

    public void setGameY(float gameY) {
        this.gameY = gameY;
    }

    public void hero() {

    }

    public boolean intersect() {
        return false;
    }
}
