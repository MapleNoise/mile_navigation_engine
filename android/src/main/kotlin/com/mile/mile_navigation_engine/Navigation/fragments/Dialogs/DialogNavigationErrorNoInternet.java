package com.mile.mile_navigation_engine.fragments.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.DialogFragment;
import com.mile.mile_navigation_engine.R;
import com.mile.mile_navigation_engine.interfaces.OnAlertClosed;

/**
 * Created by karl on 28/05/2018.
 */

public class DialogNavigationErrorNoInternet extends DialogFragment {

    private static final String TAG = DialogNavigationErrorNoInternet.class.getSimpleName();
    private OnAlertClosed listener;

    public DialogNavigationErrorNoInternet() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogNavigationErrorNoInternet newInstance(final OnAlertClosed listener) {
        DialogNavigationErrorNoInternet frag = new DialogNavigationErrorNoInternet();
        frag.setListener(listener);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_navigation_no_internet, container);

        final Button okButton = view.findViewById(R.id.location_settings_button);

        getDialog().setCanceledOnTouchOutside(false);

        //FirebaseAnalytics.getInstance(getActivity()).logEvent("rate_app",null);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClosed();
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public OnAlertClosed getListener() {
        return listener;
    }

    public void setListener(OnAlertClosed listener) {
        this.listener = listener;
    }
}

