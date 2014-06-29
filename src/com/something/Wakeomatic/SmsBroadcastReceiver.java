package com.something.Wakeomatic;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Boradcast receiver registered to be called when an SMS comes in.
 * <p/>
 * Created by Vincent on 28/06/14.
 */
public class SmsBroadcastReceiver extends BroadcastReceiver
{
   private static final String SMS_INTENT_ACTION = "android.provider.Telephony.SMS_RECEIVED";

   private static final String TRIGGER_MESSAGE = "boom";

   private static final int NOTIF_ID = 8181;

   @Override
   public void onReceive(Context context, Intent intent)
   {
      if(SMS_INTENT_ACTION.equals(intent.getAction()) &&
            isTriggerMessage(intent))
      {
         WakeomaticEnabler.getInstance().alarmOn(context);
         makeNotification(context);
      }
   }

   private boolean isTriggerMessage(Intent intent)
   {
      Bundle bundle = intent.getExtras();
      if (bundle != null)
      {
         Object[] pdus = (Object[]) bundle.get("pdus");
         SmsMessage[] msgs = new SmsMessage[pdus.length];
         for(int i=0; i < msgs.length; i++)
         {
            msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
            //If ever we care where the message came from
            //msg_from = msgs[i].getOriginatingAddress();
            String msgBody = msgs[i].getMessageBody();

            if(msgBody != null && msgBody.contains(TRIGGER_MESSAGE))
            {
               return true;
            }
         }
      }
      return false;
   }

   private void makeNotification(Context context)
   {
      Intent mainActivityIntent = new Intent(context, MainActivity.class);
      PendingIntent launchAppIntent = PendingIntent.getActivity(
            context, 1, mainActivityIntent,  Intent.FLAG_ACTIVITY_NEW_TASK);

      Notification.Builder builder = new Notification.Builder(context);
      builder.setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("BOOM!")
            .setContentText("You gone dun did it now...")
            .setContentIntent(launchAppIntent);
      Notification notification = builder.build();
      NotificationManager notificationManager =
            (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
      notificationManager.notify(NOTIF_ID, notification);
   }
}
