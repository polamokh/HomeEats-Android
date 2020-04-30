package com.example.homeeats;

import java.util.ArrayList;
import java.util.List;

public abstract class EventListenersListener {
    private List<RetrievalEventListener> retrievalEventListeners;
    private boolean Locked;
    public EventListenersListener()
    {
        retrievalEventListeners = new ArrayList<RetrievalEventListener>();
        Locked = false;
    }
    private int Done;
    public abstract void onFinish();
    public void Add(RetrievalEventListener retrievalEventListener)  {
        if(Locked)
            throw new RuntimeException("Event Listeners Listener Locked");
        retrievalEventListeners.add(retrievalEventListener);
    }
    public void OnFinishAddingListeners()
    {
        Locked = true;
    }
    public void notify(RetrievalEventListener retrievalEventListener)  {
        if(Locked == false)
            throw new RuntimeException("Event Listeners Listener not Locked");
        retrievalEventListeners.remove(retrievalEventListener);
        if(retrievalEventListeners.size() == 0)
            onFinish();
    }
}
