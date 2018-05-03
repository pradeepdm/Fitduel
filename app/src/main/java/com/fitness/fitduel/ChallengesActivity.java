package com.fitness.fitduel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;

public class ChallengesActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationItemView;
    private FrameLayout mainFrameLayout;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);
        mainFrameLayout = findViewById(R.id.main_frame);
        bottomNavigationItemView = (BottomNavigationView) findViewById(R.id.main_nav);

        InitializeFragments();


        bottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        setFragment(homeFragment);
                        return true;
                    case R.id.nav_account:
                        setFragment(accountFragment);
                        return true;
                    case R.id.nav_profile:
                        setFragment(profileFragment);
                        return true;
                    case R.id.nav_exit:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(
                                ChallengesActivity.this,
                                UserRegistrationActivity.class
                        ));

                        finish();
                        return true;
                    default:
                        return false;
                }
            }
        });


    }

    private void InitializeFragments() {

        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        accountFragment = new AccountFragment();

        setFragment(homeFragment);
    }

    private void setFragment(android.support.v4.app.Fragment fragment) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, fragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
