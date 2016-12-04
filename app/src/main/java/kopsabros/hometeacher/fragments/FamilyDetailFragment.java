package kopsabros.hometeacher.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import kopsabros.hometeacher.content.FamilyContent;
import kopsabros.hometeacher.database.DBHelper;
import kopsabros.hometeacher.R;
import kopsabros.hometeacher.adapters.MyFamilyMemberRecyclerViewAdapter;

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
        final View rootView = inflater.inflate(R.layout.fragment_family_detail, container, false);

        final CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.detail_toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(family.familyName + " Family");
            appBarLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title;
                    final EditText input = new EditText(getContext());
                    input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                    if (!family.getFamilyName().equals("")) {
                        input.setText(family.getFamilyName());
                        title = "Edit Family Name:";
                        new AlertDialog.Builder(getContext())
                                .setTitle(title)
                                .setIcon(android.R.drawable.ic_menu_edit)
                                .setView(input)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        String text = input.getText().toString();
                                        family.setFamilyName(text.trim());
                                        appBarLayout.setTitle(text.trim() + " Family");
                                        mListener.onFamilyNameEdit(family);
                                        DBHelper db = new DBHelper(getContext());
                                        db.updateFamily(family);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .show();
                    }
                }
            });
        }

        ImageButton trashFamily = (ImageButton) rootView.findViewById(R.id.detail_trash_button);
        trashFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onTrashFamilyButtonPress(family);
            }
        });

        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.detail_fab_send_sms);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.addFamilyMember(family);
            }
        });
        if (family.getMemberList().isEmpty())
            rootView.findViewById(R.id.detail_family_title).setVisibility(View.INVISIBLE);

        // Hide view objects if non-existent
        if (family != null) {
            phoneNumberSetup(rootView);
            emailAddressSetup(rootView);
            postalAddressSetup(rootView);

            if (family.emailAddress.equals("")) {
                ((TextView)rootView.findViewById(R.id.detail_email)).setText("Add email address");
                rootView.findViewById(R.id.detail_email_button).setVisibility(View.INVISIBLE);
            } if (family.phoneNumber.equals("")) {
                TextView phoneNumber = ((TextView) rootView.findViewById(R.id.detail_phone_number));
                phoneNumber.setText("Add phone number");
                rootView.findViewById(R.id.detail_call_button).setVisibility(View.INVISIBLE);
                rootView.findViewById(R.id.detail_sms_button).setVisibility(View.INVISIBLE);
            } if (family.postalAddress.equals("")) {
                ((TextView)rootView.findViewById(R.id.detail_address)).setText("Add postal address");
                rootView.findViewById(R.id.detail_map_button).setVisibility(View.INVISIBLE);
            }
        }

        // Set the adapter
        if (rootView.findViewById(R.id.family_member_list) instanceof RecyclerView) {
            Context context = rootView.getContext();
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.family_member_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            if (family.getMemberNameArray() != null) {
                recyclerView.setAdapter(new MyFamilyMemberRecyclerViewAdapter(family.getMemberList(), mListener));
            }
        }
        return rootView;
    }

    private void phoneNumberSetup(final View rootView) {
        final TextView phoneNumber = ((TextView) rootView.findViewById(R.id.detail_phone_number));
        phoneNumber.setText(family.getPhoneNumber());
        phoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title;
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                if (!family.getPhoneNumber().equals("")) {
                    input.setText(phoneNumber.getText().toString());
                    title = "Edit phone number:";
                    new AlertDialog.Builder(getContext())
                            .setTitle(title)
                            .setIcon(android.R.drawable.ic_menu_edit)
                            .setView(input)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String text = input.getText().toString();
                                    family.setPhoneNumber(text.trim());
                                    phoneNumber.setText(text.trim());
                                    DBHelper db = new DBHelper(getContext());
                                    db.updateFamily(family);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                } else {
                    input.setHint("Phone Number");
                    title = "Add a phone number:";
                    new AlertDialog.Builder(getContext())
                            .setTitle(title)
                            .setIcon(android.R.drawable.ic_input_add)
                            .setView(input)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String text = input.getText().toString();
                                    family.setPhoneNumber(text.trim());
                                    phoneNumber.setText(text.trim());
                                    rootView.findViewById(R.id.detail_call_button).setVisibility(View.VISIBLE);
                                    rootView.findViewById(R.id.detail_sms_button).setVisibility(View.VISIBLE);
                                    DBHelper db = new DBHelper(getContext());
                                    db.updateFamily(family);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            }
        });
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

    private void emailAddressSetup(final View rootView) {
        final TextView email = ((TextView) rootView.findViewById(R.id.detail_email));
        email.setText(family.getEmailAddress());
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title;
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                if (!family.getEmailAddress().equals("")) {
                    input.setText(email.getText().toString());
                    title = "Edit email address:";
                    new AlertDialog.Builder(getContext())
                            .setTitle(title)
                            .setIcon(android.R.drawable.ic_menu_edit)
                            .setView(input)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String text = input.getText().toString();
                                    family.setEmailAddress(text.trim());
                                    email.setText(text.trim());
                                    DBHelper db = new DBHelper(getContext());
                                    db.updateFamily(family);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                } else {
                    input.setHint("Email Address");
                    title = "Add an email address:";
                    new AlertDialog.Builder(getContext())
                            .setTitle(title)
                            .setIcon(android.R.drawable.ic_input_add)
                            .setView(input)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String text = input.getText().toString();
                                    family.setEmailAddress(text.trim());
                                    email.setText(text.trim());
                                    rootView.findViewById(R.id.detail_email_button).setVisibility(View.VISIBLE);
                                    DBHelper db = new DBHelper(getContext());
                                    db.updateFamily(family);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            }
        });
        ImageButton emailButton = ((ImageButton) rootView.findViewById(R.id.detail_email_button));
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEmailButtonPress(family.getEmailAddress());
            }
        });

    }

    private void postalAddressSetup(final View rootView) {
        final TextView address = ((TextView) rootView.findViewById(R.id.detail_address));
        address.setText(family.getPostalAddress());
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title;
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
                if (!family.getPostalAddress().equals("")) {
                    input.setText(address.getText().toString());
                    title = "Edit postal address:";
                    new AlertDialog.Builder(getContext())
                            .setTitle(title)
                            .setIcon(android.R.drawable.ic_menu_edit)
                            .setView(input)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String text = input.getText().toString();
                                    family.setPostalAddress(text.trim());
                                    address.setText(text.trim());
                                    DBHelper db = new DBHelper(getContext());
                                    db.updateFamily(family);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                } else {
                    input.setHint("Postal Address");
                    title = "Add a postal address:";
                    new AlertDialog.Builder(getContext())
                            .setTitle(title)
                            .setIcon(android.R.drawable.ic_input_add)
                            .setView(input)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String text = input.getText().toString();
                                    family.setPostalAddress(text.trim());
                                    address.setText(text.trim());
                                    rootView.findViewById(R.id.detail_map_button).setVisibility(View.VISIBLE);
                                    DBHelper db = new DBHelper(getContext());
                                    db.updateFamily(family);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                }
            }
        });
        ImageButton emailButton = ((ImageButton) rootView.findViewById(R.id.detail_map_button));
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMapButtonPress(family.getPostalAddress());
            }
        });
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

        void onTrashFamilyButtonPress(FamilyContent.Family family);

        void onFamilyNameEdit(FamilyContent.Family family);

        void onTrashFamilyMemberButtonPress(FamilyContent.FamilyMember familyMember);

        void onFamilyMemberNameEdit(FamilyContent.FamilyMember familyMember);
    }
}
