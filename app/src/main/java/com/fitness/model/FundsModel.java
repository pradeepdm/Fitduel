package com.fitness.model;

import android.content.Context;

import com.fitness.controller.SharedPreferencesHandler;

import java.util.Map;

/**
 * Created by prade on 5/1/2018.
 */

public class FundsModel {

    public static Long currentFunds;
    private static Context context;
    private static FundsModel instance;

    private FundsModel(Context context) {
        this.context = context;
        currentFunds = 0L;
    }

    public static synchronized FundsModel instance(Context context) {

        if (instance == null) {
            instance = new FundsModel(context);
            SharedPreferencesHandler.setContext(context);
        }
        return instance;
    }

    public static FundsModel getInstance() {
        return instance;
    }

    public void addFunds(String chargeId, Long funds) {
        currentFunds = funds / 100;
        SharedPreferencesHandler.saveTransaction(chargeId, funds);
    }

    public Map<String, ?> withDrawFunds() {
        currentFunds = 0L;
        return SharedPreferencesHandler.retrieveTransactions();
    }
}
