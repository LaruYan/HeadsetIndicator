package kr.LaruYan.HeadsetIndicator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver  extends BroadcastReceiver{
	public static final boolean isDebug = HeadsetIndicatorService.isDebug;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		context.startService(new Intent(context, HeadsetIndicatorService.class));
		if(isDebug){
			Log.i("Receiver","BootCompleted");
		}
	}

}
