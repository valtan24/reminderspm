package com.reminders.valerie.reminders.myprofile;

import android.app.AlertDialog;
import com.reminders.valerie.reminders.R;


public class AddCategoryDialog extends CategoryDialog{


    @Override
    public boolean savedata() {
        category.setAudio_uri(ringtone_uri.toString());
        category.setCategory_title(cat_name_edittext.getText().toString());
        category.setMotivation(0.5);
        if(dbhandler != null){
            try {
                dbhandler.addNewCategory(category);
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
        builder.setTitle(getActivity().getResources().getText(R.string.add_category));
    }
}
