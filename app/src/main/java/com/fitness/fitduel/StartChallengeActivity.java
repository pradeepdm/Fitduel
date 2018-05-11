package com.fitness.fitduel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fitness.controller.AccelerometerService;
import com.fitness.model.UserInfoModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("FieldCanBeLocal")
public class StartChallengeActivity extends AppCompatActivity {

    private static final String RULES_LIST_FILE_NAME ="rules";
    private  TextView rulesContainer;
    private Button startButton;
    private Button stopButton;
    private Integer challengeCost = 3;


//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            update();
//        }
//    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_challenge);
        rulesContainer = findViewById(R.id.rulesContainer);
        startButton = findViewById(R.id.startChallengeButton);
        //stopButton = (Button) findViewById(R.id.stopChallengeButton);
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
            String currentBalance =UserInfoModel.instance().getCurrentBalance();
            if(Integer.parseInt(currentBalance)<3){
                Toast.makeText(this,"Insufficient Funds!!",Toast.LENGTH_LONG).show();
            }
            else {
                Intent intent = new Intent(this, AccelerometerService.class);
                startService(intent);
                String newBalancePostChallenge = String.valueOf(Integer.parseInt(UserInfoModel.instance().getCurrentBalance())-challengeCost);
                UserInfoModel.instance().setCurrentBalance(newBalancePostChallenge);
                Toast.makeText(this,"Challenge Started",Toast.LENGTH_SHORT).show();
            }

           // registerReceiver(broadcastReceiver,new IntentFilter(AccelerometerService.BROADCAST_ACTION));

        });

//        stopButton.setOnClickListener(view -> {
//
//            Intent intent = new Intent(this, AccelerometerService.class);
//            stopService(intent);
//
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // unregisterReceiver(broadcastReceiver);
    }

    private void update() {
        Log.d("Brodcast","called from broadcast");
    }







}

