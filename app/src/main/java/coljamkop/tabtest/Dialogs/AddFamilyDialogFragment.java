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

public class AddFamilyDialogFragment extends DialogFragment {

    OnAddFamilyDialogFragmentInteractionListener mListener;

    public static AddFamilyDialogFragment newInstance() {
        AddFamilyDialogFragment fragment = new AddFamilyDialogFragment();
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_family, null);

        builder.setTitle("Add a family");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Add Family", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText familyName = (EditText) view.findViewById(R.id.dialog_add_family_familyname);
                        EditText phoneNumber = (EditText) view.findViewById(R.id.dialog_add_family_phonenumber);
                        EditText emailAddress = (EditText) view.findViewById(R.id.dialog_add_family_email_address);
                        EditText postalAddress = (EditText) view.findViewById(R.id.dialog_add_family_postal_address);
                        FamilyContent.Family fam = new FamilyContent.Family(familyName.getText().toString());
                        fam.setPhoneNumber(phoneNumber.getText().toString());
                        fam.setEmailAddress(emailAddress.getText().toString());
                        fam.setPostalAddress(postalAddress .getText().toString());
                        mListener.onAddFamilyDialogConfirm(fam);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AddFamilyDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddFamilyDialogFragmentInteractionListener) {
            mListener = (OnAddFamilyDialogFragmentInteractionListener) context;
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

    public interface OnAddFamilyDialogFragmentInteractionListener {
        void onAddFamilyDialogConfirm(FamilyContent.Family newFamily);
    }
}
