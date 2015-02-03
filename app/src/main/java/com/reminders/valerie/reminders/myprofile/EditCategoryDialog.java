package com.reminders.valerie.reminders.myprofile;

import android.app.Dialog;
import android.os.Bundle;


public class EditCategoryDialog extends AddCategoryDialog {

    private CharSequence cat_name;

    public void setCat_name(CharSequence name){
        cat_name = name;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        super.getCat_name_edittext().setText(this.cat_name);
        return dialog;
    }
}
