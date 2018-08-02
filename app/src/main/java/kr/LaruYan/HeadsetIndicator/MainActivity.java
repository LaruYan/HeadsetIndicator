package kr.LaruYan.HeadsetIndicator;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends Activity {

	Button btnEnableNotif;
	Button btnDisableNotif;
	CheckBox cb_hideLauncher;
	SharedPreferences sharedPrefs;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        btnEnableNotif = (Button)findViewById(R.id.button_enable_notification);
        btnDisableNotif = (Button)findViewById(R.id.button_disable_notification);
        
        cb_hideLauncher = (CheckBox)findViewById(R.id.checkBox_hideFromLauncher);
        
        if(isMyServiceRunning()){
        	btnDisableNotif.setEnabled(true);
			btnEnableNotif.setEnabled(false);
			cb_hideLauncher.setEnabled(true);
        }else{
        	btnEnableNotif.setEnabled(true);
			btnDisableNotif.setEnabled(false);
			cb_hideLauncher.setEnabled(false);
        }
        
        btnEnableNotif.setOnClickListener(new OnClickListener (){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnDisableNotif.setEnabled(true);
				btnEnableNotif.setEnabled(false);
				cb_hideLauncher.setEnabled(true);
				getApplicationContext().startService(
						new Intent(getApplicationContext(),
								HeadsetIndicatorService.class));
				setServiceEnabled(true);
			}
        	
        });
        btnDisableNotif.setOnClickListener(new OnClickListener (){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnEnableNotif.setEnabled(true);
				btnDisableNotif.setEnabled(false);
				cb_hideLauncher.setChecked(false);
				cb_hideLauncher.setEnabled(false);
				getApplicationContext().stopService(
						new Intent(getApplicationContext(),
								HeadsetIndicatorService.class));
				setServiceEnabled(false);
			}
        	
        });
        
        cb_hideLauncher.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton cb, boolean bool) {
				// TODO Auto-generated method stub
					ComponentName componentToDisable =
							  new ComponentName("kr.LaruYan.HeadsetIndicator",
							  "kr.LaruYan.HeadsetIndicator.MainActivity");

					getPackageManager().setComponentEnabledSetting(
							  componentToDisable,
							  bool ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
							  PackageManager.DONT_KILL_APP);
			}});
    }
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (HeadsetIndicatorService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private void setServiceEnabled(boolean isEnabled){
    	if(sharedPrefs == null){
			sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		}
		
    	SharedPreferences.Editor sharedPrefsEditor = sharedPrefs.edit();
		
		//실제 변경할 부분 만들기
		sharedPrefsEditor.putBoolean(HeadsetIndicatorService.PREF_SERVICE_ENABLED, isEnabled);
		
		
		//설정 파일 변경을 '저장'합니다.
		sharedPrefsEditor.commit();
		sharedPrefsEditor = null;
		
		/**
		 * A better approach is to disable or enable the broadcast receivers at runtime.
		 *	That way you can use the receivers you declared in the manifest as passive alarms
		 *	that are triggered by system events only when necessary.
		 */
		/*ComponentName receiver = new ComponentName(getApplicationContext(), BootCompleteReceiver.class);

		PackageManager pm = getApplicationContext().getPackageManager();

		pm.setComponentEnabledSetting(receiver,
		        isEnabled ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		        PackageManager.DONT_KILL_APP);*/
    }
}
