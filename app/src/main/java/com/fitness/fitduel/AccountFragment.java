package com.fitness.fitduel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fitness.model.FundsModel;
import com.fitness.module.RetrofitFactory;
import com.fitness.module.Utils;
import com.fitness.service.StripeService;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private static final String TAG = "AccountFragment";
    Button addFundsButton, withdrawFunds;
    private TextView currentBalance;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View accountFragment = inflater.inflate(
                R.layout.fragment_account,
                container,
                false
        );

        addFundsButton = accountFragment.findViewById(R.id.add_funds);
        withdrawFunds = accountFragment.findViewById(R.id.withdraw_funds);
        currentBalance = accountFragment.findViewById(R.id.current_funds);

        if (FundsModel.getInstance() != null)
            currentBalance.setText(String.format(FundsModel.currentFunds.toString(), true));

        addFundsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frame, new AddFundsFragment()).
                        addToBackStack(null).commit();
            }
        });

        withdrawFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refundToTheCustomerAccount();

            }
        });

        return accountFragment;
    }

    private void refundToTheCustomerAccount() {
        Map<String, ?> transactions = FundsModel.instance(null).withDrawFunds();
        for (Map.Entry<String, ?> entry : transactions.entrySet()) {
            doRefund(entry.getKey());
        }

        Intent intent = new Intent(getContext(), ChallengesActivity.class);
        startActivity(intent);
    }

    private void doRefund(final String key) {

        final StripeService service = RetrofitFactory.getInstance().create(StripeService.class);
        try {

            Map<String, Object> params = new HashMap<>();
            params.put("charge", key);

            Call<ResponseBody> call = service.refund(params);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> paramOne,
                                       @NonNull Response<ResponseBody> response) {

                    Log.i(TAG, "Refund with " + key + " successful");
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> paramOne, @NonNull Throwable t) {
                    t.printStackTrace();
                    Utils.displayError(t.getMessage(), getContext());
                }
            });

        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
