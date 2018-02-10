package com.baby_alert;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private final static String DEVICE_HOST_KEY = "deviceHost";
    public static final String HOST_EXTRA = "host";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        readHost();
    }

    public void sendMessage(View view) {
        saveHost();
        startDisplayTemperatureActivity();
    }

    private void startDisplayTemperatureActivity() {
        Intent intent = new Intent(this, DisplayReadingsActivity.class);
        String host = getDeviceHost();
        intent.putExtra(HOST_EXTRA, host);
        startActivity(intent);
    }

    @NonNull
    private String getDeviceHost() {
        EditText editText = findViewById(R.id.deviceIpNumber);
        return editText.getText().toString();
    }

    private void readHost() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String preferredHost = sharedPref.getString(DEVICE_HOST_KEY, "");
        EditText editText = findViewById(R.id.deviceIpNumber);
        editText.setText(preferredHost);
    }

    private void saveHost() {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(DEVICE_HOST_KEY, getDeviceHost());
        editor.apply();
    }

}
