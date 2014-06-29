package com.something.Wakeomatic;

import android.app.Application;
import android.os.PowerManager;

/**
 * The specific implementation of our Android application.
 *
 * Holds on to all state information.
 *
 * <p/>
 * Created by Vincent on 28/06/14.
 */
public class WakeomaticApplication extends Application
{
   //Gotta keep the appp awake if we want to wake anyone up
   PowerManager.WakeLock wakelock;


}
