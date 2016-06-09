package coljamkop.tabtest.ViewFragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import coljamkop.tabtest.Content.FamilyContent;
import coljamkop.tabtest.R;

/**
 * A fragment representing a single Family detail screen.
  */
public class FamilyDetailFragment extends Fragment {
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

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.detail_toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(family.familyName);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_family_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (family != null) {
            ((TextView) rootView.findViewById(R.id.family_detail_name)).setText(family.familyName);
            ((TextView) rootView.findViewById(R.id.family_detail_phone_number)).setText(family.phoneNumber);
        }

        return rootView;
    }
}
