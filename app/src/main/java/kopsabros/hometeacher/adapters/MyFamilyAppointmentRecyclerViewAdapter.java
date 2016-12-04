package kopsabros.hometeacher.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import kopsabros.hometeacher.content.FamilyContent;
import kopsabros.hometeacher.R;
import kopsabros.hometeacher.fragments.FamilyAppointmentsFragment;

/**
 * {@link RecyclerView.Adapter} that can display a {@link kopsabros.hometeacher.content.FamilyContent.Appointment} and makes a call to the
 * specified {@link FamilyAppointmentsFragment.OnFamilyAppointmentListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFamilyAppointmentRecyclerViewAdapter extends RecyclerView.Adapter<MyFamilyAppointmentRecyclerViewAdapter.ViewHolder> {

    private final List<FamilyContent.Appointment> mValues;
    private final FamilyAppointmentsFragment.OnFamilyAppointmentListFragmentInteractionListener mListener;

    public MyFamilyAppointmentRecyclerViewAdapter(List<FamilyContent.Appointment> items, FamilyAppointmentsFragment.OnFamilyAppointmentListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_family_appointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        if (holder.mItem != null) {
            holder.mDateView.setText(holder.mItem.getDate());
            holder.mTimeView.setText(holder.mItem.getTime());
            holder.mCheckBox.setChecked(holder.mItem.getCompleted());
            holder.mTimeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onFamilyAppointmentTimeClick(holder.mItem);
                }
            });
            holder.mDateView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onFamilyAppointmentDateClick(holder.mItem);
                }
            });
            holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mItem.setCompleted(holder.mCheckBox.isChecked());
                    mListener.onFamilyAppointmentListCheckBoxInteraction(holder.mItem, holder.mCheckBox);
                }
            });
            holder.mTrashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onTrashButtonPress(holder.mItem);
                }
            });

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction(holder.mItem);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDateView;
        public final TextView mTimeView;
        public final CheckBox mCheckBox;
        public final ImageButton mTrashButton;

        public FamilyContent.Appointment mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDateView = (TextView) view.findViewById(R.id.family_appointment_date);
            mTimeView = (TextView) view.findViewById(R.id.family_appointment_time);
            mCheckBox = (CheckBox) view.findViewById(R.id.family_appointment_checkBox);
            mTrashButton = (ImageButton) view.findViewById(R.id.family_family_appointment_trash_button);
        }
    }
}
