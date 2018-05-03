package com.fitness.controller;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.fitness.dialog.ErrorDialogFragment;
import com.fitness.fitduel.R;

/**
 * A convenience class to handle displaying error dialogs.
 */
public class ErrorDialogHandler {

    FragmentManager mFragmentManager;

    public ErrorDialogHandler(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void showError(String errorMessage) {
        DialogFragment fragment = ErrorDialogFragment.newInstance(
                R.string.validationErrors, errorMessage);
        fragment.show(mFragmentManager, "error");
    }
}
