package com.futurice.rctaudiotoolkit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
//import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.Binder;
import android.support.v4.app.NotificationCompat;
//import android.util.Log;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.net.Uri;
import android.content.ContextWrapper;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.io.IOException;
import java.io.File;
import java.lang.Thread;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import  java.util.Timer;
import  java.util.TimerTask;
import android.os.Handler;
import android.content.Intent;
import android.provider.Settings;
import android.widget.Toast;
import android.widget.Toast;
import android.app.Activity;
 
public class AudioPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {
    private static Boolean isRunning = false;

    private static final String ACTION_PLAY = "PLAY";
    private static AudioPlayerService mInstance = null;

    private MediaPlayer mMediaPlayer = null;    // The Media Player
    

    NotificationManager mNotificationManager;
    Notification mNotification = null;
    final int NOTIFICATION_ID = 1;


    // indicates the state our service:
    enum State {
        Retrieving, // the MediaRetriever is retrieving music
        Stopped, // media player is stopped and not prepared to play
        Preparing, // media player is preparing...
        Playing, 
        Paused
        //playback paused (media player ready!)
    };

    State mState = State.Retrieving;

    @Override
    public void onCreate() {
        mInstance = this;
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public AudioPlayerService getServerInstance() {
            return AudioPlayerService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_PLAY)) {
            setUpAsForeground("Playing audio");
            isRunning = true;
        }
        return START_STICKY;
    }

    public static MediaPlayer createMediaPlayer() {
        MediaPlayer mMediaPlayerNew = new MediaPlayer(); // initialize it here
        return mMediaPlayerNew;
    }

    

    /** Called when MediaPlayer is ready */
    @Override
    public void onPrepared(MediaPlayer player) {
        
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

   

    public static AudioPlayerService getInstance() {
        return mInstance;
    }

    

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        
    }

    

    /**
     * Configures service as a foreground service. A foreground service is a service that's doing something the user is
     * actively aware of (such as playing music), and must appear to the user as a notification. That's why we create
     * the notification here.
     */
    void setUpAsForeground(String text) {
        
        mNotification = new Notification();
        mNotification.tickerText = text;
        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        startForeground(NOTIFICATION_ID, mNotification);
    }

    public static boolean isRunning() {
        return AudioPlayerService.isRunning;
    }
}
