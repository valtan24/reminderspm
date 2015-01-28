package com.reminders.valerie.reminders;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

public class ReminderDialog extends DialogFragment {

    OnActionSelectedListener listener_callback;

    private String[] action_list;

    public interface OnActionSelectedListener{
        public void onActionSelected(int position);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(getArguments().getString("date_time"));
        if(getArguments().getInt("with_audio") == 1){
            action_list = new String[] {"Remove Alert Tone", "Edit Date / Time", "Delete Reminder"};
        }
        else{
            action_list = new String[] {"Add Alert Tone", "Edit Date / Time", "Delete Reminder"};
        }
        builder.setItems(action_list, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener_callback != null){
                    listener_callback.onActionSelected(which);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "no listener", Toast.LENGTH_SHORT);
                }
            }
        });

        return builder.create();
    }

    public void setCallBack(OnActionSelectedListener listener){
        listener_callback = listener;
    }




}
