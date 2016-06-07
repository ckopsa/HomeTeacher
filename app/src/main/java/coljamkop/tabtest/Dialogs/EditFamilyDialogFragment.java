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

public class EditFamilyDialogFragment extends DialogFragment {

    OnEditFamilyDialogFragmentInteractionListener mListener;

    public static EditFamilyDialogFragment newInstance(Bundle bundle) {
        EditFamilyDialogFragment fragment = new EditFamilyDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_family, null);
        final FamilyContent.Family family = (FamilyContent.Family) getArguments().getSerializable("family");
        final EditText familyName = (EditText) view.findViewById(R.id.dialog_add_family_familyname);
        final EditText phoneNumber = (EditText) view.findViewById(R.id.dialog_add_family_phonenumber);

        familyName.setText(family.toString());
        phoneNumber.setText(family.phoneNumber);

        builder.setTitle("Edit the " + family.toString() + " family");

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mListener.onEditFamilyFragmentConfirm(family, familyName.getText().toString(),
                        phoneNumber.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                EditFamilyDialogFragment.this.getDialog().cancel();
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEditFamilyDialogFragmentInteractionListener) {
            mListener = (OnEditFamilyDialogFragmentInteractionListener) context;
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

    public interface OnEditFamilyDialogFragmentInteractionListener {
        public void onEditFamilyFragmentConfirm(FamilyContent.Family family, String familyName, String phoneNumber);
    }
}
