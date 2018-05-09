package com.fitness.fitduel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.fitness.controller.UserTransactionHandler;
import com.fitness.model.UserInfoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "UserRegistration";
    private static final String EMAIL = "email";
    private CallbackManager callbackManager;
    private EditText userName;
    private EditText txtEmailAddress;
    private EditText txtPassword;
    private Button facebookLogin;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_user);
        initializeControls();
    }

    private void initializeControls() {

        /*FacebookSdk.sdkInitialize(getApplicationContext(), null);
        AppEventsLogger.activateApp(this);*/
        callbackManager = CallbackManager.Factory.create();
        facebookLogin = findViewById(R.id.login_button);

        txtEmailAddress = findViewById(R.id.email);
        userName = findViewById(R.id.name);
        txtPassword = findViewById(R.id.password);
        TextView loginLink = findViewById(R.id.link_login);
        Button createAccount = findViewById(R.id.create_account);

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = ProgressDialog.
                        show(UserRegistrationActivity.this,
                                "Please wait...",
                                "Proccessing...",
                                true
                        );

                // Finish the registration screen and return to the Login activity
                Intent i = new Intent(UserRegistrationActivity.this, UserLoginActivity.class);
                startActivity(i);
                progressDialog.dismiss();
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerUser(v);
            }
        });

        facebookLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                facebookLogin.setEnabled(false);

                LoginManager.getInstance().logInWithReadPermissions(
                        UserRegistrationActivity.this,
                        Arrays.asList(EMAIL, "public_profile"));

                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d(TAG, "On Success:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "User canceled login using Facebook");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "Error logging in using Facebook account");
                    }

                });
            }
        });
    }

    @Override
    public void onStart() {

        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            UserTransactionHandler.initializeUserInfoModel(firebaseUser);
        }
    }


    private void updateUserInfoModel(boolean isFacebook) {

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        if (UserInfoModel.instance().getName() == null
                || UserInfoModel.instance().getName().equals("")) {

            initializeUserInfoModel(user, isFacebook);
            UserTransactionHandler.storeUserProfileToFirebase();
        }
    }

    private void startChallenge() {
        // display Challenges
        Intent intent = new Intent(UserRegistrationActivity.this, ChallengesActivity.class);
        startActivity(intent);
    }

    private void initializeUserInfoModel(FirebaseUser user, boolean isFacebook) {

        if (isFacebook) {
            if (user.getDisplayName() != null) {
                UserInfoModel.instance().setName(user.getDisplayName());
            }
            UserInfoModel.instance().setEmail(user.getEmail());
            UserInfoModel.instance().setProfilePic(user.getPhotoUrl().toString());
        } else {

            UserInfoModel.instance().setName(userName.getText().toString());
            UserInfoModel.instance().setEmail(txtEmailAddress.getText().toString());
        }

        UserInfoModel.instance().setUserID(user.getUid());
    }


    private void handleFacebookAccessToken(AccessToken token) {

        final ProgressDialog progressDialog = ProgressDialog.show(UserRegistrationActivity.this,
                "Please wait...",
                "Processing...",
                true
        );

        Log.d(TAG, "handleFacebookAccessToken:" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            updateUserInfoModel(true);
                            startChallenge();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(UserRegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                        facebookLogin.setEnabled(true);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void registerUser(View v) {

        final ProgressDialog progressDialog = ProgressDialog.show(UserRegistrationActivity.this,
                getString(R.string.please_wait),
                getString(R.string.processing),
                true
        );

        (firebaseAuth.createUserWithEmailAndPassword(
                txtEmailAddress.getText().toString(),
                txtPassword.getText().toString())
        ).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(
                                    UserRegistrationActivity.this,
                                    R.string.success,
                                    Toast.LENGTH_LONG).
                                    show();

                            updateUserInfoModel(false);
                            startChallenge();

                        } else {
                            Log.e(TAG, task.getException().toString());
                            Toast.makeText(UserRegistrationActivity.this,
                                    task.getException().getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
