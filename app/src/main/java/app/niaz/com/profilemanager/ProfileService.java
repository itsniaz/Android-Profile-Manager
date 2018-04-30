package app.niaz.com.profilemanager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Niaz on 4/5/2018.
 */

public class ProfileService extends Service implements SensorEventListener{

    public static String PROFILE_MODE="";

    public String log_tag ="Profile Mode";

    //Sensor Variables
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mProximity;
    private Sensor mLight;
    private Sensor mGyro;


    //Sensor Values;
    private float distance;
    private float aX;
    private float aY;
    private float aZ;
    private float light;

    //Label Change in Activity

    public void changeLabel(String label)
    {
        if(MainActivity.profileMode!=null)
        {
            MainActivity.profileMode.setText("Current Mode : "+ label);
        }

        Log.d(log_tag, PROFILE_MODE);
    }

    //Audio Manager

    private AudioManager audioManager;

    @Override
    public void onCreate() {
        initSensors();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        registerSensorListeners();
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(ProfileService.this.getApplicationContext(),"Service Started", Toast.LENGTH_SHORT).show();
            }
        });
        return Service.START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy()
    {
        deregisterSensorListeners();

        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(ProfileService.this.getApplicationContext(),"Service Stopped", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent)
    {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT)
        {
            light = sensorEvent.values[0];
        }
        else if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            aX = sensorEvent.values[0];
            aY = sensorEvent.values[1];
            aZ = sensorEvent.values[2];
        }
        else if(sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            distance = sensorEvent.values[0];
        }
        else
        {

        }

        activateProfile();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void initSensors()
    {
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor (Sensor.TYPE_ACCELEROMETER);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    private void registerSensorListeners()
    {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mGyro,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mProximity,SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this,mLight,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private  void deregisterSensorListeners()
    {
        mSensorManager.unregisterListener(this);
    }

    private  void activateModeSilent()
    {
        try
        {
            audioManager.setRingerMode(audioManager.RINGER_MODE_SILENT);
        }
        catch (Exception e)
        {

        }
    }

    private  void activateModeVibrate()
    {
        audioManager.setRingerMode(audioManager.RINGER_MODE_VIBRATE);
    }

    private  void activateRingAndVibrate()
    {
        audioManager.setRingerMode(audioManager.RINGER_MODE_NORMAL);
    }

    private  void activateModeNormal()
    {
        audioManager.setRingerMode(audioManager.RINGER_MODE_NORMAL);
    }

    private  void activateProfile()
    {
        //Device facing upward on a table
        if(aZ>-9 && distance>=5 && aY<=0)
        {
            if(!PROFILE_MODE.equalsIgnoreCase("Normal"))
            {
                activateModeNormal();
                PROFILE_MODE = "Normal";
                changeLabel(PROFILE_MODE);
            }

        }
        //Device facedown on table
        else if(aZ<-8.5 && distance==0 && aY<=1)
        {
            if(!PROFILE_MODE.equalsIgnoreCase("Silent"))
            {
                activateModeSilent();
                PROFILE_MODE = "Silent";
                changeLabel(PROFILE_MODE);
            }

        }
        //Device in pocket speaker downward
        else if(aY>0 && aZ<0 && distance==0 && light<=1.0)
        {
            if(!PROFILE_MODE.equalsIgnoreCase("Vibrate"))
            {
                activateModeVibrate();
                PROFILE_MODE = "Vibrate";
                changeLabel(PROFILE_MODE);
            }

        }
        //Device in pocket speaker upward
        else if(aY<0 && aX>1 && distance==0 && light<=.0)
        {
            if(!PROFILE_MODE.equalsIgnoreCase("VibrateAndRing"))
            {
                activateModeVibrate();
                PROFILE_MODE = "VibrateAndRing";
                changeLabel(PROFILE_MODE);
            }

        }
        else
        {
            if(!PROFILE_MODE.equalsIgnoreCase("Normal"))
            {
                activateModeNormal();
                PROFILE_MODE = "Normal";
                changeLabel(PROFILE_MODE);
            }

        }

    }
}
