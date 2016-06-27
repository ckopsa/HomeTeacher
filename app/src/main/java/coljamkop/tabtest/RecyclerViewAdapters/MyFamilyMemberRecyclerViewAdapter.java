package coljamkop.tabtest.RecyclerViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import coljamkop.tabtest.Content.FamilyContent;
import coljamkop.tabtest.Content.FamilyContent.Family;
import coljamkop.tabtest.R;
import coljamkop.tabtest.ViewFragments.FamilyDetailFragment;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Family} and makes a call to the
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
