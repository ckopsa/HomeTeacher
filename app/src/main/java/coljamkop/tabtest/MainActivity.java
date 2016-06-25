package coljamkop.tabtest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import coljamkop.tabtest.Content.FamilyContent;
import coljamkop.tabtest.Database.DBHelper;
import coljamkop.tabtest.Dialogs.AppointmentOptionsDialogFragment;
import coljamkop.tabtest.Dialogs.FamilyOptionsDialogFragment;
import coljamkop.tabtest.Notifications.NotificationPublisher;
import coljamkop.tabtest.Pickers.DatePickerFragment;
import coljamkop.tabtest.Pickers.TimePickerFragment;
import coljamkop.tabtest.ViewFragments.AppointmentViewFragment;
import coljamkop.tabtest.ViewFragments.FamilyDetailFragment;
import coljamkop.tabtest.ViewFragments.FamilyViewFragment;

import static coljamkop.tabtest.ViewFragments.FamilyViewFragment.OnFamilyListFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements
        OnFamilyListFragmentInteractionListener,
        AppointmentViewFragment.OnAppointmentListFragmentInteractionListener,
        TimePickerFragment.OnTimePickerFragmentInteractionListener,
        DatePickerFragment.OnDatePickerFragmentInteractionListener,
        FamilyOptionsDialogFragment.OnFamilyOptionsDialogFragmentInteractionListener,
        FamilyDetailFragment.OnFamilyDetailFragmentInteractionListener,
        AppointmentOptionsDialogFragment.OnAppointmentOptionsDialogFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    /*
     * FamilyView Interfaces
     */
    @Override
    public void onFamilyListFragmentInteraction(FamilyContent.Family family) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("family", family);
        FamilyDetailFragment fragment = new FamilyDetailFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(android.R.id.content, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onFamilyListAddFamilyButtonPress() {
        final View view = getLayoutInflater().inflate(R.layout.dialog_add_family, null);

        final RecyclerView familyList = (RecyclerView) findViewById(R.id.familylist);
        final RecyclerView appointmentList = (RecyclerView) findViewById(R.id.appointmentlist);

        new AlertDialog.Builder(this)
            .setTitle("Add a family:")
            .setIcon(android.R.drawable.ic_input_add)
            .setView(view)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String familyName = ((EditText) view.findViewById(R.id.dialog_add_family_familyname)).getText().toString();
                    String phoneNumber = ((EditText) view.findViewById(R.id.dialog_add_family_phonenumber)).getText().toString();
                    String emailAddress = ((EditText) view.findViewById(R.id.dialog_add_family_email_address)).getText().toString();
                    String postalAddress = ((EditText) view.findViewById(R.id.dialog_add_family_postal_address)).getText().toString();
                    if (!familyName.equals("")) {
                        FamilyContent.Family family = new FamilyContent.Family(familyName);
                        family.setPhoneNumber(phoneNumber);
                        family.setEmailAddress(emailAddress);
                        family.setPostalAddress(postalAddress);
                        FamilyContent.addFamily(family);
                        DBHelper db = new DBHelper(getApplicationContext());
                        db.putFamily(family);
                        familyList.getAdapter().notifyDataSetChanged();
                        appointmentList.getAdapter().notifyDataSetChanged();
                    }

                }
            })
            .setNegativeButton(android.R.string.no, null)
            .show();
    }

    @Override
    public void onFamilyListLongClick(FamilyContent.Family family) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("family", family);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        FamilyOptionsDialogFragment.newInstance(bundle).show(ft, "dialog");
    }

    @Override
    public void onEditFamilyOptionSelected(final FamilyContent.Family family) {
        final View view = getLayoutInflater().inflate(R.layout.dialog_add_family, null);

        final RecyclerView familyList = (RecyclerView) findViewById(R.id.familylist);
        final RecyclerView appointmentList = (RecyclerView) findViewById(R.id.appointmentlist);

        final EditText familyName = (EditText) view.findViewById(R.id.dialog_add_family_familyname);
        final EditText phoneNumber = (EditText) view.findViewById(R.id.dialog_add_family_phonenumber);
        final EditText emailAddress = (EditText) view.findViewById(R.id.dialog_add_family_email_address);
        final EditText postalAddress = (EditText) view.findViewById(R.id.dialog_add_family_postal_address);

        familyName.setText(family.getFamilyName());
        phoneNumber.setText(family.getPhoneNumber());
        emailAddress.setText(family.getEmailAddress());
        postalAddress.setText(family.getPostalAddress());

        new AlertDialog.Builder(this)
                .setTitle("Edit the " + family.getFamilyName() + " family:")
                .setIcon(android.R.drawable.ic_menu_edit)
                .setView(view)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (!familyName.equals("")) {
                            family.setFamilyName(familyName.getText().toString());
                            family.setPhoneNumber(phoneNumber.getText().toString());
                            family.setEmailAddress(emailAddress.getText().toString());
                            family.setPostalAddress(postalAddress.getText().toString());
                            familyList.getAdapter().notifyDataSetChanged();
                            appointmentList.getAdapter().notifyDataSetChanged();
                            DBHelper db = new DBHelper(getApplicationContext());
                            db.updateFamily(family);
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onDeleteFamilyOptionSelected(final FamilyContent.Family family) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Family")
                .setMessage("Do you really want to delete this family?")
                .setIcon(android.R.drawable.ic_delete)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        DBHelper db = new DBHelper(getApplicationContext());
                        db.deleteFamily(family);
                        FamilyContent.removeFamily(family);

                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.familylist);
                        recyclerView.getAdapter().notifyDataSetChanged();
                        recyclerView = (RecyclerView) findViewById(R.id.appointmentlist);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    @Override
    public void onSendFamilySMSOptionSelected(final FamilyContent.Family family) {
        String message = "Hey " + family.familyName + "s! When can we home teach you guys?";
        final EditText input = new EditText(this);
        input.setText(message);
        new AlertDialog.Builder(this)
                .setTitle("Set up an appointment:")
                .setIcon(android.R.drawable.ic_menu_edit)
                .setView(input)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SmsManager smsPhone = SmsManager.getDefault();
                        smsPhone.sendTextMessage(family.phoneNumber, null, input.getText().toString(), null, null);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    /*
     * AppointmentView Interfaces
     */

    @Override
    public void onListAddAppointmentButtonPress() {
        if (FamilyContent.FAMILIES.size() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "No families to visit.", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Pick a family")
                    .setItems(FamilyContent.getFamilyNames(), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
                            if (prev != null) {
                                ft.remove(prev);
                            }
                            DatePickerFragment.newInstance(FamilyContent.FAMILIES.get(which)).show(ft, "dialog");
                        }
                    }).show();
        }
    }

    @Override
    public void onAppointmentListCheckBoxInteraction(FamilyContent.Family family, CheckBox mCheckBox) {
        if (family.getNextAppointment().getCompleted())
            Toast.makeText(getBaseContext(), "Appointment Completed", Toast.LENGTH_SHORT).show();
        DBHelper db = new DBHelper(getApplicationContext());
        db.updateAppointment(family.getNextAppointment());
    }

    @Override
    public void onAppointmentListLongClick(FamilyContent.Family family) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("family", family);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        AppointmentOptionsDialogFragment.newInstance(bundle).show(ft, "dialog");

    }

    @Override
    public void onAppointmentListFragmentInteraction(FamilyContent.Family family) {
        if (family.getNextAppointment() == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
            if (prev != null) {
                ft.remove(prev);
            }
            //ft.addToBackStack(null);
            DatePickerFragment.newInstance(family).show(ft, "dialog");
        } else {
            Bundle bundle = new Bundle();
            bundle.putSerializable("family", family);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            AppointmentOptionsDialogFragment.newInstance(bundle).show(ft, "dialog");
        }
    }

    @Override
    public void onEditAppointmentOptionSelected(FamilyContent.Family family) {

    }

    @Override
    public void onDeleteAppointmentOptionSelected(final FamilyContent.Family family) {
        if (family.getNextAppointment() == null) {
            Toast.makeText(getBaseContext(), "No appointment to delete", Toast.LENGTH_SHORT).show();
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Appointment")
                    .setMessage("Do you really want to delete this appointment?")
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            DBHelper db = new DBHelper(getApplicationContext());
                            db.deleteAppointment(family.getNextAppointment());
                            family.deleteNextAppointment();
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.appointmentlist);
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    @Override
    public void onSendFamilyReminderOptionSelected(final FamilyContent.Family family) {
        String message = "Hey " + family.getFamilyName()
                + "s! Just reminding you we have an appointment for "
                + family.getNextAppointment().getDate()
                + " at " + family.getNextAppointment().getTime() + ". See you then!";
        final EditText input = new EditText(this);
        input.setText(message);
        new AlertDialog.Builder(this)
                .setTitle("Send the " + family.getFamilyName() + "s a reminder:")
                .setIcon(android.R.drawable.ic_popup_reminder)
                .setView(input)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SmsManager smsPhone = SmsManager.getDefault();
                        smsPhone.sendTextMessage(family.phoneNumber, null, input.getText().toString(), null, null);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onDatePickerSet(Bundle bundle) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
        if (prev != null) {
            ft.remove(prev);
        }
        TimePickerFragment.newInstance(bundle).show(ft, "dialog");
    }

    @Override
    public void onTimePickerSet(Bundle bundle) {
        FamilyContent.Family family = (FamilyContent.Family) bundle.getSerializable("family");
        if (family != null) {
            family.addAppointment(bundle.getInt("year"),
                    bundle.getInt("month"),
                    bundle.getInt("day"),
                    bundle.getInt("hourOfDay"),
                    bundle.getInt("minute"));
        }
        DBHelper db = new DBHelper(getApplicationContext());
        db.putAppointment(family.getNextAppointment());
        ((RecyclerView) findViewById(R.id.appointmentlist)).getAdapter().notifyDataSetChanged();
    }

    /*
     * FamilyDetailView Interfaces
     */

    @Override
    public void onCallButtonPress(String phoneNumber) {
        if (!phoneNumber.equals("")) {
            Uri number = Uri.parse("tel:" + phoneNumber);
            Intent dial = new Intent(Intent.ACTION_VIEW, number);
            startActivity(dial);
        }
    }

    @Override
    public void onSMSButtonPress(String phoneNumber) {
        if (!phoneNumber.equals("")) {
            Uri number = Uri.parse("sms:" + phoneNumber);
            Intent sendSMS = new Intent(Intent.ACTION_VIEW, number);
            startActivity(sendSMS);
        }
    }

    @Override
    public void onEmailButtonPress(String emailAddress) {
        if (!emailAddress.equals("")) {
            Uri uriMail = Uri.parse("mailto:" + emailAddress);
            Intent sendMail = new Intent(Intent.ACTION_VIEW, uriMail);
            startActivity(sendMail);
        }
    }

    @Override
    public void onMapButtonPress(String postalAddress) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + postalAddress);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void addFamilyMember(final FamilyContent.Family family) {
        final EditText input = new EditText(this);
        final DBHelper db = new DBHelper(getApplicationContext());
        input.setHint("First Name");
        new AlertDialog.Builder(this)
                .setTitle("Add a family member:")
                .setIcon(android.R.drawable.ic_input_add)
                .setView(input)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String firstName = input.getText().toString();
                        if (!firstName.equals("")) {
                            FamilyContent.FamilyMember familyMember =
                                    new FamilyContent.FamilyMember(
                                            firstName,
                                            Integer.parseInt(family.getID()),
                                            null,
                                            null,
                                            null);
                            family.addMember(familyMember);
                            db.putFamilyMember(familyMember);
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.family_member_list);
                            if (recyclerView != null)
                                recyclerView.getAdapter().notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    /*
     * General Methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
        }

        if (FamilyContent.FAMILIES.isEmpty()) {
            DBHelper db = new DBHelper(this);
            if (db.doesDatabaseExist(this)) {
                for (FamilyContent.Family family:
                     db.getFamilyList()) {
                    FamilyContent.addFamily(family);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        sendNotification();
    }

    public void onSendReportOptionSelect(MenuItem item) {
        String emailBody = "";
        for (FamilyContent.Family fam :
                FamilyContent.FAMILIES) {
            if (fam.getNextAppointment() == null)
                emailBody += "The " + fam.getFamilyName() + "s weren't taught this month.";
            else if (fam.getNextAppointment().getCompleted())
                emailBody += "The " + fam.getFamilyName() + "s were taught on " + fam.getNextAppointment().getDate()+ ".\n";
            else {
                emailBody += "The " + fam.getFamilyName() + "s weren't taught this month.";
            }
        }
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Home Teaching Report");
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public void sendNotification() {
        String unappointedFamilies = "";
        boolean isReminderableFamily = false;
        for (FamilyContent.Family family :
                FamilyContent.FAMILIES) {
            if (family.getNextAppointment() == null) {
                isReminderableFamily = true;
                unappointedFamilies += "The " + family.getFamilyName() + " family\n";
            }
        }
        if (isReminderableFamily) {
            Log.d("Notification", " is good");
            Intent alarmIntent = new Intent(this, NotificationPublisher.class);
            alarmIntent.putExtra("message", unappointedFamilies);
            alarmIntent.putExtra("title", "Reminder to contact: ");

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            //TODO: For demo set after 5 seconds.
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 5 * 1000, pendingIntent);
        }
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0)
                return AppointmentViewFragment.newInstance(1);
            else
                return FamilyViewFragment.newInstance(1);

        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Appointments";
                case 1:
                    return "Families";
            }
            return null;
        }
    }
}

