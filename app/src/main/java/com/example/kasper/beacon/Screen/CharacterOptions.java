package com.example.kasper.beacon.Screen;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.kasper.beacon.R;

/**
 * Created by kasper on 3/23/2015.
 *
 * Character options in order to specify name, sex and max distance/
 */
public class CharacterOptions extends Activity {

    public static SeekBar bar;
    private Integer barValue = 5;

    private RadioButton menino, menina;

    public static boolean male = true;
    private EditText name;
    private TextView maxDistance;

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);
        setContentView(R.layout.character_options);

        name = (EditText) findViewById(R.id.chardetails_text);
        bar = (SeekBar) findViewById(R.id.chardetails_bar);

        menino = (RadioButton) findViewById(R.id.chardetails_menino);
        menina = (RadioButton) findViewById(R.id.chardetails_menina);

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
            i.putExtra("sex", male);
            startActivity(i);
        }
    }

    public void cancelClick(View v) {
        onBackPressed();
    }

    public void meninoClick(View v) {
        male = true;
        menino.setBackground(getResources().getDrawable(R.drawable.btnboyselected));
        menina.setBackground(getResources().getDrawable(R.drawable.btngirlnotselected));
    }

    public void meninaClick(View v) {
        male = false;
        menina.setBackground(getResources().getDrawable(R.drawable.btngirlselected));
        menino.setBackground(getResources().getDrawable(R.drawable.btnboynotselected));
    }

    public void init() {
        maxDistance.setText("distância máxima " + barValue);
    }
}
