package kr.LaruYan.HeadsetIndicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HeadsetPlugReceiver extends BroadcastReceiver {
	public static final boolean isDebug = HeadsetIndicatorService.isDebug;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		/*
		 * 
public static final String ACTION_HEADSET_PLUG
Added in API level 1

Broadcast Action: Wired Headset plugged in or unplugged.

The intent will have the following extra values:

    state - 0 for unplugged, 1 for plugged.
    name - Headset type, human readable string
    microphone - 1 if headset has a microphone, 0 otherwise

Constant Value: "android.intent.action.HEADSET_PLUG"

		 */
		
		if(intent != null){
			Intent headsetIntent = new Intent(context, HeadsetIndicatorService.class);
			headsetIntent.putExtra("state", intent.getIntExtra("state", 0));
			headsetIntent.putExtra("microphone", intent.getIntExtra("microphone", 0));
			if(isDebug){
				Log.i("Receiver","pluggedin: "+
				intent.getIntExtra("state", 0)+
				" / has microphone: "+
				intent.getIntExtra("microphone", 0)
				);
			}
			context.startService(headsetIntent);
		}else{
			if(isDebug){
				Log.i("Receiver","Intent is NULL.");
			}
		}
	}

}
