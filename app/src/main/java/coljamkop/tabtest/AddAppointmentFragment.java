package coljamkop.tabtest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import coljamkop.tabtest.Content.AppointmentContent;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnAddAppointmentFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddAppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAppointmentFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnAddAppointmentFragmentInteractionListener mListener;

    public AddAppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAppointmentFragment newInstance() {
        AddAppointmentFragment fragment = new AddAppointmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_add_appointment, container, false);
        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                EditText name = (EditText) view.findViewById(R.id.editFamilyName);
                EditText date = (EditText) view.findViewById(R.id.editDate);
                EditText time = (EditText) view.findViewById(R.id.editTime);
                mListener.onAddAppointmentButtonPress(new AppointmentContent.Appointment(
                        name.getText().toString(),
                        date.getText().toString(),
                        time.getText().toString()));
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddAppointmentFragmentInteractionListener) {
            mListener = (OnAddAppointmentFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddAppointmentFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAddAppointmentFragmentInteractionListener {
        // TODO: Update argument type and name
        void onAddAppointmentButtonPress(AppointmentContent.Appointment appointment);
    }
}
