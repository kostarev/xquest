package valeriy.kostarev.xquest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by valerik on 23.11.2014.
 */
public class Game {

    //Константы направления
    public static final byte CRYSTAL = 1;
    public static final byte ASTEROID = 2;
    public static final byte FREEAREA = 3;

    public static final int MENU_EXIT = 0;
    public static final int MENU_NEW_GAME = 1;
    public static final int MENU_MAIN_MENU = 2;
    public static final int MENU_RESUME = 3;
    public static final int MENU_PAUSE = 4;

    public static final int SOUND_SHOT = 0;
    public static final int SOUND_EXPLODE = 1;


    public static final int ACTION_MAIN_MENU = 0;
    public static final int ACTION_GAME = 1;
    private static Game instance;
    public final Joystick joystick;
    public final MainMenu mainMenu;
    private final ButtonFire fireButton;
    private final GameClickable buttonPause;
    private final GameClickable buttonBack;
    private final String WaveString;
    private final String PointsString;
    private final String LivesString;
    private final String CristalsString;
    private final Bitmap stars;
    public Rect screenRect, centerRect, gameScreenRect, winRect;
    public int kvant, panelWidth, elementWidth, setkaWidth, setkaHeight,
            monsterWidth, explosionsMax;
    public GameView gameView;
    public Hero hero;
    public Bitmap monstrBitmap[], shipflare;
    public int[] boomWidth;
    public Bitmap asteroidBitmap[], planetBitmap[], boomBitmap[];
    public Crystal[] crystals;
    public Unit[] monsters;
    public Planet[] planets;
    public Pulsar[] pulsars;
    public Mine[] mines;
    public Bullet[] bullets;
    public Portal portal;
    public byte[][] setka;
    public Border border;
    public MyAnimation crystallAnimation, mineAnimation, pulsarAnimation, dischargeAnimation;
    public Explosion[] explosions;
    public SpawnTimer spawnTimer;
    public BulletTimer bulletTimer;
    public Thread bulletThread;
    public Sound sound;
    private int gameScreenWidth, gameScreenHeight, screenX0,
            screenY0, screenX1, screenY1,
            screenWidth, screenHeight, centerRectWidth, centerRectHeight,
            centerRectX0, centerRectX1, centerRectY0, centerRectY1,
            gameScreenX0, gameScreenY0, gameScreenX1, gameScreenY1,
            realScreenWidth, realScreenHeight,
            points, lives, wave, bombs, crystalsCnt;
    private Paint paint;
    private int action, FPS = 0, fpsCnt = 0;
    private int waitTime;
    private long waitTimeStart, fpsStartTime;
    private boolean running = false;
    private Thread spawnThread;


    private Game(GameView gameView) {
        this.gameView = gameView;

        //Вычисление размеров экрана
        Display display = gameView.activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metricsB = new DisplayMetrics();
        display.getMetrics(metricsB);

        //Реальная ширина экрана
        realScreenWidth = metricsB.widthPixels;
        //Реальная высота экрана
        realScreenHeight = metricsB.heightPixels - gameView.activity.getStatusBarHeight();

        //Размер, относительно которого вычисляем все размеры в игре, для одинаковых пропорций на разных экранах
        kvant = realScreenWidth / 80;
        //для экрана шириной 800px 1квант = 10px

        //Ширина панели управления
        panelWidth = 20 * kvant;

        //ширина игровой области экрана
        screenWidth = realScreenWidth - 2 * panelWidth;
        //Высота игровой области экрана
        screenHeight = realScreenHeight - 2 * panelWidth;

        //Ширина игрового поля = 2 * ширину экрана
        gameScreenWidth = 2 * realScreenWidth;
        //Высота игрового поля = 2 * высоту экрана
        gameScreenHeight = 2 * realScreenHeight;

        //Область в районе которой самолёт при движении не двигает экран
        centerRectWidth = realScreenWidth / 3;
        centerRectHeight = realScreenHeight / 3;

        //Положение игровой области экрана на дисплее
        screenX0 = panelWidth;
        screenY0 = panelWidth;
        screenX1 = screenX0 + screenWidth;
        screenY1 = screenY0 + screenHeight;
        screenRect = new Rect(screenX0, screenY0, screenX1, screenY1);

        centerRectX0 = realScreenWidth / 2 - centerRectWidth / 2;
        centerRectY0 = realScreenHeight / 2 - centerRectHeight / 2;
        centerRectX1 = centerRectX0 + centerRectWidth;
        centerRectY1 = centerRectY0 + centerRectHeight;
        centerRect = new Rect(centerRectX0, centerRectY0, centerRectX1, centerRectY1);

        //Положение игрового поля относительно дисплея
        gameScreenX0 = -(gameScreenWidth - realScreenWidth) / 2;
        gameScreenX1 = gameScreenX0 + gameScreenWidth;
        gameScreenY0 = -(gameScreenHeight - realScreenHeight) / 2;
        gameScreenY1 = gameScreenY0 + gameScreenHeight;
        gameScreenRect = new Rect(gameScreenX0, gameScreenY0, gameScreenX1, gameScreenY1);

        paint = new Paint();

        //Выхлопной огонь
        Bitmap tmpShipflare = BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.shipflare);
        int hw = tmpShipflare.getHeight() / tmpShipflare.getWidth();
        shipflare = Bitmap.createScaledBitmap(tmpShipflare, kvant * 4, kvant * 4 * hw, true);


        //Ширина кристала
        elementWidth = 3 * kvant;

        //Звёздное небо
        stars = BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.stars);

        //Астеройды
        asteroidBitmap = new Bitmap[4];
        asteroidBitmap[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.asteroid), elementWidth, elementWidth, true);
        asteroidBitmap[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.smallasteroid1), elementWidth, elementWidth, true);
        asteroidBitmap[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.smallasteroid2), elementWidth, elementWidth, true);
        asteroidBitmap[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.smallasteroid3), elementWidth, elementWidth, true);

        //Планеты
        Random r = new Random();
        planetBitmap = new Bitmap[3];
        int planetWidth = r.nextInt(20 * kvant) + 15 * kvant;
        planetBitmap[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.planet1), planetWidth, planetWidth, true);
        planetWidth = r.nextInt(20 * kvant) + 15 * kvant;
        planetBitmap[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.planet2), planetWidth, planetWidth, true);
        planetWidth = r.nextInt(20 * kvant) + 15 * kvant;
        planetBitmap[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.planet3), planetWidth, planetWidth, true);


        //Анимация кристалла
        Bitmap crystalBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.crystallr), elementWidth * 5, elementWidth * 5, true);
        crystallAnimation = new MyAnimation(crystalBitmap, elementWidth, elementWidth, 10);
        crystallAnimation.setRunning(true);

        //Анимация мины
        Bitmap mineBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.mine), elementWidth * 3, elementWidth * 2, true);
        mineAnimation = new MyAnimation(mineBitmap, elementWidth, elementWidth, 10);
        mineAnimation.setRunning(true);

        //Анимация пульсара
        int pulsarWidth = 5 * kvant;
        Bitmap pulsarBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.pulsar), pulsarWidth * 8, pulsarWidth * 4, true);
        pulsarAnimation = new MyAnimation(pulsarBitmap, pulsarWidth, pulsarWidth, 10);
        pulsarAnimation.setRunning(true);

        //Анимация взрывов
        explosionsMax = 50;

        boomWidth = new int[2];
        boomWidth[0] = 2 * kvant;
        boomWidth[1] = 8 * kvant;

        boomBitmap = new Bitmap[2];
        for (int i = 0; i <= 1; i++) {
            boomBitmap[i] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.explosion1), 4 * boomWidth[i], 4 * boomWidth[i], true);
        }

        //Порталы (сразу обе стороны)
        portal = new Portal(this);
        spawnTimer = new SpawnTimer(this);

        //Монстры
        monsterWidth = 3 * kvant;
        monstrBitmap = new Bitmap[3];
        monstrBitmap[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.monstr1), monsterWidth, monsterWidth, true);
        monstrBitmap[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.monstr2), monsterWidth, monsterWidth, true);
        monstrBitmap[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(gameView.activity.getResources(), R.drawable.monstr3), monsterWidth, monsterWidth, true);

        //Рамка поля
        border = new Border(this);

        //Ширина игровой сетки (в ячейках)
        setkaWidth = gameScreenWidth / elementWidth;
        setkaHeight = gameScreenHeight / elementWidth;


        WaveString = gameView.activity.getBaseContext().getString(R.string.Wave);
        PointsString = gameView.activity.getBaseContext().getString(R.string.Points);
        LivesString = gameView.activity.getBaseContext().getString(R.string.Lives);
        CristalsString = gameView.activity.getBaseContext().getString(R.string.Crystals);

        //Инициализация главного меню
        mainMenu = new MainMenu(this);
        mainMenu.add(gameView.activity.getBaseContext().getString(R.string.Resume), MENU_RESUME);
        mainMenu.add(gameView.activity.getBaseContext().getString(R.string.NewGame), MENU_NEW_GAME);
        mainMenu.add(gameView.activity.getBaseContext().getString(R.string.Exit), MENU_EXIT);

        //Скрываем пункт Продолжить
        mainMenu.setHiden(MENU_RESUME, true);


        joystick = new Joystick(this);
        fireButton = new ButtonFire(this);

        buttonPause = new GameClickable(this);
        buttonPause.setAction(MENU_PAUSE);
        buttonPause.setXY(getRealScreenWidth() / 2 - buttonPause.getWidth() / 2, getRealScreenHeight() - buttonPause.getHeight() - 2 * kvant);
        buttonPause.setText(gameView.activity.getBaseContext().getString(R.string.Pause));

        buttonBack = new GameClickable(this);
        buttonBack.setAction(MENU_MAIN_MENU);
        buttonBack.setXY(2 * kvant, 2 * kvant);
        buttonBack.setText(gameView.activity.getBaseContext().getString(R.string.Menu));

        bulletTimer = new BulletTimer(this);
        bulletThread = new Thread(bulletTimer);

        //Загрузка звуков.
        sound = Sound.getInstance(this);
        sound.load("shot", R.raw.shot);
        sound.load("explode", R.raw.explode);
        sound.load("crystall", R.raw.crystall);

        //Запуск новой игры
        newGame();
    }

    public static synchronized Game getInstance(GameView gameView) {
        if (instance == null) {
            instance = new Game(gameView);
        }
        return instance;
    }

    //Создание пули
    public void newBullet(int owner, float x, float y, float speedX, float speedY, int color, int radius) {
        //Создание пули на экране
        for (int i = 0; i < bullets.length; i++) {
            if (bullets[i] == null) {
                bullets[i] = new Bullet(this, owner, i, x, y, speedX, speedY, color, radius);
                break;
            }
        }
    }

    public void wait(int time) {
        waitTime = time;
        waitTimeStart = System.currentTimeMillis();
    }

    public boolean isWaiting() {
        return (System.currentTimeMillis() < waitTimeStart + waitTime);
    }

    public void newGame() {
        //Наш корабль
        hero = new Hero(this);
        //Жизни
        lives = 3;
        //Очки
        points = 0;
        //Волна (уровень)
        wave = 1;
        //Бомбы
        bombs = 3;
        //Инициализация волны
        initWave();
    }

    //Создание взрыва
    public void newExplode(float x, float y, int size) {
        for (int i = 0; i < explosionsMax; i++) {
            if (explosions[i] == null) {
                explosions[i] = new Explosion(this, i, x, y, size);
                break;
            }
        }
    }

    public void initWave() {


        explosions = new Explosion[explosionsMax];

        //Сетка игрового экрана
        setka = new byte[setkaWidth][setkaHeight];
        //Заполняем свободную от объектов зону
        for (int j = 0; j < 10; j++) {
            for (int i = setkaWidth / 2 - 10; i < setkaWidth / 2 + 10; i++) {
                setka[i][j] = FREEAREA;
            }
        }
        for (int j = setkaHeight / 2 - 10; j < setkaHeight / 2 + 10; j++) {
            for (int i = setkaWidth / 2 - 5; i < setkaWidth / 2 + 5; i++) {
                setka[i][j] = FREEAREA;
            }
        }

        //Кристалы
        crystalsCnt = 14 + wave;
        crystals = new Crystal[crystalsCnt];
        for (int i = 0; i < crystalsCnt; i++) {
            crystals[i] = new Crystal(this, i);
        }

        //Мины
        int max = 9 + wave;
        mines = new Mine[max];
        for (int i = 0; i < max; i++) {
            mines[i] = new Mine(this, i);
        }

        //Планеты
        int planetsMax = 4;
        planets = new Planet[planetsMax];
        for (int i = 0; i < planetsMax; i++) {
            planets[i] = new Planet(this, i);
        }

        //Пульсары
        pulsars = new Pulsar[planetsMax];
        for (int i = 0; i < planetsMax; i++) {
            pulsars[i] = new Pulsar(this, i);
        }

        //Монстры
        monsters = new Unit[0];
        spawnThread = new Thread(spawnTimer);
        try {
            spawnThread.start();
        } catch (Exception e) {
            Log.i("Game - initXY -", e.toString());
        }

        //Очищаем массив пуль
        int bulletsMax = 512;
        bullets = new Bullet[bulletsMax];

        //Положение игрового поля относительно дисплея
        gameScreenX0 = -(gameScreenWidth - realScreenWidth) / 2;
        gameScreenX1 = gameScreenX0 + gameScreenWidth;
        gameScreenY0 = -(gameScreenHeight - realScreenHeight) / 2;
        gameScreenY1 = gameScreenY0 + gameScreenHeight;
        gameScreenRect.set(gameScreenX0, gameScreenY0, gameScreenX1, gameScreenY1);

        hero.initXY();
        //Закрываем проход
        border.setOpened(false);
    }


    public void draw(Canvas canvas) {
        //Очищаем экран
        canvas.drawColor(Color.BLACK);

        paint.setAlpha(255);
        //Звёзды
        for (int y = gameScreenY0 - panelWidth; y < gameScreenHeight + 2 * panelWidth; y = y + stars.getHeight()) {
            for (int x = gameScreenX0 - panelWidth; x < gameScreenWidth + 2 * panelWidth; x = x + stars.getWidth()) {
                canvas.drawBitmap(stars, x, y, paint);
            }
        }

        canvas.drawBitmap(stars, gameScreenX0 - panelWidth, gameScreenY0 - panelWidth, paint);

        //Пульсары на заднем фоне
        for (Pulsar pulsar : pulsars) {
            pulsar.draw(canvas);
        }

        //Планеты на заднем фоне
        for (Planet planet : planets) {
            planet.draw(canvas);
        }

        //Рамка
        border.draw(canvas);

        //Порталы
        portal.draw(canvas);

        //Кристалы
        for (Crystal crystal : crystals) {
            if (crystal != null) {
                crystal.draw(canvas);
            }
        }

        //Мины
        for (Mine mine : mines) {
            if (mine != null) {
                mine.draw(canvas);
            }
        }

        //Монстры
        for (Unit unit : monsters) {
            if (unit != null) {
                unit.draw(canvas);
            }
        }


        //Пули
        for (Bullet bullet : bullets) {
            if (bullet != null) {
                bullet.draw(canvas);
            }
        }

        //Рисуем корабль
        hero.draw(canvas);

        //Взрывы
        for (int i = 0; i < explosionsMax; i++) {
            if (explosions[i] != null) {
                explosions[i].draw(canvas);
            }
        }


        //Очки, жизни, инфа
        paint.setColor(Color.argb(100, 180, 180, 180));
        paint.setTextSize(2 * kvant);
        canvas.drawText(WaveString + " " + wave, realScreenWidth - panelWidth + 2 * kvant, 2 * kvant, paint);
        canvas.drawText(CristalsString + " " + crystalsCnt, realScreenWidth - panelWidth + 2 * kvant, 6 * kvant, paint);
        canvas.drawText(PointsString + " " + points, realScreenWidth - panelWidth + 2 * kvant, 10 * kvant, paint);
        canvas.drawText(LivesString + " " + lives, realScreenWidth - panelWidth + 2 * kvant, 14 * kvant, paint);

        switch (action) {
            case ACTION_MAIN_MENU:
                mainMenu.draw(canvas);
                break;
            case ACTION_GAME:
                if (!isWaiting()) {
                    joystick.draw(canvas);
                    fireButton.draw(canvas);
                    buttonPause.draw(canvas);
                    buttonBack.draw(canvas);
                }
                break;
        }

        canvas.drawText("FPS: " + FPS(), realScreenWidth - panelWidth + 2 * kvant, 18 * kvant, paint);

        if (hero.getBulletType() != Hero.BULLET_NORMAL) {
            String text = hero.getBulletType() + " : " + (int) (bulletTimer.getTime() / 1000);
            canvas.drawText(text, realScreenWidth - panelWidth + 2 * kvant, 22 * kvant, paint);
        }
    }

    private int FPS() {
        fpsCnt++;
        if (fpsStartTime + 1000 < System.currentTimeMillis()) {
            fpsStartTime = System.currentTimeMillis();
            FPS = fpsCnt;
            fpsCnt = 0;
        }
        return FPS;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getCenterRectWidth() {
        return centerRectWidth;
    }

    public int getCenterRectHeight() {
        return centerRectHeight;
    }

    public int getGameScreenWidth() {
        return gameScreenWidth;
    }

    public int getGameScreenHeight() {
        return gameScreenHeight;
    }

    public int getScreenX0() {
        return screenX0;
    }

    public int getScreenY0() {
        return screenY0;
    }

    public int getScreenX1() {
        return screenX1;
    }

    public int getScreenY1() {
        return screenY1;
    }

    public int getGameScreenX0() {
        return gameScreenX0;
    }

    public void setGameScreenX0(int x0) {
        gameScreenX0 = x0;
        gameScreenX1 = x0 + gameScreenWidth;
    }

    public int getGameScreenY0() {
        return gameScreenY0;
    }

    public void setGameScreenY0(int y0) {
        gameScreenY0 = y0;
        gameScreenY1 = y0 + gameScreenHeight;
    }

    public int getGameScreenX1() {
        return gameScreenX1;
    }

    public int getGameScreenY1() {
        return gameScreenY1;
    }

    public int getCenterRectX0() {
        return centerRectX0;
    }

    public int getCenterRectX1() {
        return centerRectX1;
    }

    public int getCenterRectY0() {
        return centerRectY0;
    }

    public int getCenterRectY1() {
        return centerRectY1;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void incPoints(int addPoints) {
        points = points + addPoints;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getRealScreenWidth() {
        return realScreenWidth;
    }

    public int getRealScreenHeight() {
        return realScreenHeight;
    }

    public void decLives() {
        lives--;
    }

    public int getWave() {
        return wave;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public int getBombs() {
        return bombs;
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
    }

    public int getCrystalsCnt() {
        return crystalsCnt;
    }

    public void decCrystalsCnt() {
        crystalsCnt--;
        //открываем проход на следующий уровень
        if (crystalsCnt == 0) {
            border.setOpened(true);
        }
    }

    public void initNewWave() {
        wave++;
        initWave();
        joystick.setRunning(false);
        joystick.clearPos();
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }


    //Действия по нажатию кнопок меню
    public void MenuAction(int action) {
        switch (action) {
            case MENU_EXIT:
                Log.i("Game MenuAction", "MENU_EXIT");
                gameView.activity.exit();
                break;

            case MENU_NEW_GAME:
                Log.i("Game MenuAction", "MENU_NEW_GAME");
                setRunning(true);
                setAction(ACTION_GAME);
                newGame();
                //Открываем пункт меню Продолжить
                mainMenu.setHiden(MENU_RESUME, false);
                break;

            case MENU_MAIN_MENU:
                Log.i("Game MenuAction", "MENU_MAIN_MENU");
                setRunning(false);
                setAction(ACTION_MAIN_MENU);
                break;
            case MENU_RESUME:
                Log.i("Game MenuAction", "MENU_RESUME");
                setAction(ACTION_GAME);
                break;
            case MENU_PAUSE:
                Log.i("Game MenuAction", "MENU_PAUSE");
                if (!running) {
                    setRunning(true);
                } else {
                    setRunning(false);
                }
                break;
        }

    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }


    public boolean onTouchEvent(MotionEvent event) {
        switch (action) {
            case ACTION_MAIN_MENU:
                mainMenu.onTouchEvent(event);
                break;
            case ACTION_GAME:
                if (running && !isWaiting()) {
                    joystick.onTouchEvent(event);
                    fireButton.onTouchEvent(event);
                }
                buttonPause.onTouchEvent(event);
                buttonBack.onTouchEvent(event);
                break;

        }
        return true;
    }
}
