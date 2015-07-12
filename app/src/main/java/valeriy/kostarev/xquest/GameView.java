package valeriy.kostarev.xquest;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView {

    public final Game game;
    public final GameActivity activity;
    public SurfaceHolder holder;
    public GameManager gameLoopThread;


    public GameView(final GameActivity activity) {
        super(activity);
        Log.i("GameView", "super");
        this.activity = activity;

        game = Game.getInstance(this);

        gameLoopThread = new GameManager(this);
        holder = getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {
            /** Создание области рисования */
            public void surfaceCreated(SurfaceHolder holder) {
                Log.i("GameView", "surfaceCreated");
                gameLoopThread.setRunning(true);
                try {
                    gameLoopThread.start();
                } catch (Exception e) {
                    Log.i("GameView - surfaceCreated -", e.toString());
                }

            }

            /** Изменение области рисования */
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.i("GameView", "surfaceChanged");
            }

            /** Уничтожение области рисования */
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i("GameView", "surfaceDestroyed");
                boolean retry = true;
                gameLoopThread.setRunning(false);
                gameLoopThread.interrupt();
                while (retry) {
                    try {
                        gameLoopThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
    }


    @Override
    //Обработка касаний экрана
    public boolean onTouchEvent(MotionEvent event) {
        return game.onTouchEvent(event);
    }


    //Отрисовка игрового поля
    @Override
    protected void onDraw(Canvas canvas) {
        try {
            game.draw(canvas);
        } catch (Exception e) {
            Log.i("Ошибка отрисовки ", e.toString());
        }
    }
}