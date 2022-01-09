package iss.workshop.ca;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;

public class MusicManager {
    private static final String TAG = "MusicManager";
    public static final int MUSIC_PREVIOUS = -1;
    public static final int MUSIC_BACKGROUND = 0;
    public static final int MUSIC_GAMEPLAY =1;

    private static HashMap<Integer, MediaPlayer> players = new HashMap<Integer, MediaPlayer>();
    private static int currentMusic = -1;
    private static int previousMusic =-1;

    public static void start(Context context, int music){
        start(context, music, false);
    }

    public static void start(Context context, int music, boolean force){
        if (!force && currentMusic>-1 || currentMusic==music){
            // already playing some music and not forced to change music
            //already playing this music
            return;
        }
        if (music==MUSIC_PREVIOUS){
            Log.d(TAG, "using previous music [" +previousMusic +"]");
            music = previousMusic;
        }

        if (currentMusic != -1){
            previousMusic = currentMusic;
            Log.d(TAG, "previous music was ["+ previousMusic+"]");
            //playing some other music, pause it and change;
            pause();
        }
        currentMusic = music;
        Log.d(TAG, "current music is now ["+currentMusic+"]");
        MediaPlayer mp = players.get(music);
        if (mp != null){
            if (!mp.isPlaying()){
                mp.start();
            }
        }
        else{
            if (music == MUSIC_BACKGROUND){
                mp = MediaPlayer.create(context, R.raw.background);
            }
            else if (music == MUSIC_GAMEPLAY){
                mp = MediaPlayer.create(context, R.raw.background);
            }
            else {
                Log.e(TAG, "unsupported music number:" + music);
                return;
            }
            players.put(music,mp);
            try{
                mp.setLooping(true);
                mp.start();
            }
            catch(Exception e){
                Log.e(TAG, e.getMessage(),e);
            }
        }

    }

    public static void pause(){
        Collection<MediaPlayer> mps = players.values();
        for (MediaPlayer p:mps){
            if (p.isPlaying()){
                p.pause();
            }
        }
        if (currentMusic != -1){
            previousMusic=currentMusic;
            Log.d(TAG,"Previous music was [" +previousMusic+"]");

        }
        currentMusic = -1;
        Log.d(TAG, "Current music is now ["+ currentMusic+"]");
    }

    public static void release(){
        Log.d(TAG, "Releasing media players");
        Collection<MediaPlayer> mps = players.values();
        for (MediaPlayer mp:mps){
            try{
                if (mp!=null){
                    if (mp.isPlaying()){
                        mp.stop();
                    }
                    mp.release();
                }
            }
            catch(Exception e){
                Log.e(TAG, e.getMessage(), e);
            }
            mps.clear();
            if (currentMusic != -1){
                previousMusic = currentMusic;
                Log.d(TAG, "Previous music was [" + previousMusic+"]");

            }
            currentMusic =-1;
            Log.d(TAG, "Current music is now [" +currentMusic +"]");
        }
    }
}
