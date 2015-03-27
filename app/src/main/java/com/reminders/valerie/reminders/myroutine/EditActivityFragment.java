package com.reminders.valerie.reminders.myroutine;


import android.os.Bundle;
import android.view.View;

import com.reminders.valerie.reminders.R;

public class EditActivityFragment extends ActivityInputFragment {

    private Bundle args;

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

        //set complexity and environment
    }
}
