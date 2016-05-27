package coljamkop.tabtest.RecyclerViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import coljamkop.tabtest.ViewFragments.FamilyViewFragment.OnFamilyListFragmentInteractionListener;
import coljamkop.tabtest.Content.FamilyContent.Family;
import coljamkop.tabtest.R;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Family} and makes a call to the
 * specified {@link OnFamilyListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFamilyRecyclerViewAdapter extends RecyclerView.Adapter<MyFamilyRecyclerViewAdapter.ViewHolder> {

    private final List<Family> mValues;
    private final OnFamilyListFragmentInteractionListener mListener;

    public MyFamilyRecyclerViewAdapter(List<Family> items, OnFamilyListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_family, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).familyName);

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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public Family mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.family_family_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
