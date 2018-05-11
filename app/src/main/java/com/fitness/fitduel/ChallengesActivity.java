package com.fitness.fitduel;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

@SuppressWarnings("FieldCanBeLocal")
public class ChallengesActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationItemView;
    private HomeFragment homeFragment;
    private AccountFragment accountFragment;
    private ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);
        FrameLayout mainFrameLayout = findViewById(R.id.main_frame);
        bottomNavigationItemView = findViewById(R.id.main_nav);

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

                        confirmLogout();
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

    @Override
    public void onBackPressed() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle(getString(R.string.alert));
        dialog.setContentView(R.layout.alert_dialog);
        Button exit = dialog.findViewById(R.id.exit);
        Button cancel = dialog.findViewById(R.id.resume);
        dialog.show();

        exit.setOnClickListener(view -> {
            dialog.dismiss();
            finish();
        });

        cancel.setOnClickListener(view -> dialog.dismiss());
    }

    private void confirmLogout() {

        final Dialog dialog = new Dialog(this);
        dialog.setTitle(getString(R.string.alert));
        dialog.setContentView(R.layout.alert_dialog);
        Button logout = dialog.findViewById(R.id.exit);
        Button cancel = dialog.findViewById(R.id.resume);
        TextView alertText = dialog.findViewById(R.id.alterText);
        logout.setText(getString(R.string.logout_button));
        alertText.setText(getString(R.string.logout_message));
        dialog.show();

        logout.setOnClickListener((View view) -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(
                    ChallengesActivity.this,
                    UserRegistrationActivity.class
            );
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            dialog.dismiss();
            finish();
        });

        cancel.setOnClickListener(view -> dialog.dismiss());
    }

}
