package com.reminders.valerie.reminders;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends ArrayAdapter<DrawerItem> {
    Context app_context;
    int layout_res_id;
    DrawerItem items[] = null;

    public DrawerAdapter(Context app_context, int layout_res_id, DrawerItem[] items) {
        super(app_context, layout_res_id, items);
        this.layout_res_id = layout_res_id;
        this.items = items;
        this.app_context = app_context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) app_context).getLayoutInflater();
        listItem = inflater.inflate(layout_res_id, parent, false);

        ImageView drawer_icon = (ImageView) listItem.findViewById(R.id.drawer_item_icon);
        TextView drawer_text = (TextView) listItem.findViewById(R.id.drawer_item_name);

        DrawerItem folder = items[position];


        drawer_icon.setImageResource(folder.icon);
        drawer_text.setText(folder.name);

        return listItem;
    }
}
