package com.fitness.fitduel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fitness.controller.AccelerometerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StartChallengeActivity extends AppCompatActivity {

    private static final String RULES_LIST_FILE_NAME ="rules";
    private  TextView rulesContainer;
    private Button startButton;
    private Button stopButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_challenge);
        rulesContainer = (TextView)findViewById(R.id.rulesContainer);
        startButton = (Button) findViewById(R.id.startChallengeButton);
        stopButton = (Button) findViewById(R.id.stopChallengeButton);
        StringBuilder entireFile = new StringBuilder();

        try {
            InputStream inputStream = getAssets().open(RULES_LIST_FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                entireFile.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        rulesContainer.setText(entireFile.toString());

        startButton.setOnClickListener((view)->{
            Intent intent = new Intent(this, AccelerometerService.class);
            startService(intent);
            Toast.makeText(this,"Challenge Started",Toast.LENGTH_SHORT).show();

        });

        stopButton.setOnClickListener(view -> {

            Intent intent = new Intent(this, AccelerometerService.class);
            stopService(intent);

        });
    }




}

