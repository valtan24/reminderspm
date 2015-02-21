package com.reminders.valerie.reminders.scheduleview;

import android.view.View;
import android.widget.Toast;

public class ExistingScheduleFragment extends NewScheduleFragment {
    @Override
    public void onClick(View v) {
        if (v.getId() == save_button.getId()) {
            Toast.makeText(getActivity().getApplicationContext(), "data updated", Toast.LENGTH_SHORT).show();
            getActivity().setResult(getActivity().RESULT_OK);
            getActivity().finish();
        }
    }
}
