package com.something.Wakeomatic.tasks;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Task that launches the vibrator
 * <p/>
 * Created by Vincent on 28/06/14.
 */
public class VibratorTask extends AsyncTask<Void, Void, Void>
{
   private final Context context;

   public VibratorTask(Context aContext)
   {
      context = aContext;
   }

   @Override
   protected Void doInBackground(Void... params)
   {
      //TODO Vibrate
      return null;
   }

   public void stop()
   {

   }
}
