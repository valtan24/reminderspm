package com.reminders.valerie.reminders;

import com.reminders.valerie.reminders.taskinputview.NewTaskFragment;
import com.reminders.valerie.reminders.taskinputview.TaskInputFragment;

public class NewTaskActivity extends TaskInputActivity{

    @Override
    public TaskInputFragment getFragment() {
        return new NewTaskFragment();
    }
}
