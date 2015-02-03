package com.reminders.valerie.reminders.myprofile;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.reminders.valerie.reminders.R;


public class AddCategoryDialog extends DialogFragment {

    private CharSequence title;

    public boolean isSet_neutral_button() {
        return set_neutral_button;
    }

    public void setSet_neutral_button(boolean set_neutral_button) {
        this.set_neutral_button = set_neutral_button;
    }

    private boolean set_neutral_button;

    public EditText getCat_name_edittext() {
        return cat_name_edittext;
    }

    EditText cat_name_edittext;
    RelativeLayout alert_tone;

    OnAddSetListener listener;
    public interface OnAddSetListener{
        public void OnAddSet(int choice);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle(this.getTitle());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog_content = inflater.inflate(R.layout.new_category_dialog, null);
        builder.setView(dialog_content);

        cat_name_edittext = (EditText) dialog_content.findViewById(R.id.category_name_edittext);
        alert_tone = (RelativeLayout) dialog_content.findViewById(R.id.alert_tone_layout);
        alert_tone.setClickable(true);
        alert_tone.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //open new dialog to choose tones.
            }
        });

        builder.setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null){
                    listener.OnAddSet(1);
                }
                else{
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                if(listener != null){
                    listener.OnAddSet(0);
                }
                else{
                    dialog.dismiss();
                }
            }
        });

        if(this.isSet_neutral_button()){
            builder.setNeutralButton(R.string.delete_button, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(listener != null){
                        listener.OnAddSet(2);
                    }
                    else{
                        dialog.dismiss();
                    }
                }
            });
        }
        return builder.create();
    }

    public void setTitle(CharSequence title){
        this.title = title;
    }

    public CharSequence getTitle(){
        return title;
    }

    public void setCallBack(OnAddSetListener listener){
        this.listener = listener;
    }
}
