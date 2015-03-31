package com.reminders.valerie.reminders.myroutine;


import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.DailyActivity;
import com.reminders.valerie.reminders.model.Task;

public class EditActivityFragment extends ActivityInputFragment {

    private Bundle args;
    private TaskDBHandler dbhandler;

    public void setArgs(Bundle args){
        this.args = args;
    }

    @Override
    public void setContents() {
        action_header.setText(getActivity().getResources().getText(R.string.edit_event));

        //set title
        event_title.setText(args.getString("title"));

        //set hours and minutes
        start_hour = args.getInt("start_hour");
        start_minute = args.getInt("start_minute");
        end_hour = args.getInt("end_hour");
        end_minute = args.getInt("end_minute");
        start_time.setText(time_et_mgr.buildText(start_hour, start_minute, 0));
        end_time.setText(time_et_mgr.buildText(end_hour, end_minute, 0));

        delete_button.setVisibility(View.VISIBLE);
        button_space.setVisibility(View.VISIBLE);
        delete_button.setClickable(true);

        //set complexity and environment and category

        //category
        category = args.getString("category");
        dbhandler = new TaskDBHandler(getActivity().getApplicationContext());
        Cursor cursor = dbhandler.getCategoryNames();
        int position = -1;
        category_spinner.setSelection(0);
        do{
            cursor.moveToNext();
            position++;
            String category_name = cursor.getString(cursor.getColumnIndex("_id"));
            Log.d("category name", category_name);
            Log.d("category", category);
            if(category.equals(category_name)){
                category_spinner.setSelection(position);
                break;
            }
        }while(!cursor.isLast());
        cursor.close();

        //environment
        if(args.getInt("is_noisy") == 1) env_noisy.toggle();
        else env_dnd.toggle();

        //complexity
        if(args.getDouble("complexity") == DailyActivity.COMPLEXITY_HIGH) complexity_high.toggle();
        else if(args.getDouble("complexity") == DailyActivity.COMPLEXITY_MEDIUM) complexity_medium.toggle();
        else complexity_low.toggle();

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.save_button:
                //TODO check details first

                //update details
                long activity_id = args.getLong("activity_id");
                //create activity
                DailyActivity daily_activity = new DailyActivity();
                daily_activity.setStart_hour(start_hour);
                daily_activity.setStart_minute(start_minute);
                daily_activity.setEnd_hour(end_hour);
                daily_activity.setEnd_minute(end_minute);
                if(env_group.getCheckedRadioButtonId() == R.id.environment_noisy) daily_activity.setNoisy(1);
                else daily_activity.setNoisy(0);
                if(complexity_group.getCheckedRadioButtonId() == R.id.complexity_high) daily_activity.setComplexity(DailyActivity.COMPLEXITY_HIGH);
                else if(complexity_group.getCheckedRadioButtonId() == R.id.complexity_medium) daily_activity.setComplexity(DailyActivity.COMPLEXITY_MEDIUM);
                else daily_activity.setComplexity(DailyActivity.COMPLEXITY_LOW);
                daily_activity.setCategory(category);
                daily_activity.setId(activity_id);
                daily_activity.setName(event_title.getText().toString());
                daily_activity.setDay(day);
                if(dbhandler.updateDailyActivity(daily_activity)){
                    Log.i("EditActivityFragment", "activity updated");
                }
                else{
                    Log.e("EditActivityFragment", "failed to update activity");
                }
                dbhandler.close();
                super.onClick(v);
                break;
        }
    }
}
