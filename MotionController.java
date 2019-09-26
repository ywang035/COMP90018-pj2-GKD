package com.example.mobile_pj2.Control;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


import com.example.mobile_pj2.UI.Main.MainActivity;

import static android.content.ContentValues.TAG;
import static android.content.Context.SENSOR_SERVICE;

public class MotionController implements Runnable, SensorEventListener{

    private Context context;
    private Sensor mSensor;
    private SensorManager mSensorManager;

    public static int getX;

    public MotionController(Context context) {
        this.context = context;
    }

    @Override
    public void run() {


        mSensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        if (mSensorManager == null) {
            Log.d(TAG, "device does not support SensorManager");
        } else {
            //  G-Sensor
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener((SensorEventListener) this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            int x = (int) event.values[0];
            int y = (int) event.values[1];
            int z = (int) event.values[2];

//            System.out.println(x+y+z);

            getX = x;


        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

}
