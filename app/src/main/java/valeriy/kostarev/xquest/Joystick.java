package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by valerik on 29.11.2014.
 */
public class Joystick{

    private int moveX, moveY;
    private Game game;
    private int radius,x,y;
    private Paint paint;
    private boolean running;
    private int id = -1;

    public Joystick(Game game) {
        this.game = game;
        radius = 10*game.kvant;
        x = game.getRealScreenWidth() - radius;
        y = game.getRealScreenHeight() - radius;

        clearPos();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
    }

    public void setPos(float x, float y) {
        moveX = (int) x;
        moveY = (int) y;

        if (!isInside(x, y)) {
            int d = (int) Math.sqrt((moveX - this.x) * (moveX - this.x) + (moveY - this.y) * (moveY - this.y));
            moveX = this.x + (moveX - this.x) * radius / d;
            moveY = this.y + (moveY - this.y) * radius / d;
        }
    }

    public void clearPos() {
        moveX = x;
        moveY = y;
    }

    public void draw(Canvas canvas) {

        if (running) {
            paint.setColor(Color.argb(50, 125, 125, 250));
            canvas.drawCircle(x, y, radius, paint);
        }

        paint.setColor(Color.rgb(125, 125, 250));
        canvas.drawCircle(moveX, moveY, radius/3, paint);

    }

    //Получаем направление движения
    public int getDX() {
        int dX;
        dX = 100 * (moveX - x) / radius;
        return dX;
    }

    public int getDY() {
        int dY = 100 * (moveY - y) / radius;
        return dY;
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
                    id = event.getPointerId(pointerIndex);
                    setRunning(true);
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (running) {
                    for (int i = 0; i < pointerCount; i++) {
                        if (event.getPointerId(i) == id) {
                            setPos(event.getX(i), event.getY(i));
                            game.hero.setSpeed(getDX(), getDY());
                        }
                    }
                }
                break;


            case MotionEvent.ACTION_UP:
                if (event.getPointerId(pointerIndex) == id) {
                    setRunning(false);
                    clearPos();
                    id = -1;
                }

            case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
                if (event.getPointerId(pointerIndex) == id) {
                    id = -1;
                    setRunning(false);
                    clearPos();
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
