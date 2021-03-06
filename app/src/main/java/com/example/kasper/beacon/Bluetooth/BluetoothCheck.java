package com.example.kasper.beacon.Bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.kasper.beacon.Beacon.BeaconList;
import com.example.kasper.beacon.R;

/**
 * Created by kasper on 3/23/2015.
 *
 * NOTE: MAIN CLASS
 * Checks whether Bluetooth is on/off, and if Bluetooth Low Energy (BLE)
 * is supported or not.
 */
public class BluetoothCheck extends Activity {

    enum Bluetooth {
        ON, OFF, NOTSUPPORTED
    }

    private static final int REQUEST_ENABLE_BT = 1234;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.bluetooth_load);

        if (checkBluetooth() == Bluetooth.NOTSUPPORTED) {
            Toast.makeText(this, "Your device does not support Bluetooth Low Energy (BLE)", Toast.LENGTH_LONG).show();
            finish();
        }
        if (checkBluetooth() == Bluetooth.OFF) {
            Toast.makeText(this, "Bluetooth is disabled", Toast.LENGTH_LONG).show();
            Intent enableBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBt, REQUEST_ENABLE_BT);
        }
        else nextActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                nextActivity();
            } else {
                finish();
            }
        }
    }

    public Enum checkBluetooth() {
        if (mBluetoothAdapter == null) {
            return Bluetooth.NOTSUPPORTED; // Device does not support Bluetooth
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                return Bluetooth.OFF; // Bluetooth is not enabled
            }
            return Bluetooth.ON; // Bluetooth is enabled
        }
    }

    public void nextActivity() {
        Intent i = new Intent(this, BeaconList.class);
        finish();
        startActivity(i);
    }
}
