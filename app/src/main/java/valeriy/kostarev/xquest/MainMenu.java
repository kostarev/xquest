package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Created by valerik on 13.12.2014.
 */
public class MainMenu{

    public Game game;
    private RectF rect;
    private Paint paint;
    private MenuElement[] menuElements;
    public int elHeight, elRad,padding, cnt,width,height,x,y;
    public Paint paintBorder, paintText, paintActiveBorder, paintBackground,paintActiveBackground, paintAciveText;


    public MainMenu(Game game) {
        this.game = game;
        paint = new Paint();
        width = game.getRealScreenWidth()/2;
        elHeight = (game.getRealScreenHeight()-5*game.kvant) / 3;
        height = 3*elHeight;
        x = game.getRealScreenWidth()/2-width/2;
        y = game.getRealScreenHeight()/2 - height/2;
        int textSize = elHeight / 3;

        padding = 0;
        elRad = game.kvant;
        rect = new RectF(x,y,x+width,y+height);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(100);
        paint.setColor(Color.rgb(150, 160, 170));

        paintBorder = new Paint();
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setColor(Color.WHITE);

        paintActiveBorder = new Paint();
        paintActiveBorder.setStyle(Paint.Style.STROKE);
        paintActiveBorder.setColor(Color.WHITE);

        paintText = new Paint();
        paintText.setTextSize(textSize);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setColor(Color.WHITE);

        paintAciveText = new Paint();
        paintAciveText.setTextSize(textSize);
        paintAciveText.setColor(Color.WHITE);

        paintBackground = new Paint();
        paintBackground.setColor(Color.BLACK);
        paintBackground.setStyle(Paint.Style.FILL);

        paintActiveBackground = new Paint();
        paintActiveBackground.setColor(Color.rgb(50,20,60));
        paintActiveBackground.setStyle(Paint.Style.FILL);


        menuElements = new MenuElement[1];
        cnt = 0;
    }

    public void add(String title, int action){
        menuElements[cnt] = new MenuElement(this,cnt,cnt);
        menuElements[cnt].setTitle(title);
        menuElements[cnt].setAction(action);
        cnt++;

        if(cnt>=menuElements.length){
            MenuElement[] tmp = menuElements;
            menuElements = new MenuElement[cnt+1];
            for(int i = 0; i< tmp.length;i++){
                menuElements[i] = tmp[i];
            }
            tmp = null;
        }
    }

    public void setHiden(int menu_action, boolean hidden){
        for(int i = 0; i< menuElements.length;i++){
            if(menuElements[i]!=null && menuElements[i].getAction() == menu_action){
                menuElements[i].setHidden(hidden);
                updatePositions();
            }
        }
    }



    public void updatePositions(){
        int position = 0;
        for(int i = 0; i< menuElements.length;i++){
            if(menuElements[i]!=null && !menuElements[i].isHidden()) {
                menuElements[i].setPosition(position);
                position++;
            }
        }
    }

    public void draw(Canvas canvas) {
        //canvas.drawRoundRect(rect, 10, 10, paint);

        for(int i =0;i<menuElements.length;i++){
            if(menuElements[i]!=null){
                menuElements[i].draw(canvas);
            }
        }

    }

    public void onTouchEvent(MotionEvent event) {
        for(int i =0;i<menuElements.length;i++){
            if(menuElements[i]!=null){
                menuElements[i].onTouchEvent(event);
            }
        }
    }

}
