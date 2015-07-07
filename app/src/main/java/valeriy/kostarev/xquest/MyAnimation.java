package valeriy.kostarev.xquest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by valerik on 29.11.2014.
 */
public class MyAnimation {

    private Bitmap[] frames;
    private long startTime;
    private int FPS, sleepTime;
    private Paint paint;
    private boolean running = false, once;
    public boolean finished = false;
    public int frame, framesCnt;


    public MyAnimation(Bitmap srcImage, int frameW, int frameH, int FPS) {
        this.FPS = FPS;
        int cntY = srcImage.getHeight() / frameH;
        int cntX = srcImage.getWidth() / frameW;
        framesCnt = 0;
        frames = new Bitmap[cntY*cntX];
        for (int i = 0; i < cntY; i++) {
            for(int j = 0; j< cntX; j++){
                frames[framesCnt] = Bitmap.createBitmap(srcImage, j * frameW, i*frameH, frameW, frameH);
                framesCnt++;
            }
        }

        paint = new Paint();

        startTime = System.currentTimeMillis();
        this.sleepTime = 1000 / FPS;
        frame = 0;
    }





    public void setRunning(boolean running) {
        this.running = running;
    }

    public void draw(Canvas canvas, int x, int y) {

        if(running) {
            if (startTime < System.currentTimeMillis() - sleepTime) {
                startTime = System.currentTimeMillis();
                frame++;
                if (frame >= frames.length) {
                    if(once){
                        finished = true;
                        running = false;
                        return;
                    }else{
                        frame = 0;
                    }
                }
            }
        }

        canvas.drawBitmap(frames[frame], x, y, paint);
    }

    public void setOnce(boolean once) {
        this.once = once;
    }
}
