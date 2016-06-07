package coljamkop.tabtest.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import coljamkop.tabtest.Content.FamilyContent;

public class FamilyOptionsDialogFragment extends DialogFragment {

    OnFamilyOptionsDialogFragmentInteractionListener mListener;

    public static FamilyOptionsDialogFragment newInstance(Bundle bundle) {
        FamilyOptionsDialogFragment fragment = new FamilyOptionsDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        final FamilyContent.Family family = (FamilyContent.Family) getArguments().getSerializable("family");

        builder.setTitle("Choose an option:");
        String[] options = {"Edit Family", "Delete Family", "Send Family Message"};
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    mListener.onEditFamilyOptionSelected(family);
                } else if (which == 1) {
                    mListener.onDeleteFamilyOptionSelected(family);
                } else if (which == 2) {
                    mListener.onSendFamilySMSOptionSelected(family);
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFamilyOptionsDialogFragmentInteractionListener) {
            mListener = (OnFamilyOptionsDialogFragmentInteractionListener) context;
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

    public interface OnFamilyOptionsDialogFragmentInteractionListener {

        void onEditFamilyOptionSelected(FamilyContent.Family family);

        void onDeleteFamilyOptionSelected(FamilyContent.Family family);

        void onSendFamilySMSOptionSelected(FamilyContent.Family family);
    }
}
