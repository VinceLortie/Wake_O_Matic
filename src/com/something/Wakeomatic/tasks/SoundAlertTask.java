package com.something.Wakeomatic.tasks;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.util.Log;
import com.something.Wakeomatic.R;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * And AsyncTask whose purpose is to periodically play an increasingly louder sound until cancelled.
 *
 * <p/>
 * Created by Vincent on 28/06/14.
 */
public class SoundAlertTask extends AsyncTask<Void, Void, Void>
{
   private static final String TAG = "SoundAlertTask";

   private static final float INITIAL_VOLUME = 0.1f;
   private static final float MAX_VOLUME = 1.0f;

   private static final long INTERVAL = 3000; //in ms
   private static final float MAGNITUDE_INCREASE = 0.1f;

   private float currentVolume = INITIAL_VOLUME;

   private AtomicBoolean running = new AtomicBoolean(false);
   private AtomicBoolean soundLoaded = new AtomicBoolean(false);

   private SoundPool soundPool;
   private final int soundId;

   private final Context context;

   public SoundAlertTask(Context aContext)
   {
      context = aContext;
      soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
      soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener()
      {
         @Override
         public void onLoadComplete(SoundPool soundPool, int sampleId, int status)
         {
            soundLoaded.set(true);
         }
      });
      soundId = soundPool.load(context, R.raw.dive_horn_once_only, 1);
   }

   @Override
   protected void onPreExecute()
   {
      if(running.get())
      {
         throw new IllegalStateException("Whoa there cowboy, you already started this bad boy up: don't get greedy.");
      }

      running.set(true);
   }

   @Override
   protected Void doInBackground(Void... params)
   {
      while(running.get())
      {
         try
         {
            Log.i(TAG, "Yup, making noise at volume: " + currentVolume);
            if(soundLoaded.get())
            {
               soundPool.play(soundId, currentVolume, currentVolume, 1, 0, 1.0f);
            }

            if(currentVolume < MAX_VOLUME)
            {
               currentVolume += MAGNITUDE_INCREASE;
            }

            if(currentVolume > MAX_VOLUME)
            {
               currentVolume = MAX_VOLUME;
            }

            Thread.sleep(INTERVAL);
         }
         catch(InterruptedException iex)
         {
            Log.w(TAG, "Someone rudely interrupted us... let's be gentlemen about it and ignore them", iex);
         }
      }

      //Reset volume
      currentVolume = INITIAL_VOLUME;

      return null;
   }

   @Override
   protected void onCancelled()
   {
      stop();
   }

   public void stop()
   {
      running.set(false);
   }
}
