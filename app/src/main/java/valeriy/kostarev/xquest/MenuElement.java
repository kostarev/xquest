package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Created by valerik on 21.12.2014.
 */
public class MenuElement {
    private String title="";
    private MainMenu menu;
    private int id,x0, y0, x1,y1, pointerId, position;
    private float textX,textY;
    private RectF rect;
    private boolean running, hidden;
    private int action;

    public MenuElement(MainMenu menu,int id, int position){
        this.menu = menu;
        this.id = id;
        setPosition(position);
        x0 = menu.x+menu.padding;
        x1 = menu.x+menu.width - menu.padding*2;
        pointerId = -1;
    }

    public void draw(Canvas canvas){
        if(hidden){
            return;
        }
        if(running) {
            canvas.drawRoundRect(rect, menu.elRad, menu.elRad, menu.paintActiveBackground);
            canvas.drawRoundRect(rect, menu.elRad, menu.elRad, menu.paintActiveBorder);
            canvas.drawText(title, textX, textY, menu.paintAciveText);
        }else{
            canvas.drawRoundRect(rect, menu.elRad, menu.elRad, menu.paintBackground);
            canvas.drawRoundRect(rect, menu.elRad, menu.elRad, menu.paintBorder);
            canvas.drawText(title, textX, textY, menu.paintText);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        textX = x0+(x1-x0)/2- menu.paintText.measureText(title) / 2;
        textY = y0+ (y1-y0)/2+menu.paintText.getTextSize()/2;
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

                    if (rect.contains(event.getX(pointerIndex), event.getY(pointerIndex))) {
                        setRunning(true);
                        pointerId = event.getPointerId(pointerIndex);
                    }
                    break;


                case MotionEvent.ACTION_MOVE:
                    setRunning(false);
                    pointerId = -1;
                    for (int i = 0; i < pointerCount; i++) {
                            if(rect.contains(event.getX(i),event.getY(i))){
                                setRunning(true);
                                pointerId = i;
                            }
                    }
                break;


                case MotionEvent.ACTION_UP:
                    if (event.getPointerId(pointerIndex) == pointerId) {
                        pointerId = -1;
                        setRunning(false);
                        menu.game.MenuAction(action);
                    }

                case MotionEvent.ACTION_POINTER_UP: // прерывания касаний
                    if (event.getPointerId(pointerIndex) == pointerId) {
                        pointerId = -1;
                        setRunning(false);
                        menu.game.MenuAction(action);
                    }
                    break;

            }



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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setPosition(int position) {
        this.position = position;
        y0 = menu.y+menu.padding+position*(menu.elHeight)+1;
        y1 = menu.y+menu.padding+(position+1)*(menu.elHeight);
        textX = x0+(x1-x0)/2- menu.paintText.measureText(title) / 2;
        textY = y0+ (y1-y0)/2+menu.paintText.getTextSize()/2;
        rect = new RectF(x0,y0,x1,y1);
    }

    public int getPosition() {
        return position;
    }
}
