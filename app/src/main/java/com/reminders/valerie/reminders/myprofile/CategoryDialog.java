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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reminders.valerie.reminders.R;
import com.reminders.valerie.reminders.TaskDBHandler;
import com.reminders.valerie.reminders.model.Category;

public abstract class CategoryDialog extends DialogFragment {

    public final static int SAVE = 1;
    public final static int CANCEL = 0;
    public final static int DELETE = 2;
    public TextView alert_text;
    public Uri ringtone_uri;
    public EditText cat_name_edittext;
    RelativeLayout alert_tone;
    public Category category;

    TaskDBHandler dbhandler;


    OnCategorySetListener listener;


    public interface OnCategorySetListener{
        public void OnCategorySet(int choice);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        dbhandler = new TaskDBHandler(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialog_content = inflater.inflate(R.layout.category_dialog, null);
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
                    if(savedata()){
                        Toast.makeText(getActivity().getApplicationContext(), "Category saved", Toast.LENGTH_SHORT).show();
                        listener.OnCategorySet(SAVE);
                    }
                    else{
                        Toast.makeText(getActivity().getApplication(), "There was an error saving the category", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
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
                    listener.OnCategorySet(CANCEL);
                }
                else{
                    dialog.dismiss();
                }
            }
        });

        /*copy this to edit category dialog
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
        }*/


        alert_text = (TextView) dialog_content.findViewById(R.id.alert_text_clickable);
        ringtone_uri = RingtoneManager.getActualDefaultRingtoneUri(getActivity(),RingtoneManager.TYPE_RINGTONE);
        Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), ringtone_uri);
        alert_text.setText(ringtone.getTitle(getActivity()));
        category = new Category();
        setContents(builder);
        return builder.create();
    }

    public abstract boolean savedata();

    public void setCallBack(OnCategorySetListener listener){
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

    public abstract void setContents(AlertDialog.Builder builder);
}
