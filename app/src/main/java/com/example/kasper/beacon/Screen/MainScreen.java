package com.example.kasper.beacon.Screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.example.kasper.beacon.R;
import com.example.kasper.beacon.SupportClasses.ASyncResponse;

import java.util.List;

/**
 * Created by kasper on 3/23/2015.
 */
public class MainScreen extends Activity implements ASyncResponse {

    //Temperature tmp_manager = new Temperature();
    private static final String TAG = MainScreen.class.getSimpleName();

    // Bar Settings
    private SeekBar bar;
    private Integer barValue;
    private TextView maxDistance;

    // Distance values on screen
    private TextView distance;

    // Beacon Settings
    private BeaconManager bc_manager;
    private Beacon beacon;

    // Dotview
    private View dotView;
    private int startY = -1;
    private int segmentLength = -1;


    private String temperature;

    private static final double RELATIVE_START_POS = 220.0 / 450.0;
    private static final double RELATIVE_STOP_POS = 450.0 / 450.0;

    private Region SINGLE;

    // Calculating average
    private int counter = 0;
    private int MAX = 4;
    private double[] average;
    private double currentAverage = 0;
    private static final int offset = 15;


    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.mainscreen);

        dotView = findViewById(R.id.dot);
//        tmp_manager.execute();
//        tmp_manager.delegate = this;

        average = new double[MAX];
        bar = (SeekBar) findViewById(R.id.mainscr_bar);
        maxDistance = (TextView) findViewById(R.id.mainscr_maxDistance);
        distance = (TextView) findViewById(R.id.mainscr_distance);
        init();

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                barValue = progress;
                maxDistance.setText("distância máxima: " + barValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        bc_manager = new BeaconManager(this);
        bc_manager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, final List<Beacon> beacons) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!beacons.isEmpty()) {
                            double dist = Utils.computeAccuracy(beacons.get(0));
                            distance.setText(String.format("Distance: %.1fm \n (Average: %.1fm)", dist, currentAverage));
                            calculate(dist);
                            updateDistanceView();

                            if (currentAverage > barValue) {
                                Intent i = new Intent(MainScreen.this, WarningDistance.class);
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
        maxDistance.setText("distância máxima: " + barValue);
        SINGLE = new Region("single", beacon.getProximityUUID(), beacon.getMajor(), beacon.getMinor());
    }

    private void calculate(double dist) {
        if (counter < MAX) {
            if ((currentAverage + offset) > dist) {
                average[counter] = dist;
                counter++;
            }
        }
        else {
            double newAverage = countAverage();
            currentAverage = newAverage;
            average = new double[MAX];
            counter = 0;
        }
    }

    private Double countAverage() {
        double avgDistance = 0;
            for (int i = 0; i < average.length; i++) {
            avgDistance += average[i];
        }
        return (avgDistance / MAX);
    }

    // View updated start


    public void updateDistanceView() {
        if (segmentLength == -1) {
            return;
        }
        dotView.animate().translationY(computeDotPosY()).start();

    }

    public int computeDotPosY() {
        double distance = Math.min(currentAverage, 11.0);
        return startY + (int) (segmentLength * (distance / 11.0));

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
        this.temperature = temperature;
        System.out.println("processFinish set to: " + temperature);
    }
}