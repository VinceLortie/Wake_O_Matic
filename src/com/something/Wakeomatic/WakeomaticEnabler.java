package com.something.Wakeomatic;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;
import com.something.Wakeomatic.tasks.SoundAlertTask;
import com.something.Wakeomatic.tasks.VibratorTask;

/**
 * The specific implementation of our Android application.
 *
 * Holds on to all state information.
 *
 * <p/>
 * Created by Vincent on 28/06/14.
 */
public class WakeomaticEnabler
{
   private static final String TAG = "WakeomaticEnabler";

   //Gotta keep the appp awake if we want to wake anyone up
   private PowerManager.WakeLock wakelock;

   //Noise maker task
   private SoundAlertTask noiseMaker;

   //Vibrator task
   private VibratorTask vibrator;

   //Flag indicating whether we are currently making noise or not
   private boolean makingNoise = false;

   //The one and only instance
   private static WakeomaticEnabler instance = null;

   public static WakeomaticEnabler getInstance()
   {
      if(instance == null)
      {
         instance = new WakeomaticEnabler();
      }
      return instance;
   }

   private WakeomaticEnabler()
   {
   }

   public synchronized void alarmOn(Context context)
   {
      Log.i(TAG, "alarmOn() - Let's do this!");
      if(!makingNoise)
      {
         noiseMaker = new SoundAlertTask(context);
         vibrator = new VibratorTask(context);

         PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
         wakelock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
         wakelock.acquire();

         makingNoise = true;
         noiseMaker.execute();
      }
      else
      {
         Log.i(TAG, "alarmOn() - nevermind...");
      }
   }

   public synchronized void alarmOff()
   {
      Log.i(TAG, "alarmOff() - Show's over!");
      if(makingNoise)
      {
         makingNoise = false;

         noiseMaker.stop();
         noiseMaker = null;

         vibrator.stop();
         vibrator = null;

         wakelock.release();
         wakelock = null;
      }
      else
      {
         Log.i(TAG, "alarmOff() - just kidding...");
      }
   }
}
