package com.ewida.mealmaster.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

import com.ewida.mealmaster.MealMasterApplication;

public class NetworkUtils extends ConnectivityManager.NetworkCallback {

    private final Context context;
    private final NetworkCallbacksListener networkCallbacksListener;

    public NetworkUtils(Context context, NetworkCallbacksListener networkCallbacksListener){
        this.context = context;
        this.networkCallbacksListener = networkCallbacksListener;
    }

    public static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) MealMasterApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        networkCallbacksListener.onConnectionAvailable();

    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        networkCallbacksListener.onConnectionUnAvailable();
    }




    public void register() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest networkRequest = new NetworkRequest.Builder().build();
        cm.registerNetworkCallback(networkRequest, this);
    }

    public void unregister() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        cm.unregisterNetworkCallback(this);
    }

    public interface NetworkCallbacksListener{
        void onConnectionAvailable();

        void onConnectionUnAvailable();
    }
}
