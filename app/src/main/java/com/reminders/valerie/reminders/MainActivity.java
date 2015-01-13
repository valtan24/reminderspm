package com.reminders.valerie.reminders;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.os.Bundle;

public class MainActivity extends ActionBarActivity{

    ActionBar.Tab history_tab, todo_tab, settings_tab;
    Fragment history_fragment = new HistoryFragment();
    Fragment todo_fragment = new TodoFragment();
    Fragment settings_fragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //remove if required

        ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        history_tab = bar.newTab();
        todo_tab = bar.newTab();
        settings_tab = bar.newTab();

        history_tab.setText("History");
        todo_tab.setText("To Do");
        settings_tab.setText("Settings");

        history_tab.setTabListener(new TabListener(history_fragment));
        todo_tab.setTabListener(new TabListener(todo_fragment));
        settings_tab.setTabListener(new TabListener(settings_fragment));

        bar.addTab(history_tab);
        bar.addTab(todo_tab);
        bar.addTab(settings_tab);

    }


}
