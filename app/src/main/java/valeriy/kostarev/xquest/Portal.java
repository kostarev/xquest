package valeriy.kostarev.xquest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

/**
 * Created by valerik on 25.12.2014.
 */
public class Portal {

    public static final int BULLETS = 0;
    public static final int MONSTR1 = 1;
    public static final int MONSTR2 = 2;
    private Game game;
    private Paint paint;
    private Bitmap portalLeftBitmap, portalRightBitmap;
    private int portalWidth, portalHeight;

    public Portal(Game game) {

        this.game = game;
        paint = new Paint();

        Bitmap realPortalBitmap = BitmapFactory.decodeResource(game.gameView.activity.getResources(), R.drawable.portal);
        int hw = realPortalBitmap.getHeight() / realPortalBitmap.getWidth();
        portalWidth = 10 * game.kvant;
        portalHeight = portalWidth * hw;
        portalRightBitmap = Bitmap.createScaledBitmap(realPortalBitmap, portalWidth, portalHeight, true);
        portalLeftBitmap = Bitmap.createScaledBitmap(realPortalBitmap, -portalWidth, portalHeight, true);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(portalLeftBitmap, game.getGameScreenX0(), game.getGameScreenY0() + game.getGameScreenHeight() / 2 - portalHeight / 2, paint);
        canvas.drawBitmap(portalRightBitmap, game.getGameScreenX1() - portalRightBitmap.getWidth(), game.getGameScreenY0() + game.getGameScreenHeight() / 2 - portalHeight / 2, paint);
    }

    public void spawn(int type) {
        Log.i("Portal", "spawn " + type);

        //reduce size of array of monsters +1
        if (game.monsters == null) {
            game.monsters = new Enemy[1];
        } else {
            Enemy[] tmp = game.monsters;
            game.monsters = new Enemy[game.monsters.length+1];

                for (int i = 0; i < tmp.length; i++) {
                    game.monsters[i] = tmp[i];
                }
        }

        int id = game.monsters.length-1;
        switch (type){
            case BULLETS:
                game.monsters[id] = new BulletPrize(game, id);
                break;
            case MONSTR1:
            game.monsters[id] = new Monstr1(game, id);
            break;
            case MONSTR2:
                game.monsters[id] = new Monstr2(game, id);
                break;

        }
        game.monsters[id].setGameY(game.getGameScreenHeight() / 2);
        Random r = new Random();
        if (r.nextBoolean()) {
            //Left portal
            game.monsters[id].setGameX(portalWidth);
        } else {
            //right portal
            game.monsters[id].setGameX(game.getGameScreenWidth() - portalWidth);
            game.monsters[id].speedX = -game.monsters[id].speedX;
        }
    }
}
