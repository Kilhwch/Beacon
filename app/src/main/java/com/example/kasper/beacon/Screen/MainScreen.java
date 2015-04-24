package com.example.kasper.beacon.Screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.example.kasper.beacon.R;
import com.example.kasper.beacon.SupportClasses.ASyncResponse;
import com.example.kasper.beacon.SupportClasses.AvrgDistance;
import com.example.kasper.beacon.SupportClasses.Temperature;

import java.util.List;

/**
 * Created by kasper on 3/23/2015.
 */
public class MainScreen extends Activity implements ASyncResponse {

    private static final String TAG = MainScreen.class.getSimpleName();
    private Temperature tmp_manager = new Temperature();


    // Bar Settings
    private Integer barValue;
    private TextView maxDistance;

    // Distance values on screen
    private TextView distance, outsideTemperature;

    // Beacon Settings
    private BeaconManager bc_manager;
    private Beacon beacon;

    // Dotview
    private View dotView;
    private int startY = -1;
    private int segmentLength = -1;

    private double newDistance;


    private String temperature;
    private static final double RELATIVE_START_POS = 230.0 / 260.0; // 230.0 / 260.0
    private static final double RELATIVE_STOP_POS = 260.0 / 260.0; // 260.0 / 260.0

    private Region SINGLE;
    private AvrgDistance next = new AvrgDistance();

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.mainscreen);


        dotView = findViewById(R.id.dot);
        tmp_manager.execute();
        tmp_manager.delegate = this;

        maxDistance = (TextView) findViewById(R.id.mainscr_maxDistance);
        distance = (TextView) findViewById(R.id.mainscr_distance);
        outsideTemperature = (TextView) findViewById(R.id.mainscr_temperature);
        init();

        bc_manager = new BeaconManager(this);
        bc_manager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!beacons.isEmpty()) {
                            newDistance = Utils.computeAccuracy(beacons.get(0));
                            double currentAverage = next.getCurrentAverage();
                            distance.setText(String.format("Distance: %.1fm \n (Average: %.1fm)", newDistance, currentAverage));
                            next.calculate(newDistance, currentAverage);
                            updateDistanceView();

                            if (currentAverage > barValue) {
                                Intent i = new Intent(MainScreen.this, WarningDistance.class);
                                i.putExtra("distance", currentAverage);
                                startActivity(i);
                            }
                        }
                    }
                });
            }
        });

        final View view = findViewById(R.id.radarwin);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                startY = (int) (RELATIVE_START_POS * view.getMeasuredHeight());
                int stopY = (int) (RELATIVE_STOP_POS * view.getMeasuredHeight());
                segmentLength = stopY - startY;

                dotView.setVisibility(View.VISIBLE);
                dotView.setTranslationY(computeDotPosY());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        bc_manager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    bc_manager.startRanging(SINGLE);
                } catch (RemoteException e) {
                    Toast.makeText(MainScreen.this, "Cannot start ranging, something terrible happened",
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }

    private void init() {
        beacon = getIntent().getParcelableExtra("beacon");
        barValue = getIntent().getIntExtra("barvalue", 5);
        maxDistance.setText("distância máxima: \n " + barValue + "m");
        SINGLE = new Region("single", beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());

    }


    // View updated start

    public void updateDistanceView() {
        if (segmentLength == -1) {
            return;
        }
        dotView.animate().translationY(computeDotPosY()).start();
    }

    public int computeDotPosY() {
        double distance = newDistance; // switch into next.getCurrentAverage if you want to update the screen less frequently
        return startY + (int) (segmentLength * -(distance / 2));
    }

    // View updated end

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
            bc_manager.stopRanging(SINGLE);
        } catch (RemoteException e) {
            Log.d(TAG, "Error while stopping ranging", e);
        }
        super.onStop();
    }

    @Override
    public void processFinish(String temperature) {
        if (temperature != null) {
            this.temperature = temperature;
            outsideTemperature.setText("Temperature: " + this.temperature + "°C");
        } else outsideTemperature.setText("Error when trying to fetching temperature");
    }
}