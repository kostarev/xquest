package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by valerik on 14.12.2014.
 */
public class Explosion {
    private Paint paint;
    private Game game;
    private float x, y;
    private int id;
    private MyAnimation boomAnimation;


    public Explosion(Game game, int id, float x, float y, int size) {
        this.game = game;
        this.x = x - game.boomWidth[size] / 2;
        this.y = y - game.boomWidth[size] / 2;
        this.id = id;
        paint = new Paint();

        boomAnimation = new MyAnimation(game.boomBitmap[size], game.boomWidth[size], game.boomWidth[size], 10);
        boomAnimation.setOnce(true);
        boomAnimation.setRunning(true);
    }

    public void draw(Canvas canvas) {
        boomAnimation.draw(canvas, (int) x + game.getGameScreenX0(), (int) y + game.getGameScreenY0());
        if (boomAnimation.finished) {
            killMe();
        }
    }

    public void killMe() {
        game.explosions[id] = null;
    }
}
