package com.mile.mile_navigation_engine.fragments.Dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.mile.mile_navigation_engine.R;
import com.mile.mile_navigation_engine.interfaces.OnAlertWithTextClosed;

/**
 * Created by karl on 28/05/2018.
 */

public class DialogReportProblem extends DialogFragment {

    private static final String TAG = DialogReportProblem.class.getSimpleName();
    private OnAlertWithTextClosed listener;

    public DialogReportProblem() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogReportProblem newInstance(final OnAlertWithTextClosed listener) {
        DialogReportProblem frag = new DialogReportProblem();
        frag.setListener(listener);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_report_problem, container);

        final Button okButton = view.findViewById(R.id.location_settings_button);
        final TextView comment = view.findViewById(R.id.text_dialog);

        getDialog().setCanceledOnTouchOutside(true);

        //FirebaseAnalytics.getInstance(getActivity()).logEvent("rate_app",null);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClosed(comment.getText().toString());
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public OnAlertWithTextClosed getListener() {
        return listener;
    }

    public void setListener(OnAlertWithTextClosed listener) {
        this.listener = listener;
    }
}

