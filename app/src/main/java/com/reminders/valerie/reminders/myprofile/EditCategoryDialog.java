package com.reminders.valerie.reminders.myprofile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.Category;


public class EditCategoryDialog extends CategoryDialog {

    @Override
    public boolean savedata() {
        category.setAudio_uri(ringtone_uri.toString());
        category.setCategory_title(cat_name_edittext.getText().toString());
        //get motivation value
        switch(motivation_group.getCheckedRadioButtonId()){
            case R.id.motivation_high:
                category.setMotivation(Category.MOTIVATION_HIGH);
                break;
            case R.id.motivation_medium:
                category.setMotivation(Category.MOTIVATION_MEDIUM);
                break;
            case R.id.motivation_low:
                category.setMotivation(Category.MOTIVATION_LOW);
                break;
            default:
                return false;
        }
        if(dbhandler != null){
            try {
                dbhandler.updateCategory(category);
                return true;
            }
            catch(Exception e){
                return false;
            }
        }
        else{
            return false;
        }
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

        if(category.getMotivation() == Category.MOTIVATION_HIGH){
            motivation_high.setChecked(true);
        }
        else if(category.getMotivation() == Category.MOTIVATION_LOW){
            motivation_medium.setChecked(true);
        }
        else{
            motivation_medium.setChecked(true);
        }
    }
}
