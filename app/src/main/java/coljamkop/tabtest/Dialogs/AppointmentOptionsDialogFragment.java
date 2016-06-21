package coljamkop.tabtest.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import coljamkop.tabtest.Content.FamilyContent;

public class AppointmentOptionsDialogFragment extends DialogFragment {

    OnAppointmentOptionsDialogFragmentInteractionListener mListener;

    public static AppointmentOptionsDialogFragment newInstance(Bundle bundle) {
        AppointmentOptionsDialogFragment fragment = new AppointmentOptionsDialogFragment();
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
        String[] options = {"Edit Appointment", "Delete Appointment", "Remind Family"};
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0) {
                    mListener.onEditAppointmentOptionSelected(family);
                } else if (which == 1) {
                    mListener.onDeleteAppointmentOptionSelected(family);
                } else if (which == 2) {
                    mListener.onSendFamilyReminderOptionSelected(family);
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAppointmentOptionsDialogFragmentInteractionListener) {
            mListener = (OnAppointmentOptionsDialogFragmentInteractionListener) context;
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

    public interface OnAppointmentOptionsDialogFragmentInteractionListener {

        void onEditAppointmentOptionSelected(FamilyContent.Family family);

        void onDeleteAppointmentOptionSelected(FamilyContent.Family family);

        void onSendFamilyReminderOptionSelected(FamilyContent.Family family);
    }
}
