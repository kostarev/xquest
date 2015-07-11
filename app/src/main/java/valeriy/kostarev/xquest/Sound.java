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
    private int maxDistance, distanceD;

    private Sound(Game game) {

        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sp.setOnLoadCompleteListener(this);
        this.game = game;
        sounds = new Hashtable();
        //max distance in game Diagonal of game screen
        maxDistance = (int) Math.sqrt(game.getGameScreenWidth() * game.getGameScreenWidth() + game.getGameScreenHeight() * game.getGameScreenHeight());
        distanceD = maxDistance / 10;
    }

    //Singleton
    public static synchronized Sound getInstance(Game game) {
        if (instance == null) {
            instance = new Sound(game);
        }
        return instance;
    }

    //load sound
    public void load(String key, int fileId) {
        int soundId = sp.load(game.gameView.activity, fileId, 1);
        sounds.put(key, soundId);
    }

    //play sound
    public void play(String key, float maxVolume) {
        int soundId = sounds.get(key);
        sp.play(soundId, maxVolume, maxVolume, 0, 0, 1);
    }

    //play sound with koordinates
    public void play(String key, float maxVolume, int x, int y) {
        int soundId = sounds.get(key);

        float leftVolume = 1;
        float rightVolume = 1;

        //y distance to sound source
        int yDistance = Math.abs(y - Math.abs(Math.abs(game.getGameScreenY0()) + game.getRealScreenHeight() / 2));

        //distance by left side of screen to source
        int leftDistance = Math.abs(x - Math.abs(game.getGameScreenX0()));
        //distance by right side of screen to source
        int rightDistance;
        if (x + game.getGameScreenX0() < 0) {
            rightDistance = leftDistance + game.getRealScreenWidth();
        } else {
            rightDistance = Math.abs(game.getRealScreenWidth() - leftDistance);
        }

        leftVolume = 1f - (float) leftDistance / maxDistance;
        rightVolume = 1f - (float) rightDistance / maxDistance;


        //volume koef of y distance
        float volumeKoef = 1f - (float) yDistance / maxDistance;

        rightVolume = rightVolume * maxVolume * volumeKoef;
        leftVolume = leftVolume * maxVolume * volumeKoef;

        /*
        if (rightVolume < 0) {
            rightVolume = 0;
        } else if (rightVolume > 1) {
            rightVolume = 1;
        }

        if (leftVolume < 0) {
            leftVolume = 0;
        } else if (leftVolume > 1) {
            leftVolume = 1;
        }*/

        sp.play(soundId, leftVolume, rightVolume, 0, 0, 1);

    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int i, int i1) {

    }
}
