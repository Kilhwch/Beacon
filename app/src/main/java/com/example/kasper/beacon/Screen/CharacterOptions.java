package com.example.kasper.beacon.Screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kasper.beacon.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kasper on 3/23/2015.
 */
public class CharacterOptions extends Activity {

    // Bar settings
    private SeekBar bar;
    private Integer barValue = 5;

    private boolean male = true;
    private EditText name;
    private Button menino;
    private Button menina;

    private TextView maxDistance;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.character_options);

        name = (EditText) findViewById(R.id.chardetails_text);
        bar = (SeekBar) findViewById(R.id.chardetails_bar);
        menino = (Button) findViewById(R.id.chardetails_menino);
        menina = (Button) findViewById(R.id.chardetails_menina);

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
        Toast.makeText(this, "Male: " + male, Toast.LENGTH_SHORT).show();
        if (name.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
        else {
            Intent beacon = getIntent();
            Intent i = new Intent(this, MainScreen.class);
            i.putExtra("beacon", beacon.getParcelableExtra("placeholder"));
            i.putExtra("barvalue", barValue);
            startActivity(i);
        }
    }

    public void cancelClick(View v) {
        onBackPressed();
    }

    public void meninoClick(View v) {
        male = true;
        buttonDelay(menino);
    }

    public void meninaClick(View v) {
        male = false;
        buttonDelay(menina);
    }


    public void init() {
        maxDistance.setText("distância máxima " + barValue);
        //menino.setBackgroundResource(R.color.blue);
        //menino.setPressed(true);
    }

    public void buttonDelay(final Button button) {
        button.setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        button.setEnabled(true);
                    }
                });
            }
        }, 1000);
    }
}
