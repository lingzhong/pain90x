package exerciseManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

public class PunishmentReceiver extends BroadcastReceiver {

    private static final String TAG = "Lab-UserInterface";

    @Override
    public void onReceive(Context context, Intent intent) {
        // check if there are undone task(s)
        if (!ToDoManagerActivity.mAdapter.areAllItemsDone()) {
            punishSMS(context, intent);
        }
    }

    private void punishTest(Context context, Intent intent) {
        String toastMsg = "YOU ARE BEING PUNISHED BECAUSE YOU HAVEN'T COMPLETED THE "
                + intent.getStringExtra(ToDoItem.TITLE) + " TASK!!";
        Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
    }

    private void punishSMS(Context context, Intent intent) {
        // toast
        String toastMsg = "YOU ARE BEING PUNISHED BECAUSE YOU HAVEN'T COMPLETED THE "
                + intent.getStringExtra(ToDoItem.TITLE) + " TASK!!";
        Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
        // Send texts
        String phoneNo = "16477081123"; // Put phone number here
        String message = "I have big butts I cannot lie!"; // Put message here
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(context, "SMS sent.", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(context, "SMS failed, please try again.",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void punishFB(Context context, Intent intent) {
        // toast
        String toastMsg = "YOU ARE BEING PUNISHED ON FB BECAUSE YOU HAVEN'T COMPLETED THE "
                + intent.getStringExtra(ToDoItem.TITLE) + " TASK!!";
        Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
        // post on FB
        facebookClient = new Facebook();
        facebookClient.authorize(this, new String[] { "publish_stream",
                "read_stream", "offline_access" }, this);
        String message = "I have big butts I cannot lie!";
        try {
            Bundle parameters = new Bundle();
            parameters.putString("message", message);
            facebookClient.dialog(this, "stream.publish", parameters, this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}