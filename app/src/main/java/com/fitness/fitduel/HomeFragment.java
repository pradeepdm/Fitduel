package com.fitness.fitduel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.fitness.module.Utils;

import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements StepListener {

    private TextView tvSteps;
    private Button btnStart;
    private Button btnStop;
    private CountDownTimer counterTimer;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private static boolean FLAG_FROM_SERVICE = false;
    private static final String TAG = "FLAG";
    private static final String STEPS_COUNT = "STEPS";
    private static Long currentSteps;
    private static String winMessage = "Congrats!!You have won";
    private static String lossMessage = "Oops!!You lost, better luck next time";
    //time in seconds
    private static int TIME_TO_STOP = 15;
    NotificationManager notificationManager;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onResume() {
        super.onResume();
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
        btnStart = homeFragment.findViewById(R.id.startButton);
        return homeFragment;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent start = new Intent(getActivity(), StartChallengeActivity.class);
                startActivity(start);

            }
        });


    }


    @Override
    public void step(Long numSteps) {
        numSteps++;
    }

}
