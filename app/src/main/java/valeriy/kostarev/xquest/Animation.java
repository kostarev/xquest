package valeriy.kostarev.xquest;

import android.graphics.Canvas;

import java.util.concurrent.TimeUnit;

/**
 * Created by valerik on 08.08.2015.
 */
 class Animation {

    protected int frames, frame,x1,y1, x0,y0, width, height, timeMS, size0, size1, fps;
    protected boolean running;
    protected float dx,dy,x,y, dsize,size;
    protected Game game;


    protected void init(int timeMS, final int sleepTime, int x0, int y0, int x1, int y1, int size0, int size1) {

        this.x1 = x1;
        this.y1 = y1;
        this.x0 = x0;
        this.y0 = y0;
        this.timeMS = timeMS;
        this.size0 = size0;
        this.size1 = size1;
        this.x = x0;
        this.y = y0;
        this.size = size0;
        setSize(size);

        fps = game.FPS();
        frame = 0;
        //Всего кадров анимации
        frames = timeMS * fps / 1000;
        dx = (x1 - x0) * 1000f / timeMS / fps;
        dy = (y1 - y0) * 1000f / timeMS / fps;
        dsize = (size1 - size0) * 1000f / timeMS / fps;

        running = false;

        //Через промежуток времени запускаем анимацию
        new Thread(new Runnable() {
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepTime);
                } catch (Exception e) {}
                running = true;
            }
        }).start();
    }



    protected void action() {
        if (running && frame < frames) {
            x += dx;
            y += dy;
            size += dsize;
            setSize(size);
            frame++;

            //Окончательная подгонка
            if(frame == frames){
                lastAction();
            }
        }
    }

    protected void setSize(float size){
        this.size = size;
    }

    protected void lastAction(){
        x = x1;
        y = y1;
        size = size1;
        setSize(size);
    }
}
