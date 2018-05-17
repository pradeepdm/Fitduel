package com.fitness.controller;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.fitness.fitduel.HomeFragment;
import com.fitness.fitduel.R;
import com.fitness.model.UserInfoModel;

@SuppressWarnings("FieldCanBeLocal")
public class AccelerometerService extends Service implements SensorEventListener, StepListener {

    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private static Long currentSteps = 0L;

    private static int TIME_TO_STOP = 15;
    private CountDownTimer counterTimer;
    NotificationManager notificationManager;
    private static final String TAG = "FLAG";
    private static final String STEPS_COUNT = "STEPS";
    private static final Boolean FLAG_FROM_SERVICE = true;
    public static final String BROADCAST_ACTION = "com.fitness.controller";
    private final Handler handler = new Handler();
    Intent intent;

    public AccelerometerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        currentSteps = 0L;
        // Get an instance of the SensorManager
        sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        Sensor accelerometerValue = sensorManager != null ? sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) : null;
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);

        sensorManager.registerListener(this, accelerometerValue, SensorManager.SENSOR_DELAY_FASTEST);
         counterTimer = new CountDownTimer(TIME_TO_STOP * 1000, TIME_TO_STOP * 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                stopSelf();
            }
        };
         counterTimer.start();
        createNotificationChannel();
        showNotification();
        return START_STICKY;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("AccelerrometerNotification", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        Long stepsToWin = Long.valueOf(getString(R.string.stepsToWin));
        if(currentSteps<stepsToWin){
            Toast.makeText(this,"Oops!! You did not win, better luck next time",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"Hurray!! You won.Congratulations",Toast.LENGTH_LONG).show();
           String currentBalance =  UserInfoModel.instance().getCurrentBalance();
           Integer balanceAfterWin = Integer.parseInt(currentBalance)+6;
           UserInfoModel.instance().setCurrentBalance(balanceAfterWin.toString());
        }
        Log.d("Challenge","Challenge Completed");
        sensorManager.unregisterListener(this);
        stopNotification();
        intent.putExtra(TAG,FLAG_FROM_SERVICE);
        intent.putExtra(STEPS_COUNT,currentSteps);

        sendBroadcast(intent);
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public static Long getCurrentStatus() {
        return currentSteps;
    }

    @Override
    public void step(Long steps) {
        currentSteps++;
    }

    private void showNotification() {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "AccelerrometerNotification");
        notificationBuilder.setContentTitle("FitDuel");
        notificationBuilder.setContentText("Challenge  is running in the background.");
        notificationBuilder.setSmallIcon(R.drawable.running_logo);
        notificationBuilder.setColor(Color.parseColor("#6600cc"));
        int colorLED = Color.argb(255, 0, 255, 0);
        notificationBuilder.setLights(colorLED, 500, 500);
        // To  make sure that the Notification LED is triggered.
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setOngoing(true);

        //Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,0,new Intent(),0);
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());

    }

    private void stopNotification() {
        notificationManager.cancel(0);
    }
}
