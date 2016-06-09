package coljamkop.tabtest.Pickers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import coljamkop.tabtest.Content.FamilyContent;

/**
 * Created by Aghbac on 5/25/16.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    OnDatePickerFragmentInteractionListener mListener;

    public static DatePickerFragment newInstance(FamilyContent.Family selectedFamily) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("family", selectedFamily);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // onDateSet called twice
        // workaround for this bug found @
        // http://stackoverflow.com/questions/12436073/datepicker-ondatechangedlistener-called-twice
        if(view.isShown()) {
            Bundle bundle = getArguments();
            if(bundle.getInt("year", -1) == -1) {
                bundle.putInt("year", year);
                bundle.putInt("month", month + 1);
                bundle.putInt("day", day);
                mListener.onDatePickerSet(bundle);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDatePickerFragmentInteractionListener) {
            mListener = (OnDatePickerFragmentInteractionListener) context;
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

    public interface OnDatePickerFragmentInteractionListener {
        void onDatePickerSet(Bundle bundle);
    }
}
