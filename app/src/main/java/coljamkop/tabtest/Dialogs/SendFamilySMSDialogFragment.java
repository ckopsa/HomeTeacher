package coljamkop.tabtest.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import coljamkop.tabtest.Content.FamilyContent;
import coljamkop.tabtest.R;

public class SendFamilySMSDialogFragment extends DialogFragment {

    OnSendFamilySMSDialogFragmentInteractionListener mListener;

    public static SendFamilySMSDialogFragment newInstance(Bundle bundle) {
        SendFamilySMSDialogFragment fragment = new SendFamilySMSDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_send_family_sms, null);

        builder.setTitle("Set up an appointment");
        final FamilyContent.Family family = (FamilyContent.Family) getArguments().getSerializable("family");
        final EditText smsMessage = (EditText) view.findViewById(R.id.dialog_send_family_sms_text);
        smsMessage.setText("Hey " + family.familyName + "s! When can we home teach you guys?");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Send Message", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onSendFamilySMSDialogConfirm(family, smsMessage.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SendFamilySMSDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSendFamilySMSDialogFragmentInteractionListener) {
            mListener = (OnSendFamilySMSDialogFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddFamilyDialogFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnSendFamilySMSDialogFragmentInteractionListener {
        public void onSendFamilySMSDialogConfirm(FamilyContent.Family family, String smsMessage);
    }
}
