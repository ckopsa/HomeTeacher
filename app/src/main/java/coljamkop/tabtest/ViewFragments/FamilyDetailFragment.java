package coljamkop.tabtest.ViewFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import coljamkop.tabtest.Content.FamilyContent;
import coljamkop.tabtest.R;
import coljamkop.tabtest.RecyclerViewAdapters.MyFamilyMemberRecyclerViewAdapter;
import coljamkop.tabtest.RecyclerViewAdapters.MyFamilyRecyclerViewAdapter;

/**
 * A fragment representing a single Family detail screen.
  */
public class FamilyDetailFragment extends Fragment {

    OnFamilyDetailFragmentInteractionListener mListener;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_FAMILY_ID = "family";

    /**
     * The dummy content this fragment is presenting.
     */
    private FamilyContent.Family family;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FamilyDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        family = (FamilyContent.Family) getArguments().getSerializable(ARG_FAMILY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_family_detail, container, false);

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.detail_toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(family.familyName + " Family");
        }

        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.detail_fab_send_sms);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.addFamilyMember(family);
            }
        });

        // Hide view objects if non-existent
        if (family != null) {
            if (family.phoneNumber != null) {
                ((TextView) rootView.findViewById(R.id.detail_phone_number)).setText(family.phoneNumber);
                ImageButton callButton = ((ImageButton) rootView.findViewById(R.id.detail_call_button));
                callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onCallButtonPress(family.getPhoneNumber());
                    }
                });
                ImageButton smsButton = ((ImageButton) rootView.findViewById(R.id.detail_sms_button));
                smsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onSMSButtonPress(family.getPhoneNumber());
                    }
                });
            }
            else /*if (family.phoneNumber == "")*/ {
                rootView.findViewById(R.id.detail_phone_number).setVisibility(View.GONE);
                rootView.findViewById(R.id.detail_call_button).setVisibility(View.GONE);
                rootView.findViewById(R.id.detail_sms_button).setVisibility(View.GONE);
            }
            if (family.emailAddress != null) {
                ((TextView) rootView.findViewById(R.id.detail_email)).setText(family.emailAddress);
                ImageButton emailButton = ((ImageButton) rootView.findViewById(R.id.detail_email_button));
                emailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onEmailButtonPress(family.getEmailAddress());
                    }
                });
            }
            else {
                rootView.findViewById(R.id.detail_email).setVisibility(View.GONE);
                rootView.findViewById(R.id.detail_email_button).setVisibility(View.GONE);

            }
            if (family.postalAddress != null) {
                ((TextView) rootView.findViewById(R.id.detail_address)).setText(family.postalAddress);
                ImageButton mapButton = ((ImageButton) rootView.findViewById(R.id.detail_map_button));
                mapButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onMapButtonPress(family.getPostalAddress());
                    }
                });
            }
            else {
                rootView.findViewById(R.id.detail_address).setVisibility(View.GONE);
                rootView.findViewById(R.id.detail_map_button).setVisibility(View.GONE);
            }

        }

        // Set the adapter
        if (rootView.findViewById(R.id.family_member_list) instanceof RecyclerView) {
            Context context = rootView.getContext();
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.family_member_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            assert (family.getMemberNameArray() != null);
            recyclerView.setAdapter(new MyFamilyMemberRecyclerViewAdapter(family.getMemberList(), mListener));
        }
        if (family.getMemberNameArray() != null) {
            rootView.findViewById(R.id.detail_family_title).setVisibility(View.GONE);
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFamilyDetailFragmentInteractionListener) {
            mListener = (OnFamilyDetailFragmentInteractionListener) context;
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

    public interface OnFamilyDetailFragmentInteractionListener {
        void onCallButtonPress(String phoneNumber);
        void onSMSButtonPress(String phoneNumber);
        void onEmailButtonPress(String emailAddress);
        void onMapButtonPress(String postalAddress);

        void addFamilyMember(FamilyContent.Family family);
    }
}
