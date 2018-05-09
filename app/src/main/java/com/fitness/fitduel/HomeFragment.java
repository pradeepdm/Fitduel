package com.fitness.fitduel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fitness.controller.AccelerometerService;
import com.fitness.controller.StepListener;
import com.fitness.fitduel.R;
import com.fitness.fitduel.StartChallengeActivity;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements StepListener {

    private TextView tvSteps;
    private Button btnStart;
    private Button btnStop;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    NotificationManager notificationManager;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
        tvSteps.setText(String.valueOf(AccelerometerService.getCurrentStatus()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeFragment = inflater.inflate(
                R.layout.fragment_home,
                container,
                false
        );
        tvSteps = (TextView) homeFragment.findViewById(R.id.tv_steps);
        btnStart = (Button) homeFragment.findViewById(R.id.startButton);
        return homeFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(getActivity(), AccelerometerService.class);
//                getActivity().startService(intent);
//                Toast.makeText(getContext(),"Challenge Started",Toast.LENGTH_SHORT).show();
                // showNotification();
                Intent start = new Intent(getActivity(), StartChallengeActivity.class);
                startActivity(start);
            }
        });


//        btnStop.setOnClickListener(view -> {
//
////            Intent intent = new Intent(getActivity(), AccelerometerService.class);
////            getActivity().stopService(intent);
////            stopNotification();
//        });

    }



//    private void showNotification() {
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getActivity());
//        notificationBuilder.setContentTitle("FitDuel");
//        notificationBuilder.setContentText("Challenge  is running in the background.");
//        notificationBuilder.setSmallIcon(R.drawable.messenger_bubble_large_blue);
//        notificationBuilder.setColor(Color.parseColor("#6600cc"));
//        int colorLED = Color.argb(255, 0, 255, 0);
//        notificationBuilder.setLights(colorLED, 500, 500);
//        // To  make sure that the Notification LED is triggered.
//        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
//        notificationBuilder.setOngoing(true);
//
//        //Intent resultIntent = new Intent(this, MainActivity.class);
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(getContext(),0,new Intent(),0);
//        notificationBuilder.setContentIntent(resultPendingIntent);
//        notificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());
//
//    }
//
//    private void stopNotification() {
//        notificationManager.cancel(0);
//    }

    @Override
    public void step(Long numSteps) {
        numSteps++;
        tvSteps.setText(String.format("%s%d", TEXT_NUM_STEPS, numSteps));
    }
}
