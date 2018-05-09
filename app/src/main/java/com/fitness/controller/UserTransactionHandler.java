package com.fitness.controller;

import com.fitness.model.UserInfoModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by pradeep on 5/1/2018.
 */

public class UserTransactionHandler {

    static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private static String USERPROFILE = "UserProfile";

    public static void addFunds(String chargeId, Long funds) {
        UserInfoModel.instance().setCurrentBalance(String.valueOf(funds / 100));

        UserInfoModel.instance().addTransaction(chargeId, funds);
        storeUserProfileToFirebase();
    }

    public static void storeUserProfileToFirebase() {
        databaseReference.child(USERPROFILE).
                child(UserInfoModel.instance().getUserID()).
                setValue(UserInfoModel.instance());
    }

    public static Map<String, ?> withDrawFunds() {

        return UserInfoModel.instance().getUserTransactions();
    }

    public static void resetUserTransactions() {
        UserInfoModel.instance().setCurrentBalance(String.valueOf(0L));
        UserInfoModel.instance().resetUserTransaction();
        storeUserProfileToFirebase();
    }

    public static void initializeUserInfoModel(FirebaseUser user) {

        databaseReference.child(USERPROFILE).child(user.getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserInfoModel userInfoModel = dataSnapshot.getValue(UserInfoModel.class);
                        if (userInfoModel != null) {
                            UserInfoModel.setInstance(userInfoModel);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

}
