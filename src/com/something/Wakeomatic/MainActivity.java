package com.something.Wakeomatic;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity
{
   /**
    * Called when the activity is first created.
    */
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
   }

   @Override
   protected void onResume()
   {
      super.onResume();

      WakeomaticEnabler.getInstance().alarmOff();
   }

   @Override
   protected void onPause()
   {
      super.onPause();

      finish();
   }
}
