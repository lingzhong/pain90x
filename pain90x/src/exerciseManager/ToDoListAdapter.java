package exerciseManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import course.labs.todomanager.R;
import exerciseManager.ToDoItem.Status;

public class ToDoListAdapter extends BaseAdapter implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<ToDoItem> mItems = new ArrayList<ToDoItem>();

    private final Context mContext;

    private static final String TAG = "Lab-UserInterface";

    public ToDoListAdapter(Context context) {
        mContext = context;
    }

    public void add(ToDoItem item) {

        mItems.add(item);
        notifyDataSetChanged();

    }

    public void clear() {

        mItems.clear();
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {

        return mItems.size();

    }

    public boolean areAllItemsDone() {
        for (ToDoItem i : mItems) {
            if (i.getStatus() == ToDoItem.Status.NOTDONE) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Object getItem(int pos) {

        return mItems.get(pos);

    }

    @Override
    public long getItemId(int pos) {

        return pos;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ToDoItem toDoItem = (ToDoItem) getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        RelativeLayout itemLayout = (RelativeLayout) inflater.inflate(
                R.layout.todo_item, parent, false);

        final TextView titleView = (TextView) itemLayout
                .findViewById(R.id.titleView);
        titleView.setText(toDoItem.getTitle());

        final CheckBox statusView = (CheckBox) itemLayout
                .findViewById(R.id.statusCheckBox);
        statusView.setChecked(toDoItem.getStatus().equals(Status.DONE));

        statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                    boolean isChecked) {

                // is called when the user toggles the status checkbox
                if (isChecked) {
                    toDoItem.setStatus(Status.DONE);
                } else {
                    toDoItem.setStatus(Status.NOTDONE);
                }

            }
        });

        final TextView priorityView = (TextView) itemLayout
                .findViewById(R.id.priorityView);
        priorityView.setText(toDoItem.getPriority().toString());

        final TextView dateView = (TextView) itemLayout
                .findViewById(R.id.dateView);
        dateView.setText(ToDoItem.FORMAT.format(toDoItem.getDate()));

        return itemLayout;

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
