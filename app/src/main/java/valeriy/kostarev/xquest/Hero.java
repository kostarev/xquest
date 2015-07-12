package valeriy.kostarev.xquest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by valerik on 30.11.2014.
 */
public class Hero {

    public static final int BULLET_NORMAL = 0;
    public static final int BULLET_2 = 1;
    public static final int BULLET_3 = 2;
    public static final int BULLET_BACK = 3;
    public Rect heroRect, hitRect;
    private int shipWidth, shipHeight, safeZone;
    private float x,y,dx,gameX, gameY, dxInc, dyInc, dy, rotateAngle, fullSpeed;
    private Bitmap image;
    private Paint paint, flarePaint;
    private Game game;
    private boolean hidden;
    private int bulletType;
    private long lastShotTime;
    private int shotSleepTime;


    public Hero(Game game) {
        this.game = game;
        paint = new Paint();
        flarePaint = new Paint();
        shipWidth = 5 * game.kvant;
        shipHeight = 5 * game.kvant;
        initXY();

        setBulletType(BULLET_NORMAL);

        heroRect = new Rect((int)x, (int)y, shipWidth, shipHeight);
        safeZone = game.kvant;
        hitRect = new Rect((int)(x + safeZone), (int)(y + safeZone), shipWidth - safeZone, shipHeight - safeZone);
        image = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(game.gameView.activity.getResources(), R.drawable.ship), shipWidth, shipHeight, true);
    }

    //Инициализация положения корабля на экране
    public void initXY() {
        game.setGameScreenX0(-(game.getGameScreenWidth() - game.getRealScreenWidth()) / 2);
        game.setGameScreenY0(-(game.getGameScreenHeight() - game.getRealScreenHeight()) / 2);
        gameX = game.getGameScreenWidth() / 2 - shipWidth / 2;
        gameY = game.getGameScreenHeight() / 2 - shipHeight / 2;
        x = gameX + game.getGameScreenX0();
        y = gameY + game.getGameScreenY0();
        dx = 0;
        dy = 0;
        flarePaint.setAlpha(0);
        fullSpeed = 0;
        setHidden(false);
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }


    public void draw(Canvas canvas) {
        //Обновляем позицию корабля
        updatePosition();
        if (!hidden) {
            canvas.save();
            canvas.rotate(rotateAngle, x + shipWidth / 2, y + shipHeight / 2);
            canvas.drawBitmap(game.shipflare, x + shipWidth / 2 - game.shipflare.getWidth() / 2, y + fullSpeed * 3, flarePaint);
            canvas.drawBitmap(image, x, y, paint);
            canvas.restore();
        }
    }

    public void updatePosition() {
        if (!game.isRunning() || game.isWaiting()) {
            return;
        }

        dxInc = dx / 10;
        dyInc = dy / 10;
        fullSpeed = (float) Math.sqrt(dxInc * dxInc + dyInc * dyInc);
        flarePaint.setAlpha((int) (255 * fullSpeed / 10));

        gameX = gameX + dxInc;
        gameY = gameY + dyInc;


        //Обработка столкновения с границей игрового экрана
        if (hitRect.intersect(game.border.left) ||
                hitRect.intersect(game.border.right) ||
                hitRect.intersect(game.border.top1) ||
                hitRect.intersect(game.border.top2) ||
                hitRect.intersect(game.border.bottom) ||
                (!game.border.isOpened() && heroRect.intersect(game.border.hole))) {
            killMe();
        } else if (game.border.isOpened() && game.winRect.contains(heroRect)) {
            game.initNewWave();
        }


        //Движение корабля по экрану
        if (dxInc > 0) {
            if (x + dxInc + shipWidth > game.getCenterRectX1() && game.getGameScreenX1() > game.getScreenX1()) {
                game.setGameScreenX0((int)(x - gameX));
            } else {
                x = x + dxInc;
            }
        } else if (dxInc < 0) {
            if (x + dxInc < game.getCenterRectX0() && game.getGameScreenX0() < game.getScreenX0()) {
                game.setGameScreenX0((int)(x - gameX));
            } else {
                x = x + dxInc;
            }
        }

        if (dyInc > 0) {
            if (y + dyInc + shipHeight > game.getCenterRectY1() && game.getGameScreenY1() > game.getScreenY1()) {
                game.setGameScreenY0((int)(y - gameY));
            } else {
                y = y + dyInc;
            }
        } else if (dyInc < 0) {
            if (y + dyInc < game.getCenterRectY0() && game.getGameScreenY0() < game.getScreenY0()) {
                game.setGameScreenY0((int)(y - gameY));
            } else {
                y = y + dyInc;
            }
        }

        if (game.getGameScreenX0() > game.getScreenX0()) {
            game.setGameScreenX0(game.getScreenX0());
        } else if (game.getGameScreenX1() < game.getScreenX1()) {
            game.setGameScreenX0(game.getScreenX1() - game.getGameScreenWidth());
        }

        if (game.getGameScreenY0() > game.getScreenY0()) {
            game.setGameScreenY0(game.getScreenY0());
        } else if (game.getGameScreenY1() < game.getScreenY1()) {
            game.setGameScreenY0(game.getScreenY1() - game.getGameScreenHeight());
        }


        //Проверяем не наехали ли на кристал
        for (Crystal crystal : game.crystals) {
            if (crystal != null) {
                if (heroRect.intersect(crystal.rect)) {
                    game.incPoints(crystal.objectCost);
                    game.decCrystalsCnt();
                    crystal.killMe();
                }
            }
        }

        //Проверяем не наехали ли на мину
        for (Mine mine : game.mines) {
            if (mine != null) {
                if (hitRect.intersect(mine.rect)) {
                    mine.killMe();
                    killMe();
                }
            }
        }


        //Проверяем не наехали ли на врага
        for (Unit unit : game.monsters) {
            if (unit != null) {
                if (unit.intersect()) {
                    unit.hero();
                }
            }
        }


        //Обновляем пямоугольник героя
        heroRect.set((int)x, (int)y, (int)(x + shipWidth), (int)(y + shipHeight));
        hitRect.set((int)(x + safeZone), (int)(y + safeZone), (int)(x + shipWidth - 2 * safeZone), (int)(y + shipHeight - 2 * safeZone));
    }


    //Установка скорости корабля по двум осям
    public void setSpeed(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
        if (dx != 0 || dy != 0) {
            rotateAngle = (float) (Math.atan2(dy, dx) * 180 / Math.PI) + 90;
        }
    }

    //Смерть героя
    public void killMe() {

        //Создаём взрыв
        game.newExplode(gameX + heroRect.width() / 2, gameY + heroRect.height() / 2, 1);
        //Звук взрыва
        game.sound.play("explode", 1f, (int) gameX, (int) gameY);

        game.decLives();
        dxInc = 0;
        dyInc = 0;
        //Задержка перед началом игры
        game.wait(3000);
        setHidden(true);

        //Инициализация координат с задержкой
        class WorkingThread implements Runnable {
            @Override
            public void run() {
                Log.i("Hero", "waiting");
                while (game.isWaiting()) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (Exception e) {

                    }
                }
                Log.i("Hero", "initXY");
                initXY();
            }
        }
        WorkingThread wt = new WorkingThread();
        Thread thread = new Thread(wt);
        thread.start();

        Log.i("Hero", "initXYsleep");
    }


    //Выстрел
    public void shot() {
        //Ограничиваем количество выстрелов во времени
        if (lastShotTime + shotSleepTime > game.time()) {
            return;
        }
        lastShotTime = game.time();

        float x = gameX + shipWidth / 2;
        float y = gameY + shipHeight / 2;
        double angle = Math.PI*(rotateAngle-90)/180;

        switch (bulletType) {
            //Нормальные пули
            case BULLET_NORMAL:
                newBullet(x, y, dxInc, dyInc);
                break;
            //Пули из зада
            case BULLET_BACK:
                newBullet(x, y, dxInc, dyInc);
                //Задние пули
                newBullet(x, y, (float) (fullSpeed * Math.cos(angle+Math.PI)), (float) (fullSpeed * Math.sin(angle+Math.PI)));
                break;
            //2 пули разом вперёд
            case BULLET_2:
                double tmpAngle2 = 0.3;
                newBullet(x, y, (float) (fullSpeed * Math.cos(angle+tmpAngle2)), (float) (fullSpeed * Math.sin(angle+tmpAngle2)));
                newBullet(x, y, (float) (fullSpeed * Math.cos(angle-tmpAngle2)), (float) (fullSpeed * Math.sin(angle-tmpAngle2)));
                break;
            //3 пули разом вперёд
            case BULLET_3:
                newBullet(x, y, dxInc, dyInc);
                double tmpAngle = 0.1;
                newBullet(x, y, (float) (fullSpeed * Math.cos(angle+tmpAngle)), (float) (fullSpeed * Math.sin(angle+tmpAngle)));
                newBullet(x, y, (float) (fullSpeed * Math.cos(angle-tmpAngle)), (float) (fullSpeed * Math.sin(angle-tmpAngle)));
                break;
        }

        //Проигрываем звук выстрела
        game.sound.play("shot", 0.1f);
    }

    //Создание пули на экране
    public void newBullet(float x, float y, float speedX, float speedY) {
        game.newBullet(Bullet.OWNER_HERO, x, y, speedX, speedY, Color.WHITE, 1);
    }


    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getBulletType() {
        return bulletType;
    }

    public void setBulletType(int bulletType) {
        this.bulletType = bulletType;

        //Задержка между выстрелами
        switch(bulletType){
            case BULLET_NORMAL:
                shotSleepTime = 200;
                break;
            case BULLET_2:
                shotSleepTime = 100;
                break;
            case BULLET_3:
                shotSleepTime = 200;
                break;
            case BULLET_BACK:
                shotSleepTime = 300;
                break;
        }
    }
}
