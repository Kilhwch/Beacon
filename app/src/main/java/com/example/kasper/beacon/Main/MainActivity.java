package com.example.kasper.beacon.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.kasper.beacon.Bluetooth.BluetoothCheck;

/**
 * Created by kasper on 3/23/2015.
 * Transition through classes below:
 *
 * Main_Activity        (Main method)       →
 * BluetoothCheck       (Checks bluetooth)  →
 * Beacon_Select        (Shows beacons)     →
 * Character_Options                        →
 * Main_Screen          (Main mainscreen screen)
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        Intent i = new Intent(this, BluetoothCheck.class);
        finish();
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
