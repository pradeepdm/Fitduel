package com.fitness.controller;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by prade on 5/1/2018.
 */

public class SharedPreferencesHandler {

    private static Context preferenceContext;
    private static SharedPreferencesHandler instance;
    private static String TRANSACTIONS = "Transactions";

    public static void setContext(Context context) {
        preferenceContext = context;
    }

    public static void saveTransaction(String chargeId, Long funds) {
        SharedPreferences preferences = preferenceContext.getSharedPreferences(TRANSACTIONS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(chargeId, funds);
        editor.apply();
    }

    public static Map<String, ?> retrieveTransactions() {
        SharedPreferences preferences = preferenceContext.getSharedPreferences(TRANSACTIONS, Context.MODE_PRIVATE);
        return preferences.getAll();
    }
}
