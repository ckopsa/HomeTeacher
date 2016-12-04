package kopsabros.hometeacher.adapters;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import kopsabros.hometeacher.content.FamilyContent;
import kopsabros.hometeacher.content.FamilyContent.Family;
import kopsabros.hometeacher.R;
import kopsabros.hometeacher.fragments.FamilyDetailFragment;

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
        holder.mFamilyMemberName.setText(mValues.get(position).getName());

        holder.mFamilyMemberName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFamilyMemberNameEdit(holder.mItem);
            }
        });
        holder.mTrashcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTrashFamilyMemberButtonPress(holder.mItem);
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mTrashcan.setVisibility(View.VISIBLE);
                holder.mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                     holder.mTrashcan.setVisibility(View.GONE);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFamilyMemberName;
        public final ImageButton mTrashcan;
        public FamilyContent.FamilyMember mItem;
        public final Handler mHandler = new Handler();


        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFamilyMemberName = (TextView) view.findViewById(R.id.family_member_name);
            mTrashcan = (ImageButton) view.findViewById(R.id.detail_member_trash_button);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mFamilyMemberName.getText() + "'";
        }
    }
}

