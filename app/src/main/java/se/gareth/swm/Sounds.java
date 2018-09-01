package se.gareth.swm;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import java.util.ArrayList;

public class Sounds {

    public static enum Sound {
        Shot1,
            Splash1,
            BreakingWood1,
            Freeze1,
            BreakingGlass1,
            Miss1,
            Fire1,
            Click1,

            LastValue
            };

    private SoundPool soundPool;
    private SparseIntArray soundPoolMap;
    private AudioManager mMgr;
    private final ArrayList<Integer> mStreamList;
    private final GameBase game;

    public Sounds(GameBase gameBase) {
        game = gameBase;
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        soundPoolMap = new SparseIntArray();
        soundPoolMap.put(Sound.Shot1.ordinal(), soundPool.load(game.gameView.getContext(), R.raw.shot1, 1));
        soundPoolMap.put(Sound.Splash1.ordinal(), soundPool.load(game.gameView.getContext(), R.raw.splash1, 1));
        soundPoolMap.put(Sound.BreakingWood1.ordinal(), soundPool.load(game.gameView.getContext(), R.raw.breaking_wood1, 1));
        soundPoolMap.put(Sound.Freeze1.ordinal(), soundPool.load(game.gameView.getContext(), R.raw.freeze1, 1));
        soundPoolMap.put(Sound.BreakingGlass1.ordinal(), soundPool.load(game.gameView.getContext(), R.raw.breaking_glass1, 1));
        soundPoolMap.put(Sound.Miss1.ordinal(), soundPool.load(game.gameView.getContext(), R.raw.miss1, 1));
        soundPoolMap.put(Sound.Fire1.ordinal(), soundPool.load(game.gameView.getContext(), R.raw.fire1, 1));
        soundPoolMap.put(Sound.Click1.ordinal(), soundPool.load(game.gameView.getContext(), R.raw.click1, 1));
        mMgr = (AudioManager)game.gameView.getContext().getSystemService(Context.AUDIO_SERVICE);

        mStreamList = new ArrayList<Integer>(Sound.LastValue.ordinal());
        for (int i = 0; i < Sound.LastValue.ordinal(); i ++) {
            mStreamList.add(0);
        }
    }

    public void loop(Sound sound, int loop) {

        /* Play the sound with the correct volume */
        final float streamVolumeCurrent = mMgr.getStreamVolume(AudioManager.STREAM_MUSIC);
        final float streamVolumeMax = mMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        final float volume = streamVolumeCurrent / streamVolumeMax;
        final int soundIndex = soundPoolMap.get(sound.ordinal());
        int streamId = mStreamList.get(sound.ordinal());
        if (streamId > 0)
            soundPool.stop(streamId);
        streamId = soundPool.play(soundIndex, volume, volume, 1, loop, 1f);
        mStreamList.set(sound.ordinal(), streamId);
    }

    public void stop(Sound sound) {
        int streamId = mStreamList.get(sound.ordinal());
        if (streamId > 0) {
            soundPool.stop(streamId);
            mStreamList.set(sound.ordinal(), 0);
        }
    }

    public void stopAll() {
        for (int i = 0; i < Sound.LastValue.ordinal(); i ++) {
            int streamId = mStreamList.get(i);
            soundPool.stop(streamId);
            mStreamList.set(i, 0);
        }
    }

    public void play(Sound sound) {
        loop(sound, 0);
    }
}
