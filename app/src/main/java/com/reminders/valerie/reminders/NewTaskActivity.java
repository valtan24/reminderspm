package com.reminders.valerie.reminders;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;

import com.reminders.valerie.reminders.taskinputview.NewTaskFragment;

public class NewTaskActivity extends ActionBarActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cud_task);

        if (findViewById(R.id.task_fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            /*if (savedInstanceState != null) {
                return;
            }*/
            Fragment task_input_fragment = new NewTaskFragment();

            task_input_fragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
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


}
