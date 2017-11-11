package com.example.hossainkabir.devicecontroller;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Hossain Kabir on 4/13/2017.
 */
public class ServiceClass extends Service implements SensorEventListener {
    private AudioManager audioManager;
    private  SensorManager ManagerSensor;

    private boolean screenOn;
    private  boolean screenUp;
    private boolean lightPersent;

    private float lastAccX,lastAccY,lastAccZ;
    private boolean phoneMoved;
    private long lastMovedTime;

    private static final float movementSpeed =8;




    @Override
    public void onCreate() {
        Toast.makeText(this,"on Created",Toast.LENGTH_SHORT).show();
        Log.d("ServiceCreated","Service on Created");
        ManagerSensor = (SensorManager)getSystemService(SENSOR_SERVICE);
        audioManager=(AudioManager) getSystemService(AUDIO_SERVICE);
        Sensor lightSensor = ManagerSensor.getDefaultSensor(Sensor.TYPE_LIGHT);
        Sensor accSensor = ManagerSensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor proxSensor = ManagerSensor.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if(accSensor !=null){
            ManagerSensor.registerListener(this, accSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(lightSensor !=null){
            ManagerSensor.registerListener(this, lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (proxSensor !=null){
            ManagerSensor.registerListener(this, proxSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Service on Start Command",Toast.LENGTH_SHORT).show();
        Log.d("ServiceStartCommand","Service on Start Command");
        /*TheadClass tc=new TheadClass(startId);
        Thread thread=new Thread(tc);*/
        Thread thread=new Thread(new TheadClass(startId));
        thread.start();
        Toast.makeText(this,"Thread on StartCommand",Toast.LENGTH_SHORT).show();
        Log.d("ThreadStartCommand","Thread on Start Command");
       /* handler=new Handler(){
             public void handleMessage(Message msg) {
        String message = (String) msg.obj;
        //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
      }
        };*/

        return START_STICKY;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            float currAccX = event.values[0];
            float currAccY = event.values[1];
            float currAccZ = event.values[2];

            //if(currAccZ <=0)
            //Toast.makeText(this,""+ currAccZ,Toast.LENGTH_SHORT).show();

            if(currAccZ >= 2 && currAccZ < 10){
                screenUp=true;
                Log.d("Screen Face","Screen Face Up");
            }else if(currAccZ <=-2 && currAccZ >-10){
                screenUp=false;
                Log.d("Screen Face","Screen Face Down");
            }
            long curTime = System.currentTimeMillis();

            if((curTime-lastMovedTime)>200){
                long diffTime = (curTime - lastMovedTime);
                lastMovedTime=curTime;
                currAccX =event.values[0];
                currAccY =event.values[1];
                currAccZ =event.values[2];

                float speed = Math.abs(currAccX + currAccY + currAccZ - lastAccX - lastAccY- lastAccZ) / diffTime * 10000;

                if(speed >movementSpeed){
                    Log.d("Moving Status","Phone Moved");
                    phoneMoved=true;
                    Toast.makeText(this,"Speed = "+speed+" m/s2",Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("Moving Status","Phone Stable");
                    phoneMoved=false;
                    Toast.makeText(this,"Speed = "+speed+" m/s2",Toast.LENGTH_SHORT).show();
                }
                lastAccX= currAccX;
                lastAccY= currAccY;
                lastAccZ= currAccZ;
            }

        }
        if(event.sensor.getType()==Sensor.TYPE_LIGHT){
            if(event.values[0]<=10){
                Log.d("Screen Light","Screen Light Off");
                lightPersent=false;
            }else if(event.values[0]>10) {
                Log.d("Screen Light","Screen Light On");
                lightPersent=true;
            }

        }
        if (event.sensor.getType()==Sensor.TYPE_PROXIMITY){
            if(event.values[0]==0){
                screenOn=false;
                Log.d("Screen Proximity","Screen Off");
            }else{
                screenOn=true;
                Log.d("Screen Proximity","Screen On");
            }

        }
        if (screenUp && screenOn){
            Log.d("Profile", "In a desk Up Mode");
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            audioManager.setStreamVolume(AudioManager.STREAM_RING,audioManager.getStreamMaxVolume(AudioManager.STREAM_RING),0);
            //Toast.makeText(this,"In a desk Up Mode",Toast.LENGTH_LONG).show();
        }

        else if (!screenUp && !screenOn){
            Log.d("Profile", "In a desk Down Mode");
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            //Toast.makeText(this,"In a desk Down Mode",Toast.LENGTH_LONG).show();
        }
        else if (!lightPersent && !screenOn && phoneMoved && audioManager.getRingerMode()!=AudioManager.RINGER_MODE_VIBRATE){
            Log.d("Profile", "Pocket");
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            audioManager.setStreamVolume(AudioManager.STREAM_RING,20,0);
            //Toast.makeText(this,"Phone in Pocket",Toast.LENGTH_LONG).show();
//            SystemClock.sleep(20);

        }


    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }




    @Override
    public void onDestroy() {
        Toast.makeText(this,"Service on Desreoy ",Toast.LENGTH_SHORT).show();
        Log.d("ServiceDestroy","Service on Destroy");
        ManagerSensor.unregisterListener(this);

    }
    class TheadClass implements Runnable {
        int serviceId;

        TheadClass(int serviceId) {
            this.serviceId = serviceId;
        }

        @Override
        public void run() {
           // Message message=Message.obtain();
                Log.d("Thread", "Thread Running");
                //String text="Thread Running";
                    /*message.obj=text;
                    message.setTarget(handler); // Set the Handler
                    message.sendToTarget();*/
                //handler.sendMessage(text);
                try {
                    Thread.sleep(150);
                    //ManagerSensor.unregisterListener(this);
                    //wait(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
