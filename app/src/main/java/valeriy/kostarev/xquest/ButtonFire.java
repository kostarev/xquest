package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by valerik on 13.12.2014.
 */
public class ButtonFire{

    private Game game;
    private Paint paint;
    private int radius,x,y;
    private int id = -1;
    private boolean running = false;

    public ButtonFire(Game game) {
        this.game = game;
        paint = new Paint();
        radius = 5*game.kvant;
        x = radius;
        y = game.getRealScreenHeight() - radius;

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    public void draw(Canvas canvas) {

        if (running) {
            paint.setColor(Color.argb(50, 125, 125, 250));
            canvas.drawCircle(x, y, radius, paint);
        }

        paint.setColor(Color.rgb(125, 125, 250));
        canvas.drawCircle(x, y, radius/3, paint);
    }


    public void onTouchEvent(MotionEvent event) {

        // событие
        int actionMask = event.getActionMasked();
        // число касаний
        int pointerCount = event.getPointerCount();

        // индекс касания
        int pointerIndex = event.getActionIndex();

        switch (actionMask) {

            case MotionEvent.ACTION_DOWN:

            case MotionEvent.ACTION_POINTER_DOWN: // последующие касания

                if (isInside(event.getX(pointerIndex), event.getY(pointerIndex))) {
                    setRunning(true);
                    id = event.getPointerId(pointerIndex);
                    game.hero.shut();
                }
                break;


            case MotionEvent.ACTION_MOVE:

                break;


            case MotionEvent.ACTION_UP:
                if (event.getPointerId(pointerIndex) == id) {
                    id = -1;
                    setRunning(false);
                }

            case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
                if (event.getPointerId(pointerIndex) == id) {
                    id = -1;
                    setRunning(false);
                }
                break;

        }


    }

    public boolean isInside(Float x, Float y) {
        if ((x - this.x) * (x - this.x) + (y - this.y) * (y - this.y) <= radius * radius) {
            return true;
        }
        return false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }


}
