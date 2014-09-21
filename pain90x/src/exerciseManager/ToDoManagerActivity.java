package exerciseManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import course.labs.todomanager.R;
import exerciseManager.ToDoItem.Priority;
import exerciseManager.ToDoItem.Status;

public class ToDoManagerActivity extends ListActivity {

    private static final int ADD_TODO_ITEM_REQUEST = 0;

    private static final String FILE_NAME = "TodoManagerActivityData.txt";
    private static final String TAG = "Lab-UserInterface";

    private static final int MENU_DELETE = Menu.FIRST;
    private static final int MENU_DUMP = Menu.FIRST + 1;

    public static ToDoListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new ToDoListAdapter(getApplicationContext());

        getListView().setFooterDividersEnabled(true);

        LayoutInflater inflater = getLayoutInflater();
        TextView footerView = (TextView) inflater.inflate(R.layout.footer_view,
                getListView(), false);

        getListView().addFooterView(footerView);

        footerView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                log("Entered footerView.OnClickListener.onClick()");

                Intent intent = new Intent(ToDoManagerActivity.this,
                        AddToDoActivity.class);
                startActivityForResult(intent, ADD_TODO_ITEM_REQUEST);
            }
        });

        setListAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Create a new ToDoItem from the data Intent
        // and then add it to the adapter
        if (requestCode == ADD_TODO_ITEM_REQUEST && resultCode == RESULT_OK) {
            ToDoItem toDoItem = new ToDoItem(data);
            mAdapter.add(toDoItem);
            addToDoAlarm(toDoItem);
        }
    }

    private void addToDoAlarm(ToDoItem toDoItem) {
        // setting alarm based on new todolist
        AlarmManager alarms = (AlarmManager) this
                .getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, PunishmentReceiver.class);
        ToDoItem.packageIntent(intent, toDoItem.getTitle(), toDoItem
                .getPriority(), toDoItem.getStatus(), toDoItem.getDate()
                .toString());
        PendingIntent operation = PendingIntent
                .getBroadcast(this, 0, intent, 0);
        alarms.set(AlarmManager.RTC_WAKEUP, toDoItem.getDate().getTime(),
                operation);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter.getCount() == 0)
            loadItems();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveItems();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
        menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_DELETE:
            mAdapter.clear();
            return true;
        case MENU_DUMP:
            dump();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void dump() {

        for (int i = 0; i < mAdapter.getCount(); i++) {
            String data = ((ToDoItem) mAdapter.getItem(i)).toLog();
            log("Item " + i + ": " + data.replace(ToDoItem.ITEM_SEP, ","));
        }

    }

    // Load stored ToDoItems
    private void loadItems() {
        BufferedReader reader = null;
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));

            String title = null;
            String priority = null;
            String status = null;
            Date date = null;

            while (null != (title = reader.readLine())) {
                priority = reader.readLine();
                status = reader.readLine();
                date = ToDoItem.FORMAT.parse(reader.readLine());
                mAdapter.add(new ToDoItem(title, Priority.valueOf(priority),
                        Status.valueOf(status), date));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Save ToDoItems to file
    private void saveItems() {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int idx = 0; idx < mAdapter.getCount(); idx++) {

                writer.println(mAdapter.getItem(idx));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    private void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }

}