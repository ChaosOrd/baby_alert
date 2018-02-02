package com.baby_alert;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Locale;

public class DisplayReadingsActivity extends AppCompatActivity implements ReadingsGatheredCallback<Reading> {

    private GatherReadingsFragment gatherReadingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_readings);
        Intent intent = getIntent();
        String deviceHost = intent.getStringExtra(MainActivity.HOST_EXTRA);
        gatherReadingsFragment = GatherReadingsFragment.newInstance(getSupportFragmentManager(),
                deviceHost);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startGatherReadings();
    }

    private void startGatherReadings() {
        if (gatherReadingsFragment != null) {
            gatherReadingsFragment.startGatheringReadings();
        }
    }

    @Override
    public void onReadingGathered(Reading reading) {
        TextView temperatureView = findViewById(R.id.temperatureValue);
        temperatureView.setText(String.format(Locale.US,"%.1f", reading.getTemperature()));
        TextView humidityView = findViewById(R.id.humidityValue);
        humidityView.setText(String.format(Locale.US, "%.1f", reading.getHumidity()));
    }

    @Override
    public void onReadingNotGathered(String reason) {

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }
}
