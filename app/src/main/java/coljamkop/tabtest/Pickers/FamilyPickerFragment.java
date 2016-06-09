package coljamkop.tabtest.Pickers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import coljamkop.tabtest.Content.FamilyContent;

/**
 * Created by Aghbac on 5/27/16.
 */
public class FamilyPickerFragment extends DialogFragment {

    OnFamilyPickerFragmentInteractionListener mListener;

    public static FamilyPickerFragment newInstance() {
        FamilyPickerFragment fragment = new FamilyPickerFragment();
        return fragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pick a family")
                .setItems(FamilyContent.getFamilyNames(), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        mListener.onFamilyPickerSelect(FamilyContent.FAMILIES.get(which));
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFamilyPickerFragmentInteractionListener) {
            mListener = (OnFamilyPickerFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFamilyListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFamilyPickerFragmentInteractionListener {
        void onFamilyPickerSelect(FamilyContent.Family selectedFamily);
    }
}
