package com.reminders.valerie.reminders;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.reminders.valerie.reminders.historyview.HistoryFragment;
import com.reminders.valerie.reminders.myprofile.ProfileFragment;
import com.reminders.valerie.reminders.myroutine.RoutineFragment;
import com.reminders.valerie.reminders.taskinputview.TodoFragment;

public class MainActivity extends ActionBarActivity {
    private String[] drawer_list_titles;
    private DrawerLayout drawer_layout;
    private ListView drawer_list;
    private ActionBarDrawerToggle drawer_toggle;
    private CharSequence drawer_title;
    private CharSequence title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = drawer_title = getTitle();
        drawer_list_titles = getResources().getStringArray(R.array.drawer_list);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer_toggle = new ActionBarDrawerToggle(this, drawer_layout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close){

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(title);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(drawer_title);
            }
        };

        drawer_layout.setDrawerListener(drawer_toggle);
        drawer_list = (ListView) findViewById(R.id.left_drawer);

        DrawerItem[] drawer_items = new DrawerItem[4];
        drawer_items[0] = new DrawerItem(R.drawable.ic_default, "To do");
        drawer_items[1] = new DrawerItem(R.drawable.ic_default, "History");
        drawer_items[2] = new DrawerItem(R.drawable.ic_default, "My Profile");
        drawer_items[3] = new DrawerItem(R.drawable.ic_default, "My Routine");

        DrawerAdapter adapter = new DrawerAdapter(this, R.layout.drawer_list_item, drawer_items);
        drawer_list.setAdapter(adapter);
        drawer_list.setOnItemClickListener(new DrawerItemClickListener());
        selectItem(0);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private void selectItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new TodoFragment();
                break;
            case 1:
                fragment = new HistoryFragment();
                break;
            case 2:
                fragment = new ProfileFragment();
                break;
            case 3:
                fragment = new RoutineFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            clearBackStack();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
            drawer_list.setItemChecked(position, true);
            drawer_list.setSelection(position);
            setTitle(drawer_list_titles[position]);
            drawer_layout.closeDrawer(drawer_list);

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawer_toggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if(fm.getBackStackEntryCount()>0){
                    fm.popBackStack();
                    return true;
                }
                else{
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawer_toggle.syncState();
    }

    private void clearBackStack(){
        while(getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    public void disableDrawer(){
        drawer_toggle.setDrawerIndicatorEnabled(false);
    }

    public void enableDrawer(){
        drawer_toggle.setDrawerIndicatorEnabled(true);
    }


}

