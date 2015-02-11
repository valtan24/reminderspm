package com.reminders.valerie.reminders.myprofile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.reminders.valerie.reminders.R;


public class EditCategoryDialog extends CategoryDialog {

    @Override
    public boolean savedata() {
        return true;
    }

    @Override
    public void setContents(AlertDialog.Builder builder) {
        builder.setTitle(getActivity().getResources().getText(R.string.edit_category));
        builder.setNeutralButton(R.string.delete_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null){
                    listener.OnCategorySet(CategoryDialog.DELETE);
                }
                else{
                    dialog.dismiss();
                }
            }
        });
        Bundle args = getArguments();
        cat_name_edittext.setText(args.getString("category"));
        RingtoneManager ringtone_mgr = new RingtoneManager(getActivity());
        Ringtone ringtone = ringtone_mgr.getRingtone(getActivity(), Uri.parse(args.getString("uri")));
        alert_text.setText(ringtone.getTitle(getActivity()));

        //TODO SET MOTIVATION
    }
}
