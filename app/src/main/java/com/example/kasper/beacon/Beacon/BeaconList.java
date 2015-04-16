package com.example.kasper.beacon.Beacon;

import android.app.Activity;
import android.content.Intent;
import com.estimote.sdk.Region;
import android.os.RemoteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.utils.L;
import com.example.kasper.beacon.Screen.CharacterOptions;
import com.example.kasper.beacon.R;

import java.util.Collections;
import java.util.List;


public class BeaconList extends Activity {

    // Beacon (bc) related
    private BeaconManager bc_manager;
    private BeaconData adapter;
    private static final String TAG = BeaconList.class.getSimpleName();
    private static final Region ALL = new Region("all", null, null, null);
    private int bc_count = 0;

    private TextView main;
    private TextView subLabel;
    private ListView list;



    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.beacon_list);

        adapter = new BeaconData(this);
        main = (TextView) findViewById(R.id.bcselect_mainlabel);
        subLabel = (TextView) findViewById(R.id.bcselect_sublabel);
        list = (ListView) findViewById(R.id.bcselect_list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(createOnItemClickListener());
        L.enableDebugLogging(true);


        bc_manager = new BeaconManager(this);
        bc_manager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.replaceWith(beacons);
                        bc_count = beacons.size();
                        loadingText();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        connectToService();
    }

    private void connectToService() {
        adapter.replaceWith(Collections.<Beacon>emptyList());
        bc_manager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    bc_manager.startRanging(ALL);
                } catch (RemoteException e) {
                    Toast.makeText(BeaconList.this, "Cannot start ranging, something terrible happened",
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }

    public void loadingText() {
        if (bc_count==0) {
            main.setText("Loading...");
            subLabel.setText("");
        }
        else {
            main.setText("Olá! Achamos " + bc_count + " pranchas:");
            subLabel.setText("Selecione a sua e personalize.");
        }
    }


    private AdapterView.OnItemClickListener createOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(BeaconList.this, CharacterOptions.class);
                i.putExtra("placeholder", adapter.getItem(position));

                // is there a need to stop ranging?
                bc_manager.disconnect();
                startActivity(i);
            }
        };
    }
    // Menu related methods -->

    // Is this necessary?
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan_menu, menu);
        MenuItem refreshItem = menu.findItem(R.id.refresh);
        refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
        return true;
    }

    // Is this necessary?
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        bc_manager.disconnect();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        try {
            bc_manager.stopRanging(ALL);
        } catch (RemoteException e) {
            Log.d(TAG, "Error while stopping ranging", e);
        }
        bc_manager.disconnect(); // this may cause errors?
        super.onStop();
    }
}
