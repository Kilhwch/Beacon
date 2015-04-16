package com.example.kasper.beacon.Screen;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.example.kasper.beacon.R;



/**
 * Created by kasper on 4/10/2015.
 */
public class WarningTemperature extends Activity {

    private Button okButton;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.screen_warningtemp);

        okButton = (Button) findViewById(R.id.warningOk); // Fix the ID
    }


}
