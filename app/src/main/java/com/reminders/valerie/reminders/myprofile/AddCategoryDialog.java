package com.reminders.valerie.reminders.myprofile;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.Category;


public class AddCategoryDialog extends DialogFragment{


    private CharSequence title;
    private boolean set_neutral_button;
    public TextView alert_text;
    private Uri ringtone_uri;
    EditText cat_name_edittext;
    RelativeLayout alert_tone;

    OnAddSetListener listener;


    public interface OnAddSetListener{
        public void OnAddSet(int choice);
    }
    public void setSet_neutral_button(boolean set_neutral_button) {
        this.set_neutral_button = set_neutral_button;
    }
    public boolean isSet_neutral_button() {
        return set_neutral_button;
    }
    public EditText getCat_name_edittext() {
        return cat_name_edittext;
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
                RingtoneManager ringtone_mgr = new RingtoneManager(getActivity());
                Intent pick_ringtone = new Intent(ringtone_mgr.ACTION_RINGTONE_PICKER);
                pick_ringtone.putExtra(ringtone_mgr.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
                pick_ringtone.putExtra(ringtone_mgr.EXTRA_RINGTONE_TITLE, getActivity().getResources().getText(R.string.ringtone_picker));
                pick_ringtone.getBooleanExtra(ringtone_mgr.EXTRA_RINGTONE_INCLUDE_DRM, true);

                String uri = null;
                if ( uri != null ) {
                    pick_ringtone.putExtra(ringtone_mgr.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(uri));
                }
                else {
                    pick_ringtone.putExtra(ringtone_mgr.EXTRA_RINGTONE_EXISTING_URI, (Uri)null);
                }

                startActivityForResult(pick_ringtone, 10);
            }
        });


        builder.setPositiveButton(R.string.save_button, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(listener != null){
                    savedata();
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

        alert_text = (TextView) dialog_content.findViewById(R.id.alert_text_clickable);
        ringtone_uri = RingtoneManager.getActualDefaultRingtoneUri(getActivity(),RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), ringtone_uri);
        alert_text.setText(ringtone.getTitle(getActivity()));
        return builder.create();
    }

    public void savedata() {
        Category new_category = new Category(cat_name_edittext.getText().toString(), ringtone_uri.toString());
        //continue here
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent pick_ringtone){
        if(requestCode == 10){
            if(resultCode == getActivity().RESULT_OK){
                RingtoneManager ringtone_mgr = new RingtoneManager(getActivity());
                ringtone_uri = pick_ringtone.getParcelableExtra(ringtone_mgr.EXTRA_RINGTONE_PICKED_URI);
                Ringtone ringtone = ringtone_mgr.getRingtone(getActivity(),ringtone_uri);
                String ringtone_title = ringtone.getTitle(getActivity());
                alert_text.setText(ringtone_title);
            }
        }
    }
}
