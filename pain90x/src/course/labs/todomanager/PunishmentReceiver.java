package course.labs.todomanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class PunishmentReceiver extends BroadcastReceiver {
	
	private static final String TAG = "Lab-UserInterface";
	
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
    	Log.i(TAG, "Entering onReceive()");
    	// check if there are undone task(s)
    	if(! ToDoManagerActivity.mAdapter.areAllItemsDone()) {
    		punishSMS();
    	}
    }
    
    private void punish(Context context, Intent intent) {
    	String toastMsg = "YOU ARE BEING PUNISHED BECAUSE YOU HAVEN'T COMPLETED THE "
    			+ intent.getStringExtra(ToDoItem.TITLE) + " TASK!!";
    	Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
    }
	
	private void punishSMS(){
            //Send texts
            String phoneNo = "<Phone number here>"; //Put phone number here
            String message = "<Insulting message here>"; //Put message here
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
                Toast.makeText(getApplicationContext(), "SMS sent.",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "SMS failed, please try again.",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
}