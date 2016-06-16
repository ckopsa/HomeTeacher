package coljamkop.tabtest.RecyclerViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import coljamkop.tabtest.Content.FamilyContent;
import coljamkop.tabtest.Content.FamilyContent.Family;
import coljamkop.tabtest.R;
import coljamkop.tabtest.ViewFragments.FamilyDetailFragment;
import coljamkop.tabtest.ViewFragments.FamilyViewFragment.OnFamilyListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Family} and makes a call to the
 * specified {@link OnFamilyListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFamilyMemberRecyclerViewAdapter extends RecyclerView.Adapter<MyFamilyMemberRecyclerViewAdapter.ViewHolder> {

    private final List<FamilyContent.FamilyMember> mValues;
    private final FamilyDetailFragment.OnFamilyDetailFragmentInteractionListener mListener;

    public MyFamilyMemberRecyclerViewAdapter(List<FamilyContent.FamilyMember> items, FamilyDetailFragment.OnFamilyDetailFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_family_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).getName());

//        holder.mView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    //mListener.onFamilyMemberListFragmentInteraction(holder.mItem);
//                }
//            }
//        });
//
//        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
//
//            @Override
//            public boolean onLongClick(View v) {
//                //mListener.onFamilyMemberListLongClick(holder.mItem);
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public FamilyContent.FamilyMember mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.family_member_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
