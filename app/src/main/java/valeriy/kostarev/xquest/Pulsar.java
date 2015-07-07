package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by valerik on 12.12.2014.
 */
public class Pulsar {
    private int imageId;
    private Game game;
    private int id,x,y;
    private double speedMas;
    private Paint paint;

    public Pulsar(Game game, int id) {
        this.game = game;
        this.id = id;

        speedMas = 0.99;

        Random r = new Random();
        //Определяем координаты планеты
        x = r.nextInt((int)(game.getGameScreenWidth()*speedMas));
        y = r.nextInt((int)(game.getGameScreenHeight()*speedMas));

        paint = new Paint();
    }

    public void draw(Canvas canvas) {
        game.pulsarAnimation.draw(canvas,(int)(game.getGameScreenX0()/speedMas+x),(int)(game.getGameScreenY0()/speedMas+y));
    }
}
