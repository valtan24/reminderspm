package com.reminders.valerie.reminders.myroutine;

import android.util.Log;
import android.view.View;


import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.DailyActivity;

import java.util.Calendar;

public class NewActivityFragment extends ActivityInputFragment {
    @Override
    public void setContents() {
        action_header.setText("New Activity");

        //set hours and minutes
        final Calendar cal = Calendar.getInstance();
        start_hour = cal.get(Calendar.HOUR_OF_DAY);
        start_minute = cal.get(Calendar.MINUTE);
        end_hour = cal.get(Calendar.HOUR_OF_DAY);
        end_minute = cal.get(Calendar.MINUTE);

        delete_button.setVisibility(View.GONE);
        button_space.setVisibility(View.GONE);
        delete_button.setClickable(false);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.save_button:
                //save activity into db
                DailyActivity activity = new DailyActivity();
                activity.setDay(day);
                activity.setName(event_title.getText().toString());
                activity.setStart_hour(start_hour);
                activity.setEnd_hour(end_hour);
                activity.setStart_minute(start_minute);
                activity.setEnd_minute(end_minute);
                activity.setCategory(category);
                switch(complexity_group.getCheckedRadioButtonId()){
                    case R.id.complexity_high:
                        activity.setComplexity(DailyActivity.COMPLEXITY_HIGH);
                        break;
                    case R.id.complexity_medium:
                        activity.setComplexity(DailyActivity.COMPLEXITY_MEDIUM);
                        break;
                    case R.id.complexity_low:
                        activity.setComplexity(DailyActivity.COMPLEXITY_LOW);
                        break;
                }
                switch(env_group.getCheckedRadioButtonId()){
                    case R.id.environment_noisy:
                        activity.setNoisy(1);
                        break;
                    case R.id.environment_dnd:
                        activity.setNoisy(0);
                        break;
                }
                TaskDBHandler dbhandler = new TaskDBHandler(getActivity().getApplicationContext());
                if(dbhandler.addDailyActivity(activity) != -1){
                    Log.i("add activity", "success");
                }
                super.onClick(v);
                break;
        }
    }
}
