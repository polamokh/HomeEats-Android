package com.example.homeeats.Listeners;

import com.example.homeeats.Listeners.AbstractEventListener;

public abstract class RetrievalEventListener<T> extends AbstractEventListener {
    public abstract void OnDataRetrieved(T t);
}
