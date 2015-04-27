package com.example.kasper.beacon.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.kasper.beacon.Bluetooth.BluetoothCheck;

/**
 * Created by kasper on 3/23/2015.
 * Transition through classes below:
 *
 * MainActivity        (Main method)       →
 * BluetoothCheck      (Checks bluetooth)  →
 * BeaconList          (Shows beacons)     →
 * CharacterOptions                        →
 * MainScreen          (Main mainscreen screen)
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
