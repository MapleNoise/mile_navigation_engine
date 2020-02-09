package com.mile.mile_navigation_engine.fragments.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.DialogFragment;
import com.mile.mile_navigation_engine.R;
import com.mile.mile_navigation_engine.interfaces.OnDialogActionClicked;


/**
 * Created by karl on 28/05/2018.
 */

public class DialogBatterySaverOn extends DialogFragment {

    private static final String TAG = DialogBatterySaverOn.class.getSimpleName();
    private OnDialogActionClicked listener;

    public DialogBatterySaverOn() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogBatterySaverOn newInstance(final OnDialogActionClicked listener) {
        DialogBatterySaverOn frag = new DialogBatterySaverOn();
        frag.setListener(listener);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_battery_saver_on, container);

        final Button settingsButton = view.findViewById(R.id.location_settings_button);
        final Button cancelButton = view.findViewById(R.id.cancel_button);

        getDialog().setCanceledOnTouchOutside(false);

        //FirebaseAnalytics.getInstance(getActivity()).logEvent("rate_app",null);

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onAgreed();
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeclined();
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public OnDialogActionClicked getListener() {
        return listener;
    }

    public void setListener(OnDialogActionClicked listener) {
        this.listener = listener;
    }
}

