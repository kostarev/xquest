package valeriy.kostarev.xquest;
//Vince

import android.graphics.Canvas;

/**
 * Created by valerik on 07.12.2014.
 */
public class Monstr4 extends Monstr1 {

    public Monstr4(Game game, int id) {
        super(game, id);
        cost = 500;
    }


    @Override
    public void draw(Canvas canvas) {
        updatePosition();
        canvas.save();
        canvas.rotate(rotateAngle, gameX + game.getGameScreenX0() + game.monsterWidth / 2, gameY + game.getGameScreenY0() + game.monsterWidth / 2);
        canvas.drawBitmap(game.monstrBitmap[3], gameX + game.getGameScreenX0(), gameY + game.getGameScreenY0(), paint);
        canvas.restore();
    }
}