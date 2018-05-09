package com.fitness.fitduel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fitness.controller.UserTransactionHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class UserLoginActivity extends AppCompatActivity {


    private EditText txtEmailLogin;
    private EditText txtPwd;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        txtEmailLogin = (EditText) findViewById(R.id.input_email);
        txtPwd = (EditText) findViewById(R.id.input_password);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void btnUserLogin_Click(View v) {
        final ProgressDialog progressDialog = ProgressDialog.show(UserLoginActivity.this, getString(R.string.please_wait), getString(R.string.processing), true);

        (firebaseAuth.signInWithEmailAndPassword(txtEmailLogin.getText().toString(), txtPwd.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {

                            Toast.makeText(UserLoginActivity.this, R.string.success_login, Toast.LENGTH_LONG).show();
                            Intent i = new Intent(UserLoginActivity.this, ChallengesActivity.class);
                            UserTransactionHandler.initializeUserInfoModel(firebaseAuth.getCurrentUser());
                            i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                            startActivity(i);

                        } else {
                            Log.e("UserLoginActivity", task.getException().toString());
                            Toast.makeText(UserLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
}
