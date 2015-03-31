package com.reminders.valerie.reminders.myprofile;

import android.app.AlertDialog;
import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.model.Category;


public class AddCategoryDialog extends CategoryDialog{


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
