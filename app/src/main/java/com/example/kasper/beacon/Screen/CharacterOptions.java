package com.example.kasper.beacon.Screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.kasper.beacon.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kasper on 3/23/2015.
 */
public class CharacterOptions extends Activity {

    private SeekBar bar;
    private Integer barValue = 5;

    private boolean male = true;
    private EditText name;
    private TextView maxDistance;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.character_options);

        name = (EditText) findViewById(R.id.chardetails_text);
        bar = (SeekBar) findViewById(R.id.chardetails_bar);

        maxDistance = (TextView) findViewById(R.id.chardetails_maxDistance);
        init();

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                barValue = progress;
                maxDistance.setText("distância máxima: " + barValue);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    public void saveClick(View v) {
        if (name.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
        else {
            Intent beacon = getIntent();
            Intent i = new Intent(this, MainScreen.class);
            i.putExtra("beacon", beacon.getParcelableExtra("placeholder"));
            i.putExtra("barvalue", barValue);
            // putExtra <male> <female> if needed
            startActivity(i);
        }
    }

    public void cancelClick(View v) {
        onBackPressed();
    }

    public void meninoClick(View v) {
        male = true;
    }

    public void meninaClick(View v) {
        male = false;
    }

    public void init() {
        maxDistance.setText("distância máxima " + barValue);
    }
}
