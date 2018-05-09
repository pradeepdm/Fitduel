package com.fitness.fitduel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fitness.controller.ErrorDialogHandler;
import com.fitness.fitduel.PaymentTransactionActivity;
import com.fitness.fitduel.R;


/**
 *
 */
public class AddFundsFragment extends Fragment implements View.OnClickListener {

    TextView valueTen, valueTwentyFive, valueFifty, valueHundred;
    private TextView currentSelectedValue;
    private Button selectPaymentButton;
    private ErrorDialogHandler errorDialogHandler;

    public AddFundsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View addFunds = inflater.inflate(
                R.layout.fragment_add_funds,
                container,
                false
        );

        valueTen = addFunds.findViewById(R.id.value_10);
        valueTwentyFive = addFunds.findViewById(R.id.value_25);
        valueFifty = addFunds.findViewById(R.id.value_50);
        valueHundred = addFunds.findViewById(R.id.value_100);
        selectPaymentButton = addFunds.findViewById(R.id.select_payment);
        valueTen.setOnClickListener(this);
        valueTwentyFive.setOnClickListener(this);
        valueFifty.setOnClickListener(this);
        valueHundred.setOnClickListener(this);
        selectPaymentButton.setOnClickListener(this);

        return addFunds;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.value_10:
                resetCurrentSelectedValue(valueTen);
                currentSelectedValue = valueTen;
                break;
            case R.id.value_25:
                resetCurrentSelectedValue(valueTwentyFive);
                currentSelectedValue = valueTwentyFive;
                break;
            case R.id.value_50:
                resetCurrentSelectedValue(valueFifty);
                currentSelectedValue = valueFifty;
                break;
            case R.id.value_100:
                resetCurrentSelectedValue(valueHundred);
                currentSelectedValue = valueHundred;
                break;
            case R.id.select_payment:
                selectPaymentOption();
                break;
        }
    }

    private void selectPaymentOption() {

        if (currentSelectedValue != null) {
            String amountToLoad = currentSelectedValue.getText().toString();
            amountToLoad = amountToLoad.replace("$", "");

            Intent intent = new Intent(getContext(), PaymentTransactionActivity.class);
            intent.putExtra("amount", amountToLoad);
            startActivity(intent);
        } else {
            Toast.makeText(getContext(), "Please select the amount to be added", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetCurrentSelectedValue(TextView value) {

        if (currentSelectedValue != null) {
            currentSelectedValue.setBackground(getResources().getDrawable(R.drawable.rounded_textview));
        }
        else
            currentSelectedValue = value;

        value.setBackground(getResources().getDrawable(R.drawable.rounded_textview_primary));
    }
}
