package com.reminders.valerie.reminders;


import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.reminders.valerie.reminders.taskinputview.TaskInputFragment;

public abstract class TaskInputActivity extends ActionBarActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            int notification_id = getIntent().getExtras().getInt("notification_id");
            if(notification_id != -1){
                NotificationManager notification_mgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notification_mgr.cancel(notification_id);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cud_task);

        if (findViewById(R.id.task_fragment_container) != null) {

            Fragment task_input_fragment = getFragment();

            task_input_fragment.setArguments(getIntent().getBundleExtra("arguments"));

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.task_fragment_container, task_input_fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if(fm.getBackStackEntryCount()>0){
                    fm.popBackStack();
                    return true;
                }
                else{
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract TaskInputFragment getFragment();


}
