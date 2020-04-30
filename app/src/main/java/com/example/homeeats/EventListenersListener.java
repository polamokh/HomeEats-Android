package com.example.homeeats;

import java.util.ArrayList;
import java.util.List;

public abstract class EventListenersListener {
    private List<RetrievalEventListener> retrievalEventListeners;
    public EventListenersListener()
    {
        retrievalEventListeners = new ArrayList<RetrievalEventListener>();
    }
    private int Done;
    public abstract void onFinish();
    public void Add(RetrievalEventListener retrievalEventListener)
    {
        retrievalEventListeners.add(retrievalEventListener);
    }
    public void notify(RetrievalEventListener retrievalEventListener)
    {
        retrievalEventListeners.remove(retrievalEventListener);
        if(retrievalEventListeners.size() == 0)
            onFinish();
    }
}
