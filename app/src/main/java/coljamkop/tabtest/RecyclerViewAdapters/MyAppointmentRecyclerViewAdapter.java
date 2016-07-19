package coljamkop.tabtest.RecyclerViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import coljamkop.tabtest.Content.FamilyContent.Appointment;
import coljamkop.tabtest.R;
import coljamkop.tabtest.ViewFragments.AppointmentViewFragment.OnAppointmentListFragmentInteractionListener;

import static coljamkop.tabtest.Content.FamilyContent.Family;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Appointment} and makes a call to the
 * specified {@link OnAppointmentListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAppointmentRecyclerViewAdapter extends RecyclerView.Adapter<MyAppointmentRecyclerViewAdapter.ViewHolder> {

    private final List<Family> mValues;
    private final OnAppointmentListFragmentInteractionListener mListener;

    public MyAppointmentRecyclerViewAdapter(List<Family> families, OnAppointmentListFragmentInteractionListener listener) {
        mValues = families;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        final Appointment nextAppointment = holder.mItem.getNextAppointment();
        if (nextAppointment == null) {
            holder.mNoAppointment.setVisibility(View.VISIBLE);
            holder.mCheckBox.setVisibility(View.INVISIBLE);
            holder.mTimeView.setVisibility(View.INVISIBLE);
            holder.mDateView.setVisibility(View.INVISIBLE);
        } else {
            holder.mDateView.setText(mValues.get(position).getNextAppointment().getDate());
            holder.mTimeView.setText(mValues.get(position).getNextAppointment().getTime());
            holder.mNoAppointment.setVisibility(View.INVISIBLE);
            holder.mCheckBox.setVisibility(View.VISIBLE);
            holder.mTimeView.setVisibility(View.VISIBLE);
            holder.mDateView.setVisibility(View.VISIBLE);
        }
        holder.mFamilyNameView.setText(mValues.get(position).toString());
        holder.mFamilyNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFamilyNameInteraction(holder.mItem);
            }
        });
        holder.mNoAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAppointmentListFragmentInteraction(holder.mItem);
            }
        });
        holder.mTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAppointmentTimeClick(holder.mItem);
            }
        });
        holder.mDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAppointmentDateClick(holder.mItem);
            }
        });
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAppointmentListCheckBoxInteraction(nextAppointment);
            }
        });
        holder.mReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRemindButtonPress(holder.mItem);
            }
        });
//        holder.mListButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onListButtonPress(holder.mItem);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDateView;
        public final TextView mTimeView;
        public final TextView mFamilyNameView;
        public final ImageButton mCheckBox;
        public final TextView mNoAppointment;
//        public final ImageButton mListButton;
        public final ImageButton mReminderButton;

        public Family mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.appointment_date);
            mTimeView = (TextView) view.findViewById(R.id.appointment_time);
            mNoAppointment = (TextView) view.findViewById(R.id.appointment_none);
            mFamilyNameView = (TextView) view.findViewById(R.id.appointment_family);
            mCheckBox = (ImageButton) view.findViewById(R.id.appointment_checkbox_button);
//            mListButton = (ImageButton) view.findViewById(R.id.appointment_list_button);
            mReminderButton = (ImageButton) view.findViewById(R.id.appointment_reminder_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFamilyNameView.getText() + "'";
        }
    }
}
