package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Created by valerik on 20.12.2014.
 */
public class GameClickable {

    protected boolean running = false;
    protected int x, y, radius, action, width, height, textX, textY;
    protected Game game;
    protected int id = -1;
    protected RectF rect;
    protected String text;
    protected Paint paint, paintActive, paintText;

    public GameClickable(Game game) {
        this.game = game;
        paint = new Paint();
        width = 15*game.kvant;
        height = 8*game.kvant;
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(20, 125, 125, 250));
        paint.setTextSize(height / 2);

        paintActive = new Paint();
        paintActive.setStyle(Paint.Style.FILL);
        paintActive.setColor(Color.argb(50, 125, 125, 250));

        paintText = new Paint();
        paintText.setColor(Color.WHITE);
        paintText.setTextSize(height / 2);
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
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

                    game.MenuAction(action);

                    if (!isRunning()) {
                        setRunning(true);
                    } else {
                        setRunning(false);
                    }
                }
                break;


            case MotionEvent.ACTION_MOVE:

                break;


            case MotionEvent.ACTION_UP:
                if (event.getPointerId(pointerIndex) == id) {
                    id = -1;
                }

            case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
                if (event.getPointerId(pointerIndex) == id) {
                    id = -1;
                }
                break;

        }


    }

    public void draw(Canvas canvas) {

        if (game.isRunning()) {
            canvas.drawRoundRect(rect, game.kvant, game.kvant, paint);
            canvas.drawText(text, textX, textY, paint);
        } else {
            canvas.drawRoundRect(rect, game.kvant, game.kvant, paintActive);
            canvas.drawText(text, textX, textY, paintText);

        }
    }


    public boolean isInside(float x, float y) {
        return rect.contains((int) x, (int) y);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        textX = x + width / 2 - (int) paint.measureText(text) / 2;
        textY = y + height / 2 - game.kvant + (int) paint.getTextSize() / 2;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
        rect = new RectF(x, y, x + width, y + height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


}
