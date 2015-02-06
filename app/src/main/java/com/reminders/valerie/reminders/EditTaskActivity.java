package com.reminders.valerie.reminders;

import com.reminders.valerie.reminders.taskinputview.EditTaskFragment;
import com.reminders.valerie.reminders.taskinputview.TaskInputFragment;

public class EditTaskActivity extends TaskInputActivity {

    @Override
    public TaskInputFragment getFragment() {
        return new EditTaskFragment();
    }
}
