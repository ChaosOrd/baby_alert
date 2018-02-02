package com.baby_alert;


import android.net.NetworkInfo;

public interface ReadingsGatheredCallback<T> {
    void onReadingGathered(T reading);

    void onReadingNotGathered(String reason);
    /**
     * Get the device's active network status in the form of a NetworkInfo object.
     */
    NetworkInfo getActiveNetworkInfo();
}
