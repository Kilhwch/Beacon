package com.example.kasper.beacon.Screen;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

import com.example.kasper.beacon.R;

/**
 * Created by kasper on 4/2/2015.
 */
public class WarningDistance extends Activity {

    private Vibrator v;
    private Button okButton;
    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.screen_warningdist);

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
