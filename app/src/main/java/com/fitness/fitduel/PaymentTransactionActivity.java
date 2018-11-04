package com.fitness.fitduel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fitness.controller.ErrorDialogHandler;
import com.fitness.controller.UserTransactionHandler;
import com.fitness.module.RetrofitFactory;
import com.fitness.module.Utils;
import com.fitness.service.StripeService;
import com.google.gson.Gson;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;
import com.stripe.model.Charge;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("FieldCanBeLocal")
public class PaymentTransactionActivity extends AppCompatActivity {

    public static final String PUBLISHABLE_KEY =
            "test key here";
    private static final String TAG = "PaymentTransaction";
    private final int TO_DOLLARS = 100;
    private CardInputWidget cardInputWidget;
    private Button goButton;
    private Integer amountToAdd;
    private ErrorDialogHandler errorDialogHandler;
    private ProgressDialog progressDialog;

    public PaymentTransactionActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_input);
        cardInputWidget = findViewById(R.id.card_input_widget);
        goButton = findViewById(R.id.go);

        Intent intent = getIntent();
        amountToAdd = Integer.valueOf(intent.getStringExtra("amount"));

        errorDialogHandler = new ErrorDialogHandler(this.getSupportFragmentManager());

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = ProgressDialog.
                        show(PaymentTransactionActivity.this,
                                getString(R.string.please_wait),
                                getString(R.string.processing),
                                true
                        );

                Card cardToSave = cardInputWidget.getCard();
                if (cardToSave == null) {

                    progressDialog.dismiss();
                    Toast.makeText(
                            PaymentTransactionActivity.this,
                            getString(R.string.card_details),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {

                    boolean validateCard = cardToSave.validateCard() &&
                            cardToSave.validateCVC() &&
                            cardToSave.validateNumber() &&
                            cardToSave.validateExpiryDate();

                    if (validateCard) {

                        Stripe stripe = new Stripe(PaymentTransactionActivity.this, PUBLISHABLE_KEY);
                        stripe.createToken(
                                cardToSave,
                                new TokenCallback() {

                                    @Override
                                    public void onError(Exception error) {

                                        progressDialog.dismiss();
                                        Log.e(TAG, error.getMessage());
                                        Utils.displayError(error.getLocalizedMessage(), getApplicationContext());
                                    }

                                    @Override
                                    public void onSuccess(final Token token) {
                                        addFundsTransaction(token);
                                    }
                                }
                        );
                    }
                }
            }
        });

    }

    private void addFundsTransaction(Token token) {

        final StripeService service = RetrofitFactory.getInstance().create(StripeService.class);
        try {

            Call<ResponseBody> call = service.charge(createParamsForCharge(amountToAdd, token.getId()));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> paramOne,
                                       @NonNull Response<ResponseBody> response) {
                    finishCharge(response);
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> paramOne, @NonNull Throwable t) {
                    t.printStackTrace();
                    progressDialog.dismiss();
                    Utils.displayError(t.getMessage(), PaymentTransactionActivity.this);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    private Map<String, Object> createParamsForCharge(Integer price, String sourceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", price * TO_DOLLARS);
        params.put("currency", "usd");
        params.put("description", "Add Funds charge");
        params.put("source", sourceId);
        return params;
    }

    private void finishCharge(Response<ResponseBody> response) {

        Charge chargeInstance = null;
        try {
            chargeInstance = new Gson().fromJson(response.body().string(), Charge.class);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getLocalizedMessage());
        }

        UserTransactionHandler.addFunds(chargeInstance.getId(), chargeInstance.getAmount());
        progressDialog.dismiss();
        Intent intent = new Intent(this, ChallengesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Log.i(TAG, "Funds successfully added to the account");
        Toast.makeText(this, getString(R.string.successfully_added), Toast.LENGTH_SHORT).show();
    }
}
