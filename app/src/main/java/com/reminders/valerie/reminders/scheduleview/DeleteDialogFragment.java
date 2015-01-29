package com.reminders.valerie.reminders.scheduleview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.reminders.valerie.reminders.R;

public class DeleteDialogFragment extends DialogFragment {

    OnDeleteSetListener listener;
    public interface OnDeleteSetListener{
        public void OnDeleteSet(int choice);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("Delete Reminder");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog_content = inflater.inflate(R.layout.delete_dialog, null);
        builder.setView(dialog_content);
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null && listener instanceof OnDeleteSetListener){
                    listener.OnDeleteSet(1);
                }
                else{
                    dialog.dismiss();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null && listener instanceof OnDeleteSetListener){
                    listener.OnDeleteSet(0);
                }
                else{
                    dialog.dismiss();
                }
            }
        });
        return builder.create();
    }

    public void setCallBack(OnDeleteSetListener listener){
        this.listener = listener;
    }
}
