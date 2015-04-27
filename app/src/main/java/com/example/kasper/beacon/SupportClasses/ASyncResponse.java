package com.example.kasper.beacon.SupportClasses;

/**
 * Created by kasper on 4/14/2015.
 *
 * Bridge between Temperature and MainScreen class in order to carry the
 * temperature value.
 */
public interface ASyncResponse {
    void processFinish(String temperature);
}
