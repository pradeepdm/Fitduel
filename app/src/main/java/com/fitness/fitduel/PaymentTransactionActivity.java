package com.fitness.fitduel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fitness.controller.ErrorDialogHandler;
import com.fitness.dialog.ProgressDialogFragment;
import com.fitness.model.FundsModel;
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
import rx.subscriptions.CompositeSubscription;

public class PaymentTransactionActivity extends AppCompatActivity {

    public static final String PUBLISHABLE_KEY =
            "pk_test_VFuFSUOngDWxUMBoBmgbkPuu";
    private static final String TAG = "PaymentTransaction";
    private final int TO_DOLLARS = 100;
    private CardInputWidget cardInputWidget;
    private Button goButton;
    private CompositeSubscription mCompositeSubscription;
    private ProgressDialogFragment mProgressDialogFragment;
    private Integer amountToAdd;
    private ErrorDialogHandler errorDialogHandler;

    public PaymentTransactionActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_input);
        cardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        goButton = findViewById(R.id.go);

        Intent intent = getIntent();
        amountToAdd = Integer.valueOf(intent.getStringExtra("amount"));

        errorDialogHandler = new ErrorDialogHandler(this.getSupportFragmentManager());

        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card cardToSave = cardInputWidget.getCard();
                if (cardToSave == null) {
                    Toast.makeText(
                            PaymentTransactionActivity.this,
                            "Please enter the card details",
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
       /* try {

            Observable<ResponseBody> stripeResponse = service.charge(createParamsForCharge(amountToAdd, token.getId()));
            final FragmentManager fragmentManager = getSupportFragmentManager();

            mCompositeSubscription.add(stripeResponse
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe(
                            new Action0() {
                                @Override
                                public void call() {
                                    if (mProgressDialogFragment != null &&
                                            !mProgressDialogFragment.isAdded())
                                        mProgressDialogFragment.show(fragmentManager, "progress");
                                }
                            })
                    .doOnUnsubscribe(
                            new Action0() {
                                @Override
                                public void call() {
                                    if (mProgressDialogFragment != null
                                            && mProgressDialogFragment.isVisible()) {
                                        mProgressDialogFragment.dismiss();
                                    }
                                }
                            })
                    .subscribe(
                            new Action1<ResponseBody>() {
                                @Override
                                public void call(ResponseBody s) {
                                    finishCharge();

                                }
                            },
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    displayError(throwable.getLocalizedMessage());
                                }
                            }));

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }*/
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
                    Utils.displayError(t.getMessage(), getApplicationContext());
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

        FundsModel.instance(getApplicationContext()).addFunds(chargeInstance.getId(), chargeInstance.getAmount());
        Intent intent = new Intent(this, ChallengesActivity.class);
        startActivity(intent);
        Log.i(TAG, "Funds successfully added to the account");
        Toast.makeText(this, "Funds successfully added to the account", Toast.LENGTH_SHORT).show();
    }
}
