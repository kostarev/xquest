package valeriy.kostarev.xquest;

import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.util.Hashtable;

/**
 * Created by valerik on 09.07.2015.
 */
public class Sound implements SoundPool.OnLoadCompleteListener {
    private static Sound instance;
    public SoundPool sp;
    private Hashtable<String, Integer> sounds;
    private Game game;
    private int maxDistance, distanceD;

    //Singleton
    public static synchronized Sound getInstance(Game game) {
        if (instance == null) {
            instance = new Sound(game);
        }
        return instance;
    }

    private Sound(Game game) {

        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);
        this.game = game;
        sounds = new Hashtable();
        //Максимальное расстояние в игре (диагональ игрового поля)
        maxDistance = (int) Math.sqrt(game.getGameScreenWidth() * game.getGameScreenWidth() + game.getGameScreenHeight() * game.getGameScreenHeight());
        distanceD = maxDistance / 10;
    }

    //Загружаем звук
    public void load(String key, int fileId) {
        int soundId = sp.load(game.gameView.activity, fileId, 1);
        sounds.put(key, soundId);
    }

    //Проигрываем звук
    public void play(String key, float maxVolume) {
        int soundId = (int) sounds.get(key);
        sp.play(soundId, maxVolume, maxVolume, 0, 0, 1);
    }

    //Проигрываем звук с учетом координат источника
    public void play(String key, float maxVolume, int x, int y) {
        int soundId = (int) sounds.get(key);

        float leftVolume = 1;
        float rightVolume = 1;

        //Расстояние от центра экрана до источника звука
        int yDistance = (int) (game.getGameScreenY0() + game.getRealScreenHeight() / 2 - y);

        //Расстояние от источника звука до левого края экрана
        int leftDistance = Math.abs(x-game.getGameScreenX0());
        //Расстояние от источника звука до правого края экрана
        int rightDistance = Math.abs(x-game.getGameScreenX1());
        leftVolume = (float)(1-leftDistance/maxDistance);
        rightVolume = (float)(1-rightDistance/maxDistance);


        //Коэфициент учитывающий удалённость источника звука по Y
        float volumeKoef = (float) (1 - yDistance / maxDistance);

        rightVolume = maxVolume * rightVolume * volumeKoef;
        leftVolume = maxVolume * leftVolume * volumeKoef;

        if (rightVolume < 0) {
            rightVolume = 0;
        } else if (rightVolume > 1) {
            rightVolume = 1;
        }

        if (leftVolume < 0) {
            leftVolume = 0;
        } else if (leftVolume > 1) {
            leftVolume = 1;
        }

        sp.play(soundId, leftVolume, rightVolume, 0, 0, 1);
        Log.i("Sound", "x = " + x + " y = " + y + " leftDistance = " + leftDistance + " rightDistance = " + rightDistance + " mD=" + maxDistance+" rightVolume="+rightVolume+" leftVolume="+leftVolume);

    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1) {

    }
}
