package com.sustown.sustownsapp.Api;

import android.content.Context;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.sustownsapp.R;
import com.sustown.sustownsapp.helpers.Helper;
import com.sustown.sustownsapp.listeners.ConnectivityReceiver;
import com.sustown.sustownsapp.listeners.DataListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class WebServices {
    DataListener mDataListener;
    Context context;
    Helper basicUtilities;
    boolean isData;

    public WebServices(Context context) {
        mDataListener = (DataListener) context;
        this.isData = true;
        this.context = context;
        basicUtilities = new Helper(context);
    }

    public void postJsonBodyAndGetJsonObject(final String url, JSONObject jsonObject) {
        if (ConnectivityReceiver.isConnected()) {
            AndroidNetworking.post(DZ_URL.BASE_URL + url)
                    .addJSONObjectBody(jsonObject)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            mDataListener.onDataRetrieved(response, url);
                        }

                        @Override
                        public void onError(ANError anError) {
                            mDataListener.onError(anError);
                        }
                    });
        } else {
            basicUtilities.hideLoader();
            basicUtilities.showToast(context, context.getString(R.string.internet_connection));
        }
    }

    public void getJsonObject(final String url) {
        if (ConnectivityReceiver.isConnected()) {
            try {
                AndroidNetworking.get(DZ_URL.BASE_URL + url)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                mDataListener.onDataRetrieved(response, url);
                            }

                            @Override
                            public void onError(ANError anError) {
                                mDataListener.onError(anError);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                basicUtilities.showToast(context, context.getString(R.string.internet_connection));
            }
        } else {
            basicUtilities.hideLoader();
            basicUtilities.showToast(context, context.getString(R.string.internet_connection));
        }

    }

    public void getJsonObjectURL(final String url) {
        if (ConnectivityReceiver.isConnected()) {
            try {
                AndroidNetworking.get(url)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                mDataListener.onDataRetrieved(response, url);
                            }

                            @Override
                            public void onError(ANError anError) {
                                mDataListener.onError(anError);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                basicUtilities.showToast(context, context.getString(R.string.internet_connection));
            }
        } else {
            basicUtilities.hideLoader();
            basicUtilities.showToast(context, context.getString(R.string.internet_connection));
        }

    }

    public void getJsonArray(final String url) {
        if (ConnectivityReceiver.isConnected()) {
            try {
                AndroidNetworking.get(DZ_URL.BASE_URL + url)
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                mDataListener.onDataRetrieved(response, url);
                            }

                            @Override
                            public void onError(ANError anError) {
                                mDataListener.onError(anError);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                basicUtilities.showToast(context, context.getString(R.string.internet_connection));
            }
        } else {
            basicUtilities.hideLoader();
            basicUtilities.showToast(context, context.getString(R.string.internet_connection));
        }

    }

}
