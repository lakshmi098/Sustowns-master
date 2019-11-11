package com.sustown.sustownsapp.listeners;

public interface DataListener {
    void onDataRetrieved(Object data, String whichUrl);
    void onError(Object data);
}
