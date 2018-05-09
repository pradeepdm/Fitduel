package com.fitness.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by prade on 5/6/2018.
 */

public class UserInfoModel implements Serializable {

    private static UserInfoModel instance;
    private String currentBalance;
    private String userID;
    private String profilePic;
    private String email;
    private String name;
    private HashMap<String, Long> userTransactions;

    public UserInfoModel() {
        currentBalance = String.valueOf(0);
        userTransactions = new HashMap<String, Long>();
    }

    public static void setInstance(UserInfoModel instance) {
        UserInfoModel.instance = instance;
    }

    public static UserInfoModel instance() {

        if (instance == null) {
            instance = new UserInfoModel();
        }
        return instance;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void resetUserTransaction() {
        this.userTransactions = new HashMap<String, Long>();
    }

    public HashMap<String, Long> getUserTransactions() {
        return userTransactions;
    }

    public void addTransaction(String chargeID, Long amount) {
        this.userTransactions.put(chargeID, amount);
    }
}
