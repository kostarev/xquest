package valeriy.kostarev.xquest;

import android.media.AudioManager;
import android.media.SoundPool;

import java.util.Hashtable;

/**
 * Created by valerik on 09.07.2015.
 */
public class Sound implements SoundPool.OnLoadCompleteListener {
    private static Sound instance;
    public SoundPool sp;
    private Hashtable<String, Integer> sounds;
    private Game game;
    private int maxDistance;

    private Sound(Game game) {

        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);
        this.game = game;
        sounds = new Hashtable();
        //Максимальная дистанция в игре - диагональ игровой области
        maxDistance = (int) Math.sqrt(game.getGameScreenWidth() * game.getGameScreenWidth() + game.getGameScreenHeight() * game.getGameScreenHeight());
    }

    //Singleton
    public static synchronized Sound getInstance(Game game) {
        if (instance == null) {
            instance = new Sound(game);
        }
        return instance;
    }

    //Загрузка звука
    public void load(String key, int fileId) {
        int soundId = sp.load(game.gameView.activity, fileId, 1);
        sounds.put(key, soundId);
    }

    //Проигрывание звука
    public void play(String key, float maxVolume) {
        int soundId = sounds.get(key);
        sp.play(soundId, maxVolume, maxVolume, 0, 0, 1);
    }

    //Проигрывание звука с учетом координат источника
    public void play(String key, float maxVolume, int x, int y) {
        int soundId = sounds.get(key);

        float leftVolume = 1;
        float rightVolume = 1;

        //Расстояние по оси Y до источника звука
        int yDistance = Math.abs(y - Math.abs(Math.abs(game.getGameScreenY0()) + game.getRealScreenHeight() / 2));

        //Расстояние от левой стороны экрана до источника звука
        int leftDistance = Math.abs(x - Math.abs(game.getGameScreenX0()));
        //Расстояние от правой стороны экрана до источника звука
        int rightDistance;
        if (x + game.getGameScreenX0() < 0) {
            rightDistance = leftDistance + game.getRealScreenWidth();
        } else {
            rightDistance = Math.abs(game.getRealScreenWidth() - leftDistance);
        }

        leftVolume = 1f - (float) leftDistance / maxDistance;
        rightVolume = 1f - (float) rightDistance / maxDistance;


        //Коэффициент, учитывающий удалённость по оси Y
        float volumeKoef = 1f - (float) yDistance / maxDistance;

        rightVolume = rightVolume * maxVolume * volumeKoef;
        leftVolume = leftVolume * maxVolume * volumeKoef;

        sp.play(soundId, leftVolume, rightVolume, 0, 0, 1);

    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1) {
    }
}
