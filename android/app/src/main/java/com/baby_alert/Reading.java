package com.baby_alert;

import org.json.JSONException;
import org.json.JSONObject;

public class Reading {
    private float temperature;
    private float humidity;
    private Exception exception;

    Reading(float temperature, float humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    Reading(Exception e) {
        exception = e;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public Exception getException() {
        return exception;
    }

    public static Reading parseResult(JSONObject json) throws JSONException {
        float humidity = Float.parseFloat(json.getString("humidity"));
        float temperature = Float.parseFloat(json.getString("temperature"));
        return new Reading(temperature, humidity);
    }
}
