package coljamkop.tabtest;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.nio.BufferUnderflowException;

import coljamkop.tabtest.Content.FamilyContent;
import coljamkop.tabtest.Dialogs.AddFamilyDialogFragment;
import coljamkop.tabtest.Dialogs.EditFamilyDialogFragment;
import coljamkop.tabtest.Dialogs.FamilyOptionsDialogFragment;
import coljamkop.tabtest.Dialogs.SendFamilySMSDialogFragment;
import coljamkop.tabtest.Pickers.DatePickerFragment;
import coljamkop.tabtest.Pickers.FamilyPickerFragment;
import coljamkop.tabtest.Pickers.TimePickerFragment;
import coljamkop.tabtest.ViewFragments.AppointmentViewFragment;
import coljamkop.tabtest.ViewFragments.FamilyViewFragment;

import static coljamkop.tabtest.ViewFragments.FamilyViewFragment.OnFamilyListFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements
        OnFamilyListFragmentInteractionListener,
        AppointmentViewFragment.OnAppointmentListFragmentInteractionListener,
        TimePickerFragment.OnTimePickerFragmentInteractionListener,
        DatePickerFragment.OnDatePickerFragmentInteractionListener,
        FamilyPickerFragment.OnFamilyPickerFragmentInteractionListener,
        AddFamilyDialogFragment.OnAddFamilyDialogFragmentInteractionListener,
        SendFamilySMSDialogFragment.OnSendFamilySMSDialogFragmentInteractionListener,
        FamilyOptionsDialogFragment.OnFamilyOptionsDialogFragmentInteractionListener,
        EditFamilyDialogFragment.OnEditFamilyDialogFragmentInteractionListener {

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
    public void onAddFamilyDialogConfirm(FamilyContent.Family newFamily) {
        FamilyContent.addFamily(newFamily);
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFamilyListFragmentInteraction(FamilyContent.Family family) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("family", family);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
        if (prev != null) {
            ft.remove(prev);
        }
        //ft.addToBackStack(null);
        SendFamilySMSDialogFragment.newInstance(bundle).show(ft, "dialog");
    }

    @Override
    public void onFamilyListAddFamilyButtonPress() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
        if (prev != null) {
            ft.remove(prev);
        }
        //ft.addToBackStack(null);
        AddFamilyDialogFragment.newInstance().show(ft, "dialog");
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
        //ft.addToBackStack(null);
        FamilyOptionsDialogFragment.newInstance(bundle).show(ft, "dialog");
    }

    @Override
    public void onEditFamilyOptionSelected(FamilyContent.Family family) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("family", family);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
        if (prev != null) {
            ft.remove(prev);
        }
        //ft.addToBackStack(null);
        EditFamilyDialogFragment.newInstance(bundle).show(ft, "dialog");
    }

    @Override
    public void onDeleteFamilyOptionSelected(FamilyContent.Family family) {
        FamilyContent.removeFamily(family);
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSendFamilySMSOptionSelected(FamilyContent.Family family) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("family", family);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
        if (prev != null) {
            ft.remove(prev);
        }
        //ft.addToBackStack(null);
        SendFamilySMSDialogFragment.newInstance(bundle).show(ft, "dialog");
    }

    @Override
    public void onEditFamilyFragmentConfirm(FamilyContent.Family family, String familyName, String phoneNumber) {
        family.setName(familyName);
        family.setPhoneNumber(phoneNumber);
        // mSectionsPagerAdapter.notifyDataSetChanged() doesn't update the recycleview after editing
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onSendFamilySMSDialogConfirm(FamilyContent.Family family, String smsMessage) {
        SmsManager smsPhone = SmsManager.getDefault();
        smsPhone.sendTextMessage(family.phoneNumber, null, smsMessage, null, null);
    }

    /*
     * AppointmentView Interfaces
     */

    @Override
    public void onListAddAppointmentButtonPress() {
        if (FamilyContent.FAMILIES.size() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(), "No families to visit.", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            FamilyPickerFragment.newInstance().show(ft, "dialog");
        }
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
        }
    }

    @Override
    public void onFamilyPickerSelect(FamilyContent.Family selectedFamily) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DatePickerFragment.newInstance(selectedFamily).show(ft, "dialog");
    }

    @Override
    public void onDatePickerSet(Bundle bundle) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
        if (prev != null) {
            ft.remove(prev);
        }
        //ft.addToBackStack(null);
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
        mSectionsPagerAdapter.notifyDataSetChanged();
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
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
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

