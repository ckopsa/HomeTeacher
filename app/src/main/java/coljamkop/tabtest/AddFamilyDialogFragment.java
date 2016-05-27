package coljamkop.tabtest;

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

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Add Family", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText familyName = (EditText) view.findViewById(R.id.dialog_add_family_familyname);
                        mListener.onAddFamilySelect(new FamilyContent.Family(familyName.getText().toString()));
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
        public void onAddFamilySelect(FamilyContent.Family newFamily);
    }
}
