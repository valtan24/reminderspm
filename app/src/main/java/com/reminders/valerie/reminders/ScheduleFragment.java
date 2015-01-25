package com.reminders.valerie.reminders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ScheduleFragment extends Fragment implements View.OnClickListener{

    private Button save_button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.reminder_schedule_fragment, container, false);
        save_button = (Button) getActivity().findViewById(R.id.save_task_button);

        return rootView;
    }
/*
    @Override
    public void onClick(View v) {
        if(v.getId() == save_button.getId()){
            getActivity().setResult(getActivity().RESULT_OK);
            getActivity().finish();
        }
    }*/
}
