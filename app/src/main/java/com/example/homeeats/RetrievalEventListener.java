package com.example.homeeats;

public abstract class RetrievalEventListener<T> extends AbstractEventListener{
    public abstract void OnDataRetrieved(T t);
}
