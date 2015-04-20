package com.example.kasper.beacon.Screen;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kasper.beacon.R;

/**
 * Created by kasper on 4/2/2015.
 */
public class WarningDistance extends Activity {

    private TextView warnDist;
    private Vibrator v;
    private Button okButton;


    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.screen_warningdist);

        double distance = getIntent().getDoubleExtra("distance", 0);
        warnDist = (TextView) findViewById(R.id.warningDistance);
        warnDist.setText(String.format("%.1fm", distance));

        v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        v.vibrate(300);
        okButton = (Button) findViewById(R.id.warningOk);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
