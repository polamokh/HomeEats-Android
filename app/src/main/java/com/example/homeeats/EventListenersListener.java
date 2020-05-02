package com.example.homeeats;

import java.util.ArrayList;
import java.util.List;

public abstract class EventListenersListener {
    private List<AbstractEventListener> retrievalEventListeners;
    private boolean Locked;
    public EventListenersListener()
    {
        retrievalEventListeners = new ArrayList<AbstractEventListener>();
        Locked = false;
    }
    private int Done;
    public abstract void onFinish();
    public void Add(AbstractEventListener abstractEventListener)  {
        if(Locked)
            throw new RuntimeException("Event Listeners Listener Locked");
        retrievalEventListeners.add(abstractEventListener);
    }
    public void OnFinishAddingListeners()
    {
        Locked = true;
        if(retrievalEventListeners.size() == 0)
            onFinish();
    }
    public void notify(AbstractEventListener abstractEventListener)  {
        if(Locked == false)
            throw new RuntimeException("Event Listeners Listener not Locked");
        retrievalEventListeners.remove(abstractEventListener);
        if(retrievalEventListeners.size() == 0)
            onFinish();
    }
}
