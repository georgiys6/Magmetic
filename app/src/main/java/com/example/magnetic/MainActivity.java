package com.example.magnetic;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends Activity implements SensorEventListener {

    TextView tvText;
    Timer timer;
    float[] magnetic = new float[3];
    double resault = 0;
    private  static SensorManager sensorManager;
    private  Sensor sensor;
    private MediaPlayer sound;
    ToneGenerator beep;
    Button start;
    Button stop;
    boolean flag = false;
    ConstraintLayout bgElement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvText = (TextView) findViewById(R.id.tvText);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//        sound = MediaPlayer.create(this, R.raw.mysound);
        start =(Button) findViewById(R.id.start);
        stop =(Button) findViewById(R.id.stop);
        beep = new ToneGenerator(AudioManager.STREAM_ALARM,60);
        ConstraintLayout bgElement = (ConstraintLayout) findViewById(R.id.container);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(sensor != null){
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);

        }else{
            Toast.makeText(this,"ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(flag == true) {
            for (int i = 0; i < 3; i++) {
                magnetic[i] = Math.round(event.values[0]);
                resault = resault + magnetic[i] * magnetic[i];
            }

            resault = Math.sqrt(resault) / 2;
//        if (resault > 150){
//            soundStart();
//        }else if(resault < 150){
//            soundStop();
//        }

            soundStart(resault);

            String text = String.format("%.0f",resault );
            tvText.setText(text);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void soundStart(double resault){
        beep.startTone(ToneGenerator.TONE_CDMA_ALERT_INCALL_LITE,(int) resault/2);
        if(resault > 150){
            tvText.setBackgroundColor(Color.RED);
        }else if(resault > 250){
            tvText.setBackgroundColor(Color.YELLOW);
        }else{
            tvText.setBackgroundColor(Color.WHITE);
            int color = Color.WHITE;
        }
    }


    public void startMagnetic(View view) {

        flag = true;
        start.setVisibility(View.GONE);
        stop.setVisibility(View.VISIBLE);
    }




    public void stopMagnetic(View view) {
        tvText.setBackgroundColor(Color.argb(100, 0, 133, 119));
        flag = false;
        stop.setVisibility(View.GONE);
        start.setVisibility(View.VISIBLE);




    }
}
