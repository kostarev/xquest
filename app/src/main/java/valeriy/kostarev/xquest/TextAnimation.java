package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by valerik on 08.08.2015.
 */
public class TextAnimation extends Animation {

    private String text;
    private Paint paint;

    public TextAnimation(Game game) {
        this.game = game;
        paint = new Paint();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(int color){
        paint.setColor(color);
    }


    void draw(Canvas canvas) {
        canvas.drawText(text, x, y, paint);
        action();
    }

    public void setSize(float size){
        paint.setTextSize(size);
    }

    public void lastAction(){
        x = x1;
        y = y1;
        size = size1;
        setSize(size);
        paint.setColor(Color.argb(100, 180, 180, 180));
    }
}
