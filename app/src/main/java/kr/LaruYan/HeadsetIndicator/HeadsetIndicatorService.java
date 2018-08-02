package kr.LaruYan.HeadsetIndicator;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class HeadsetIndicatorService extends Service {

	public static final boolean isDebug = true;
	public static final String TAG_LOG = "Service";
	public static final String PREF_SERVICE_ENABLED = "HeadsetIndicatorServiceEnabled";
	static final int notificationId = 1;
	
	HeadsetPlugReceiver receiver;
	NotificationManager mNotificationManager;
	PendingIntent pendingIntent;
	SharedPreferences sharedPrefs;
	
	@Override
	public void onCreate(){
		super.onCreate();
		if(receiver == null){
			receiver = new HeadsetPlugReceiver();
		}
		registerReceiver(receiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));

		if(sharedPrefs == null){
			if(isDebug){
				Log.v(TAG_LOG,"DefaultSharedPreferences is NULL. try to get again");
			}
			sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		}
		
		if( ! sharedPrefs.getBoolean(PREF_SERVICE_ENABLED, false)){
			stopSelf();
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if(intent != null){
			issueNotification(intent.getIntExtra("state", 0), intent.getIntExtra("microphone", 0));
			if(isDebug){
				Log.i("Service","pluggedin: "+
				intent.getIntExtra("state", 0)+
				" / has microphone: "+
				intent.getIntExtra("microphone", 0)
				);
			}
		}
		if (isDebug){
			Log.i("Service","onStartCommand()");
		}
		return START_STICKY;
	}
	
	void issueNotification(int state, int microphone){
		
		if(mNotificationManager == null){
		mNotificationManager =
			    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		}
		if(state == 1){
			if (pendingIntent == null){
				pendingIntent = PendingIntent.getActivity(getApplicationContext(), notificationId, new Intent(Settings.ACTION_SOUND_SETTINGS), PendingIntent.FLAG_UPDATE_CURRENT);
			}
			NotificationCompat.Builder mBuilder =
			        new NotificationCompat.Builder(this)
			        .setSmallIcon(microphone == 1 ? R.drawable.ic_stat_notify_headset : R.drawable.ic_stat_notify_headphone)
			        .setContentTitle(getString(microphone == 1 ? R.string.headset_plugged_in : R.string.headphone_plugged_in))
			        .setContentText(getString(R.string.tap_to_open_sound_settings))
			        .setOngoing(true)
			        .setAutoCancel(false)
			        .setContentIntent(pendingIntent);
			
			// Creates an explicit intent for an Activity in your app
			//mBuilder.setContentIntent(new Intent(this, ResultActivity.class));
			// mId allows you to update the notification later on.
			mNotificationManager.notify(notificationId, mBuilder.build());
		}else{
			mNotificationManager.cancel(notificationId);
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		if(isDebug){
			Log.i("Service","onDestroy()");
		}
		mNotificationManager.cancel(notificationId);
		try
        {
            if(receiver != null)
            {
                unregisterReceiver(receiver);
            }
            
        }
        catch(IllegalArgumentException e)
        {
            // eat this exception.
        }
	}
}
