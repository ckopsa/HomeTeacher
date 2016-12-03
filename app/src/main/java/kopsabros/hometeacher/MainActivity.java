package kopsabros.hometeacher;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import kopsabros.hometeacher.Content.FamilyContent;
import kopsabros.hometeacher.Database.DBHelper;
import kopsabros.hometeacher.Notifications.AlarmReceiver;
import kopsabros.hometeacher.ViewFragments.AppointmentViewFragment;
import kopsabros.hometeacher.ViewFragments.FamilyAppointmentsFragment;
import kopsabros.hometeacher.ViewFragments.FamilyDetailFragment;


public class MainActivity extends AppCompatActivity implements
        AppointmentViewFragment.OnAppointmentListFragmentInteractionListener,
        FamilyDetailFragment.OnFamilyDetailFragmentInteractionListener,
        FamilyAppointmentsFragment.OnFamilyAppointmentListFragmentInteractionListener,
        SettingsFragment.OnSettingsFragmentInteractionListener {

    /*
     * AppointmentView Interfaces
     */

    @Override
    public void onAppointmentListCheckBoxInteraction(FamilyContent.Appointment appointment) {
        if (appointment != null) {
            appointment.setCompleted(true);
            if (appointment.getCompleted())
                Toast.makeText(getBaseContext(), "Appointment Completed", Toast.LENGTH_SHORT).show();
            DBHelper db = new DBHelper(getApplicationContext());
            db.updateAppointment(appointment);
            final RecyclerView recyclerView = ((RecyclerView) findViewById(kopsabros.hometeacher.R.id.appointmentlist));
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onAppointmentTimeClick(final FamilyContent.Family family) {
        final RecyclerView recyclerView = ((RecyclerView) findViewById(kopsabros.hometeacher.R.id.appointmentlist));

        final FamilyContent.Appointment appointment = family.getNextAppointment();
        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay, minute) -> {
            if (view.isShown()) {
                appointment.setHour(hourOfDay);
                appointment.setMinute(minute);
                Log.d("onTimeChanged", "time changed");
                recyclerView.getAdapter().notifyDataSetChanged();
                DBHelper db = new DBHelper(getApplicationContext());
                db.updateAppointment(appointment);
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
                appointment.getHour(),
                appointment.getMinute(), false);
        timePickerDialog.show();
    }

    @Override
    public void onAppointmentDateClick(final FamilyContent.Family family) {
        final RecyclerView recyclerView = ((RecyclerView) findViewById(kopsabros.hometeacher.R.id.appointmentlist));
        final FamilyContent.Appointment appointment = family.getNextAppointment();
        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            if (view.isShown()) {
                appointment.setYear(year);
                appointment.setMonth(1 + monthOfYear);
                appointment.setDay(dayOfMonth);
                Log.d("onDateChanged", "date changed");
                recyclerView.getAdapter().notifyDataSetChanged();
                DBHelper db = new DBHelper(getApplicationContext());
                db.updateAppointment(appointment);
            }
        };
        DatePickerDialog datePickerFragment = new DatePickerDialog(this,
                onDateSetListener,
                appointment.getYear(),
                appointment.getMonth() - 1,
                appointment.getDay());
        datePickerFragment.show();
    }

    @Override
    public void onRemindButtonPress(final FamilyContent.Family family) {
        String phoneNumber = family.getPhoneNumber();
        if (phoneNumber.equals("")) {
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_PHONE);
            final DBHelper db = new DBHelper(getApplicationContext());
            input.setHint("Phone Number");
            new AlertDialog.Builder(this)
                    .setTitle("Insert family phone number:")
                    .setIcon(android.R.drawable.ic_input_add)
                    .setView(input)
                    .setPositiveButton("Add", (dialog, whichButton) -> {
                        String phoneNumber1 = input.getText().toString();
                        if (!phoneNumber1.equals("")) {
                            family.setPhoneNumber(phoneNumber1);
                            db.updateFamily(family);
                            RecyclerView recyclerView = (RecyclerView) findViewById(kopsabros.hometeacher.R.id.family_member_list);
                            if (recyclerView != null)
                                recyclerView.getAdapter().notifyDataSetChanged();
                            onRemindButtonPress(family);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }

        if (family.getNextAppointment() != null) {
            String message = "Hey " + family.getFamilyName()
                    + "s! Just reminding you we have an appointment for "
                    + family.getNextAppointment().getDate()
                    + " at " + family.getNextAppointment().getTime() + ". See you then!";
            if (!phoneNumber.equals("")) {
                Uri number = Uri.parse("sms:" + phoneNumber);
                Intent sendSMS = new Intent(Intent.ACTION_VIEW, number);
                sendSMS.putExtra("sms_body", message);
                startActivity(sendSMS);
            }
        } else {
            if (!phoneNumber.equals("")) {
                String message = "Hey " + family.familyName + "s! When can we home teach you guys?";
                Uri number = Uri.parse("sms:" + phoneNumber);
                Intent sendSMS = new Intent(Intent.ACTION_VIEW, number);
                sendSMS.putExtra("sms_body", message);
                startActivity(sendSMS);
            }
        }
    }

    @Override
    public void onListButtonPress(FamilyContent.Family family) {
        if (family.getAppointmentList() == null || family.getAppointmentList().isEmpty()) {
            Toast.makeText(this, "No appointments to view", Toast.LENGTH_SHORT).show();
        } else {
            FamilyAppointmentsFragment fragment = FamilyAppointmentsFragment.newInstance(family);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(android.R.id.content, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onAppointmentAddFamily() {
        final View view = getLayoutInflater().inflate(kopsabros.hometeacher.R.layout.dialog_add_family, null);

        final RecyclerView appointmentList = (RecyclerView) findViewById(kopsabros.hometeacher.R.id.appointmentlist);

        new AlertDialog.Builder(this)
                .setTitle("Add a family:")
                .setIcon(android.R.drawable.ic_input_add)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                    String familyName = ((EditText) view.findViewById(kopsabros.hometeacher.R.id.dialog_add_family_familyname)).getText().toString();
//                        String phoneNumber = ((EditText) view.findViewById(R.id.dialog_add_family_phonenumber)).getText().toString();
//                        String emailAddress = ((EditText) view.findViewById(R.id.dialog_add_family_email_address)).getText().toString();
//                        String postalAddress = ((EditText) view.findViewById(R.id.dialog_add_family_postal_address)).getText().toString();
                    if (!familyName.equals("")) {
                        FamilyContent.Family family = new FamilyContent.Family(familyName.trim());
                        family.setPhoneNumber("");
                        family.setEmailAddress("");
                        family.setPostalAddress("");
                        FamilyContent.addFamily(family);
                        DBHelper db = new DBHelper(getApplicationContext());
                        db.putFamily(family);
                        appointmentList.getAdapter().notifyDataSetChanged();
                    }

                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }

    @Override
    public void onFamilyNameInteraction(FamilyContent.Family family) {
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
    public void onAppointmentListFragmentInteraction(final FamilyContent.Family family) {
        if (family.getNextAppointment() == null) {
            final RecyclerView recyclerView = ((RecyclerView) findViewById(kopsabros.hometeacher.R.id.appointmentlist));
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
            if (prev != null) {
                ft.remove(prev);
            }
            final DBHelper db = new DBHelper(getApplicationContext());
            final FamilyContent.Appointment appointment = new FamilyContent.Appointment(0, 0, 0, 0, 0, Integer.parseInt(family.getID()));
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            final int day = c.get(Calendar.DAY_OF_MONTH);
            int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay1, minute1) -> {
                if (view.isShown()) {
                    appointment.setHour(hourOfDay1);
                    appointment.setMinute(minute1);
                    Log.d("onTimeChanged", "time changed");
                    recyclerView.getAdapter().notifyDataSetChanged();
                    family.addAppointment(appointment);
                    db.updateAppointment(appointment);
                }
            };
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hourOfDay, minute, false);
            timePickerDialog.show();

            DatePickerDialog.OnDateSetListener onDateSetListener = (view, year1, monthOfYear, dayOfMonth) -> {
                if (view.isShown()) {
                    appointment.setYear(year1);
                    appointment.setMonth(1 + monthOfYear);
                    appointment.setDay(dayOfMonth);
                    Log.d("onDateChanged", "date changed");
                    recyclerView.getAdapter().notifyDataSetChanged();
                    db.putAppointment(appointment);
                }
            };
            DatePickerDialog datePickerFragment = new DatePickerDialog(this, onDateSetListener, year, month, day);
            datePickerFragment.show();
        }
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
                .setPositiveButton("Add", (dialog, whichButton) -> {
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
                        RecyclerView recyclerView = (RecyclerView) findViewById(kopsabros.hometeacher.R.id.family_member_list);
                        if (recyclerView != null)
                            recyclerView.getAdapter().notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
        findViewById(kopsabros.hometeacher.R.id.detail_family_title).setVisibility(View.VISIBLE);
    }

    @Override
    public void onTrashFamilyButtonPress(final FamilyContent.Family family) {
        final RecyclerView appointmentRecyclerView = (RecyclerView) findViewById(kopsabros.hometeacher.R.id.appointmentlist);
        new AlertDialog.Builder(this)
                .setTitle("Delete Family")
                .setMessage("Do you really want to delete the " + family.getFamilyName() + " family?")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    FamilyContent.removeFamily(family);
                    DBHelper db = new DBHelper(getApplicationContext());
                    db.deleteFamily(family);
                    appointmentRecyclerView.getAdapter().notifyDataSetChanged();
                    getSupportFragmentManager().popBackStack();
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onFamilyNameEdit(FamilyContent.Family family) {
        ((RecyclerView) findViewById(kopsabros.hometeacher.R.id.appointmentlist)).getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onTrashFamilyMemberButtonPress(final FamilyContent.FamilyMember familyMember) {
        final FamilyContent.Family family = FamilyContent.getFamily(familyMember.getFamilyID());
        final RecyclerView familyMemberRecyclerView = (RecyclerView) findViewById(kopsabros.hometeacher.R.id.family_member_list);
        new AlertDialog.Builder(this)
                .setTitle("Delete Family Member")
                .setMessage("Do you really want to remove " + familyMember.getName() +
                        " from the " + family.getFamilyName() + " family?")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    DBHelper db = new DBHelper(getApplicationContext());
                    db.deleteFamilyMember(familyMember);
                    family.removeFamilyMember(familyMember);
                    familyMemberRecyclerView.getAdapter().notifyDataSetChanged();
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onFamilyMemberNameEdit(final FamilyContent.FamilyMember familyMember) {
        final RecyclerView familyMemberRecyclerView = (RecyclerView) findViewById(kopsabros.hometeacher.R.id.family_member_list);
        String title;
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        if (!familyMember.getName().equals("")) {
            input.setText(familyMember.getName());
            title = "Edit Family Member Name:";
            new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setIcon(android.R.drawable.ic_menu_edit)
                    .setView(input)
                    .setPositiveButton(android.R.string.ok, (dialog, whichButton) -> {
                        String text = input.getText().toString();
                        familyMember.setName(text.trim());
                        DBHelper db = new DBHelper(getApplicationContext());
                        db.updateFamilyMember(familyMember);
                        familyMemberRecyclerView.getAdapter().notifyDataSetChanged();
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
        }
    }

    /*
     * Family Appointment List
     */

    @Override
    public void onListFragmentInteraction(FamilyContent.Appointment item) {

    }

    @Override
    public void onFamilyAppointmentListCheckBoxInteraction(FamilyContent.Appointment appointment, CheckBox checkBox) {
        final RecyclerView recyclerViewMain = (RecyclerView) findViewById(kopsabros.hometeacher.R.id.appointmentlist);
        recyclerViewMain.getAdapter().notifyDataSetChanged();
        if (appointment.getCompleted())
            Toast.makeText(getBaseContext(), "Appointment Completed", Toast.LENGTH_SHORT).show();
        DBHelper db = new DBHelper(getApplicationContext());
        db.updateAppointment(appointment);
    }

    @Override
    public void onTrashButtonPress(final FamilyContent.Appointment appointment) {
        final RecyclerView recyclerViewMain = (RecyclerView) findViewById(kopsabros.hometeacher.R.id.appointmentlist);
        new AlertDialog.Builder(this)
                .setTitle("Delete Appointment")
                .setMessage("Do you really want to delete this appointment?")
                .setIcon(android.R.drawable.ic_menu_delete)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    FamilyContent.getFamily(appointment.getFamilyID()).deleteAppointment(appointment);
                    DBHelper db = new DBHelper(getApplicationContext());
                    db.deleteAppointment(appointment);
                    RecyclerView recyclerView = (RecyclerView) findViewById(kopsabros.hometeacher.R.id.family_appointment_list);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    recyclerViewMain.getAdapter().notifyDataSetChanged();
                    if (FamilyContent.getFamily(appointment.getFamilyID()).getAppointmentList().isEmpty())
                        getSupportFragmentManager().popBackStack();
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onFamilyAppointmentDateClick(final FamilyContent.Appointment appointment) {
        final RecyclerView recyclerView = ((RecyclerView) findViewById(kopsabros.hometeacher.R.id.family_appointment_list));
        final RecyclerView recyclerViewMain = (RecyclerView) findViewById(kopsabros.hometeacher.R.id.appointmentlist);
        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            if (view.isShown()) {
                appointment.setYear(year);
                appointment.setMonth(1 + monthOfYear);
                appointment.setDay(dayOfMonth);
                Log.d("onDateChanged", "date changed");
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerViewMain.getAdapter().notifyDataSetChanged();
                DBHelper db = new DBHelper(getApplicationContext());
                db.updateAppointment(appointment);
            }
        };
        DatePickerDialog datePickerFragment = new DatePickerDialog(this,
                onDateSetListener,
                appointment.getYear(),
                appointment.getMonth() - 1,
                appointment.getDay());
        datePickerFragment.show();
    }

    @Override
    public void onFamilyAppointmentTimeClick(final FamilyContent.Appointment appointment) {
        final RecyclerView recyclerView = ((RecyclerView) findViewById(kopsabros.hometeacher.R.id.family_appointment_list));
        final RecyclerView recyclerViewMain = (RecyclerView) findViewById(kopsabros.hometeacher.R.id.appointmentlist);

        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay, minute) -> {
            if (view.isShown()) {
                appointment.setHour(hourOfDay);
                appointment.setMinute(minute);
                Log.d("onTimeChanged", "time changed");
                recyclerView.getAdapter().notifyDataSetChanged();
                recyclerViewMain.getAdapter().notifyDataSetChanged();
                DBHelper db = new DBHelper(getApplicationContext());
                db.updateAppointment(appointment);
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener,
                appointment.getHour(),
                appointment.getMinute(), false);
        timePickerDialog.show();
    }

    @Override
    public void onFamilyAppointmentAddAppointment(final FamilyContent.Family family) {
        final RecyclerView recyclerView = ((RecyclerView) findViewById(kopsabros.hometeacher.R.id.family_appointment_list));
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = (getSupportFragmentManager().findFragmentByTag("dialog"));
        if (prev != null) {
            ft.remove(prev);
        }
        final DBHelper db = new DBHelper(getApplicationContext());
        final FamilyContent.Appointment appointment = new FamilyContent.Appointment(0, 0, 0, 0, 0, Integer.parseInt(family.getID()));
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        DatePickerDialog.OnDateSetListener onDateSetListener = (view, year1, monthOfYear, dayOfMonth) -> {
            if (view.isShown()) {
                appointment.setYear(year1);
                appointment.setMonth(1 + monthOfYear);
                appointment.setDay(dayOfMonth);
                Log.d("onDateChanged", "date changed");
                recyclerView.getAdapter().notifyDataSetChanged();
                db.putAppointment(appointment);
            }
        };
        DatePickerDialog datePickerFragment = new DatePickerDialog(this, onDateSetListener, year, month, day);
        datePickerFragment.show();

        TimePickerDialog.OnTimeSetListener onTimeSetListener = (view, hourOfDay1, minute1) -> {
            if (view.isShown()) {
                appointment.setHour(hourOfDay1);
                appointment.setMinute(minute1);
                Log.d("onTimeChanged", "time changed");
                recyclerView.getAdapter().notifyDataSetChanged();
                family.addAppointment(appointment);
                db.updateAppointment(appointment);
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hourOfDay, minute, false);
        timePickerDialog.show();
    }

    /*
     * General Methods
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(kopsabros.hometeacher.R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(kopsabros.hometeacher.R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(kopsabros.hometeacher.R.id.frag_container, AppointmentViewFragment.newInstance(1));
        ft.commit();

        if (FamilyContent.FAMILIES.isEmpty()) {
            DBHelper db = new DBHelper(this);
            if (DBHelper.doesDatabaseExist(this)) {
                for (FamilyContent.Family family :
                        db.getFamilyList()) {
                    FamilyContent.addFamily(family);
                }

            }
        }

        scheduleCheckAlarm();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(kopsabros.hometeacher.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void onSendReportOptionSelect(MenuItem item) {
        String emailBody = "";
        for (FamilyContent.Family fam :
                FamilyContent.FAMILIES) {
            if (fam.getCurrentMonthAppointment() == null)
                emailBody += "The " + fam.getFamilyName() + "s weren't taught this month.";
            else if (fam.getCurrentMonthAppointment().getCompleted())
                emailBody += "The " + fam.getFamilyName() + "s were taught on " + fam.getCurrentMonthAppointment().getDate() + ".\n";
            else {
                emailBody += "The " + fam.getFamilyName() + "s weren't taught this month.";
            }
        }
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Home Teaching Report");
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    public void scheduleCheckAlarm() {
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // every 24 hours
        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 24 * 60 * 60 * 1000,
                SystemClock.elapsedRealtime() + 24 * 60 * 60 * 1000,
                pendingIntent
        );
    }

    public void onSettingsSelect(MenuItem item) {
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, SettingsFragment.newInstance("what's", "up")).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

