package valeriy.kostarev.xquest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by valerik on 14.12.2014.
 */
public class ExitHit {

    private Paint paint;
    private Game game;
    private int x,y;
    Bitmap exitHitBitmap;

    public ExitHit(Game game){
        this.game = game;

        exitHitBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(game.gameView.activity.getResources(), R.drawable.exithit),5*game.kvant,5*game.kvant,true);

        paint = new Paint();
        paint.setAlpha(150);
    }

    public void draw(Canvas canvas){
        updatePosition();
        canvas.drawBitmap(exitHitBitmap,x,y,paint);
    }

    private void  updatePosition(){
        x = game.winRect.centerX()-exitHitBitmap.getWidth()/2;
        y = game.winRect.bottom;

        if(x<0){
            x = 0;
        }else if(x>game.getRealScreenWidth()-exitHitBitmap.getWidth()){
            x = game.getRealScreenWidth()-exitHitBitmap.getWidth();
        }

        if(y<game.winRect.bottom+20){
            y = game.winRect.bottom+20;
        }

        if(y<0){
            y = 0;
        }

    }

}
