package valeriy.kostarev.xquest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;


public class GameActivity extends Activity {


    public Game game;
    public GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "onCreate");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Устанавливаем альбомную ориентацию экрана
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        gameView = new GameView(this);
        game = gameView.game;
        setContentView(gameView);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("GameActivity", "onDestroy");
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("GameActivity", "onResume");
        //Устанавливаем альбомную ориентацию экрана
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        gameView = new GameView(this);
        setContentView(gameView);
    }

    @Override
    public void onPause(){
        super.onPause();
        game.setRunning(false);
        Log.i("GameActivity", "onPause");
    }

    //Высота statusBar
    public int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void exit(){
        super.onDestroy();
        System.exit(0);
    }

    public void MainMenu() {
        Log.i("GameActivity", "MainMenu");
        game.setRunning(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("GameActivity", "onKeyDown keyCode=" + keyCode);
                if(keyCode == KeyEvent.KEYCODE_BACK) {
                    if(game.getAction()!=Game.ACTION_MAIN_MENU) {
                        game.MenuAction(Game.MENU_MAIN_MENU);
                    }else{
                        game.MenuAction(Game.MENU_RESUME);
                    }
                    return true;
                }
        return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i("GameActivity", "onKeyUp keyCode="+keyCode);

        return false;
    }
}
