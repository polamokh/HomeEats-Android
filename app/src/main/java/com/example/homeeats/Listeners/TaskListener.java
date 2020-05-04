package com.example.homeeats.Listeners;

import com.example.homeeats.Listeners.AbstractEventListener;

public abstract class TaskListener extends AbstractEventListener {
    public abstract void OnSuccess();
    public abstract void OnFail();
}
