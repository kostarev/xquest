package valeriy.kostarev.xquest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by valerik on 25.12.2014.
 */
public class Portal {

    public static final int BULLETS = 0;
    public static final int MONSTR1 = 1;
    public static final int MONSTR2 = 2;
    public static final int MONSTR3 = 3;
    private Game game;
    private Paint paint;
    private Bitmap portalLeftBitmap, portalRightBitmap;
    private int portalWidth, portalHeight, dischargeWidth, dischargeHeight;
    private boolean leftChargerAnimation, rightChargerAnimation;
    private MyAnimation dischargeAnimation;

    public Portal(Game game) {

        this.game = game;
        paint = new Paint();

        Bitmap realPortalBitmap = BitmapFactory.decodeResource(game.gameView.activity.getResources(), R.drawable.portal);
        int hw = realPortalBitmap.getHeight() / realPortalBitmap.getWidth();
        portalWidth = 10 * game.kvant;
        portalHeight = portalWidth * hw;
        portalRightBitmap = Bitmap.createScaledBitmap(realPortalBitmap, portalWidth, portalHeight, true);
        portalLeftBitmap = Bitmap.createScaledBitmap(realPortalBitmap, -portalWidth, portalHeight, true);

        //Анимация разрядов порталов
        dischargeWidth = 4 * game.kvant;
        dischargeHeight = 5 * game.kvant;
        Bitmap dischargeBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(game.gameView.activity.getResources(), R.drawable.discharge), dischargeWidth * 8, dischargeHeight, true);
        dischargeAnimation = new MyAnimation(dischargeBitmap, dischargeWidth, dischargeHeight, 10);
        dischargeAnimation.setRunning(true);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(portalLeftBitmap, game.getGameScreenX0(), game.getGameScreenY0() + game.getGameScreenHeight() / 2 - portalHeight / 2, paint);
        canvas.drawBitmap(portalRightBitmap, game.getGameScreenX1() - portalRightBitmap.getWidth(), game.getGameScreenY0() + game.getGameScreenHeight() / 2 - portalHeight / 2, paint);

        if (leftChargerAnimation) {
            dischargeAnimation.draw(canvas, game.getGameScreenX0() + portalLeftBitmap.getWidth() - dischargeWidth, game.getGameScreenY0() + game.getGameScreenHeight() / 2 - dischargeHeight / 2);
        }

        if (rightChargerAnimation) {
            dischargeAnimation.draw(canvas, game.getGameScreenX1() - portalRightBitmap.getWidth(), game.getGameScreenY0() + game.getGameScreenHeight() / 2 - dischargeHeight / 2);
        }
    }


    //Включение разрядников
    public void setLeftCharger(boolean running) {
        leftChargerAnimation = running;
    }

    public void setRightCharger(boolean running) {
        rightChargerAnimation = running;
    }

    public void spawn(int type, boolean leftSide) {
        //Log.i("Portal", "spawn " + type+ "leftSide "+leftSide);

        //увеличиваем размер массива монстров
        if (game.monsters == null) {
            game.monsters = new Unit[1];
        } else {
            Unit[] tmp = game.monsters;
            game.monsters = new Unit[game.monsters.length + 1];

            for (int i = 0; i < tmp.length; i++) {
                game.monsters[i] = tmp[i];
            }
        }

        int id = game.monsters.length - 1;
        switch (type) {
            case BULLETS:
                game.monsters[id] = new BulletPrize(game, id);
                break;
            case MONSTR1:
                game.monsters[id] = new Monstr1(game, id);
                break;
            case MONSTR2:
                game.monsters[id] = new Monstr2(game, id);
                break;
            case MONSTR3:
                game.monsters[id] = new Monstr3(game, id);
                break;

        }
        game.monsters[id].setGameY(game.getGameScreenHeight() / 2);

        if (leftSide) {
            //Left portal
            game.monsters[id].setGameX(portalWidth);
        } else {
            //right portal
            game.monsters[id].setGameX(game.getGameScreenWidth() - portalWidth);
            game.monsters[id].speedX = -game.monsters[id].speedX;
        }
    }
}
