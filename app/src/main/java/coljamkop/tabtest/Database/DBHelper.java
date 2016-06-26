package coljamkop.tabtest.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import coljamkop.tabtest.Content.FamilyContent;

import static coljamkop.tabtest.Database.TableData.*;

/**
 * Created by Aghbac on 6/20/16.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "home_teaching.db";

    private String SQL_CREATE_APPOINTMENT_TABLE = "CREATE TABLE "
            + AppointmentInfo.TABLE_NAME
            + " ("
            + AppointmentInfo._ID + " INTEGER PRIMARY KEY, "
            + AppointmentInfo.COLUMN_NAME_ENTRY_ID + " TEXT, "
            + AppointmentInfo.COLUMN_NAME_DATE + " TEXT, "
            + AppointmentInfo.COLUMN_NAME_TIME + " TEXT, "
            + AppointmentInfo.COLUMN_NAME_FAMILY_ID + " TEXT, "
            + AppointmentInfo.COLUMN_NAME_COMPLETED + " INTEGER);";

    private String SQL_CREATE_FAMILY_MEMBER_TABLE = "CREATE TABLE "
            + FamilyMemberInfo.TABLE_NAME
            + " ("
            + FamilyMemberInfo._ID + " INTEGER PRIMARY KEY, "
            + FamilyMemberInfo.COLUMN_NAME_ENTRY_ID + " TEXT, "
            + FamilyMemberInfo.COLUMN_NAME_FIRST_NAME + " TEXT, "
            + FamilyMemberInfo.COLUMN_NAME_FAMILY_ID + " TEXT, "
            + FamilyMemberInfo.COLUMN_NAME_PHONE_NUMBER + " TEXT, "
            + FamilyMemberInfo.COLUMN_NAME_BIRTHDAY + " TEXT);";


    private String SQL_CREATE_FAMILY_TABLE = "CREATE TABLE "
            + FamilyInfo.TABLE_NAME
            + " ("
            + FamilyInfo._ID + " INTEGER PRIMARY KEY, "
            + FamilyInfo.COLUMN_NAME_ENTRY_ID + " TEXT, "
            + FamilyInfo.COLUMN_NAME_NAME + " TEXT, "
            + FamilyInfo.COLUMN_NAME_PHONE_NUMBER + " TEXT, "
            + FamilyInfo.COLUMN_NAME_EMAIL_ADDRESS + " TEXT, "
            + FamilyInfo.COLUMN_NAME_POSTAL_ADDRESS + " TEXT);";

    private static final String SQL_DELETE_APPOINTMENT_TABLE =
            "DROP TABLE IF EXISTS " + FamilyInfo.TABLE_NAME;

    private static final String SQL_DELETE_FAMILY_MEMBER_TABLE =
            "DROP TABLE IF EXISTS " + FamilyMemberInfo.TABLE_NAME;

    private static final String SQL_DELETE_FAMILY_TABLE =
            "DROP TABLE IF EXISTS " + FamilyInfo.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_APPOINTMENT_TABLE);
        db.execSQL(SQL_CREATE_FAMILY_MEMBER_TABLE);
        db.execSQL(SQL_CREATE_FAMILY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_APPOINTMENT_TABLE);
        db.execSQL(SQL_DELETE_FAMILY_MEMBER_TABLE);
        db.execSQL(SQL_DELETE_FAMILY_TABLE);
        onCreate(db);
    }

    public static boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    /*
     * Appointment Database Functions
     */
    public void putAppointment(FamilyContent.Appointment appointment) {
        ContentValues values = new ContentValues();
        values.put(AppointmentInfo.COLUMN_NAME_ENTRY_ID, appointment.getID());
        values.put(AppointmentInfo.COLUMN_NAME_DATE, appointment.getDate());
        values.put(AppointmentInfo.COLUMN_NAME_TIME, appointment.getTime());
        values.put(AppointmentInfo.COLUMN_NAME_FAMILY_ID, appointment.getFamilyID());
        values.put(AppointmentInfo.COLUMN_NAME_COMPLETED, appointment.getCompleted());
        getWritableDatabase().insert(AppointmentInfo.TABLE_NAME, null, values);
        Log.d("Database", "appointment added");
    }
    public boolean updateAppointment(FamilyContent.Appointment appointment) {
        ContentValues values = new ContentValues();
        values.put(AppointmentInfo.COLUMN_NAME_ENTRY_ID, appointment.getID());
        values.put(AppointmentInfo.COLUMN_NAME_DATE, appointment.getDate());
        values.put(AppointmentInfo.COLUMN_NAME_TIME, appointment.getTime());
        values.put(AppointmentInfo.COLUMN_NAME_FAMILY_ID, appointment.getFamilyID());
        values.put(AppointmentInfo.COLUMN_NAME_COMPLETED, appointment.getCompleted());
        Log.d("Database", "appointment updated");
        return getWritableDatabase().update(AppointmentInfo.TABLE_NAME, values, AppointmentInfo.COLUMN_NAME_ENTRY_ID + "=" + appointment.getID(), null) > 0;
    }

    public void deleteAppointment(FamilyContent.Appointment appointment) {
        // Define 'where' part of query.
        String selection = AppointmentInfo.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { appointment.getID() };
        // Issue SQL statement.
        getWritableDatabase().delete(AppointmentInfo.TABLE_NAME, selection, selectionArgs);
        Log.d("Database", "appointment deleted");
    }

    public Cursor getAppointment() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                AppointmentInfo.COLUMN_NAME_ENTRY_ID,
                AppointmentInfo.COLUMN_NAME_DATE,
                AppointmentInfo.COLUMN_NAME_TIME,
                AppointmentInfo.COLUMN_NAME_FAMILY_ID,
                AppointmentInfo.COLUMN_NAME_COMPLETED
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                AppointmentInfo.COLUMN_NAME_DATE + " DESC";

        Cursor c = getReadableDatabase().query(
                AppointmentInfo.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return c;
    }

    public List<FamilyContent.Appointment> getAllFamilyAppointmentList(int familyId) {
        ArrayList<FamilyContent.Appointment> appointments = new ArrayList<>();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                AppointmentInfo.COLUMN_NAME_ENTRY_ID,
                AppointmentInfo.COLUMN_NAME_DATE,
                AppointmentInfo.COLUMN_NAME_TIME,
                AppointmentInfo.COLUMN_NAME_FAMILY_ID,
                AppointmentInfo.COLUMN_NAME_COMPLETED
        };

        String selection =  AppointmentInfo.COLUMN_NAME_FAMILY_ID + "=?";
        String[] selectionArgs = {String.valueOf(familyId)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                AppointmentInfo.COLUMN_NAME_DATE + " DESC";

        Cursor c = getReadableDatabase().query(
                AppointmentInfo.TABLE_NAME,  // The table to query
                projection,                  // The columns to return
                selection,                   // The columns for the WHERE clause
                selectionArgs,               // The values for the WHERE clause
                null,                        // don't group the rows
                null,                        // don't filter by row groups
                sortOrder                    // The sort order
        );

        while(c.moveToNext()) {
            appointments.add(new FamilyContent.Appointment(
                    c.getString(0),
                    c.getString(1),
                    c.getString(2),
                    Integer.parseInt(c.getString(3)),
                    c.getInt(4)));

        }
        return appointments;
    }

    /*
     * Family Member Database Functions
     */
    public void putFamilyMember(FamilyContent.FamilyMember familyMember) {
        ContentValues values = new ContentValues();
        values.put(FamilyMemberInfo.COLUMN_NAME_ENTRY_ID, familyMember.getID());
        values.put(FamilyMemberInfo.COLUMN_NAME_FIRST_NAME, familyMember.getName());
        values.put(FamilyMemberInfo.COLUMN_NAME_FAMILY_ID, familyMember.getFamilyID());
        values.put(FamilyMemberInfo.COLUMN_NAME_PHONE_NUMBER, familyMember.getPhoneNumber());
        values.put(FamilyMemberInfo.COLUMN_NAME_BIRTHDAY, String.valueOf(familyMember.getBirthday()));

        getWritableDatabase().insert(FamilyMemberInfo.TABLE_NAME, null, values);
    }

    public boolean updateFamilyMember(FamilyContent.FamilyMember familyMember) {
        ContentValues values = new ContentValues();
        values.put(FamilyMemberInfo.COLUMN_NAME_ENTRY_ID, familyMember.getID());
        values.put(FamilyMemberInfo.COLUMN_NAME_FIRST_NAME, familyMember.getName());
        values.put(FamilyMemberInfo.COLUMN_NAME_FAMILY_ID, familyMember.getFamilyID());
        values.put(FamilyMemberInfo.COLUMN_NAME_PHONE_NUMBER, familyMember.getPhoneNumber());
        values.put(FamilyMemberInfo.COLUMN_NAME_BIRTHDAY, String.valueOf(familyMember.getBirthday()));

        return getWritableDatabase().update(FamilyMemberInfo.TABLE_NAME, values, FamilyMemberInfo.COLUMN_NAME_ENTRY_ID + "=" + familyMember.getID(), null) > 0;
    }

    public void deleteFamilyMember(FamilyContent.FamilyMember familyMember) {
        // Define 'where' part of query.
        String selection = FamilyMemberInfo.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { familyMember.getID() };
        // Issue SQL statement.
        getWritableDatabase().delete(FamilyMemberInfo.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getFamilyMember() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FamilyMemberInfo.COLUMN_NAME_ENTRY_ID,
                FamilyMemberInfo.COLUMN_NAME_FIRST_NAME,
                FamilyMemberInfo.COLUMN_NAME_FAMILY_ID,
                FamilyMemberInfo.COLUMN_NAME_PHONE_NUMBER,
                FamilyMemberInfo.COLUMN_NAME_BIRTHDAY
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FamilyMemberInfo.COLUMN_NAME_FIRST_NAME + " DESC";

        Cursor c = getReadableDatabase().query(
                FamilyMemberInfo.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return c;
    }

    private List<FamilyContent.FamilyMember> getAllFamilyMemberList(int familyId) {
        ArrayList<FamilyContent.FamilyMember> familyMembers = new ArrayList<>();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FamilyMemberInfo.COLUMN_NAME_ENTRY_ID,
                FamilyMemberInfo.COLUMN_NAME_FIRST_NAME,
                FamilyMemberInfo.COLUMN_NAME_FAMILY_ID,
                FamilyMemberInfo.COLUMN_NAME_PHONE_NUMBER,
                FamilyMemberInfo.COLUMN_NAME_BIRTHDAY
        };

        String selection =  FamilyMemberInfo.COLUMN_NAME_FAMILY_ID + "=?";
        String[] selectionArgs = {String.valueOf(familyId)};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FamilyMemberInfo.COLUMN_NAME_FIRST_NAME + " DESC";

        Cursor c = getReadableDatabase().query(
                FamilyMemberInfo.TABLE_NAME,  // The table to query
                projection,                  // The columns to return
                selection,
                selectionArgs,               // The values for the WHERE clause
                null,                        // don't group the rows
                null,                        // don't filter by row groups
                sortOrder                    // The sort order
        );

        while(c.moveToNext()) {
            familyMembers.add(new FamilyContent.FamilyMember (
                    c.getString(0),
                    c.getString(1),
                    Integer.parseInt(c.getString(2)),
                    null,
                    c.getString(3),
                    c.getString(4)
            ));
        }
        return familyMembers;

    }
    /*
     * Family Database Functions
     */

    public void putFamily(FamilyContent.Family family) {
        ContentValues values = new ContentValues();
        values.put(FamilyInfo.COLUMN_NAME_ENTRY_ID, family.getID());
        values.put(FamilyInfo.COLUMN_NAME_NAME, family.getFamilyName());
        values.put(FamilyInfo.COLUMN_NAME_PHONE_NUMBER, family.getPhoneNumber());
        values.put(FamilyInfo.COLUMN_NAME_EMAIL_ADDRESS, family.getEmailAddress());
        values.put(FamilyInfo.COLUMN_NAME_POSTAL_ADDRESS, family.getPostalAddress());

        getWritableDatabase().insert(FamilyInfo.TABLE_NAME, null, values);
    }
    public boolean updateFamily(FamilyContent.Family family) {
        ContentValues values = new ContentValues();
        values.put(FamilyInfo.COLUMN_NAME_ENTRY_ID, family.getID());
        values.put(FamilyInfo.COLUMN_NAME_NAME, family.getFamilyName());
        values.put(FamilyInfo.COLUMN_NAME_PHONE_NUMBER, family.getPhoneNumber());
        values.put(FamilyInfo.COLUMN_NAME_EMAIL_ADDRESS, family.getEmailAddress());
        values.put(FamilyInfo.COLUMN_NAME_POSTAL_ADDRESS, family.getPostalAddress());
        // TODO update appointments and family members
        Log.d("Database", family.getFamilyName() + " family updated");
        return getWritableDatabase().update(FamilyInfo.TABLE_NAME, values, FamilyInfo.COLUMN_NAME_ENTRY_ID + "=" + family.getID(), null) > 0;
    }

    public void deleteFamily(FamilyContent.Family family) {
        // Define 'where' part of query.
        String selection = FamilyInfo.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { family.getID() };
        // Issue SQL statement.
        SQLiteDatabase db = getWritableDatabase();
        db.delete(FamilyInfo.TABLE_NAME, selection, selectionArgs);
        selection = FamilyMemberInfo.COLUMN_NAME_FAMILY_ID + " LIKE ?";
        selectionArgs = new String[]{family.getID()};
        db.delete(FamilyMemberInfo.TABLE_NAME, selection, selectionArgs);

        selection = AppointmentInfo.COLUMN_NAME_FAMILY_ID + " LIKE ?";
        selectionArgs = new String[]{family.getID()};
        db.delete(AppointmentInfo.TABLE_NAME, selection, selectionArgs);
        Log.d("Database", family.getFamilyName() + " family deleted");
    }

    public List<FamilyContent.Family> getFamilyList() {
        ArrayList<FamilyContent.Family> families = new ArrayList<>();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FamilyInfo.COLUMN_NAME_ENTRY_ID,
                FamilyInfo.COLUMN_NAME_NAME,
                FamilyInfo.COLUMN_NAME_PHONE_NUMBER,
                FamilyInfo.COLUMN_NAME_EMAIL_ADDRESS,
                FamilyInfo.COLUMN_NAME_POSTAL_ADDRESS
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FamilyInfo.COLUMN_NAME_NAME + " DESC";

        Cursor c = getReadableDatabase().query(
                FamilyInfo.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        while (c.moveToNext()) {
            // get family
            FamilyContent.Family family = new FamilyContent.Family(c.getString(1));
            family.setID(c.getString(0));
            family.setPhoneNumber(c.getString(2));
            family.setEmailAddress(c.getString(3));
            family.setPostalAddress(c.getString(4));
            // get family members
            List<FamilyContent.FamilyMember> familyMembers = getAllFamilyMemberList(Integer.parseInt(family.getID()));
            for (FamilyContent.FamilyMember member :
                    familyMembers) {
                family.addMember(member);
            }
            // get family appointments
            List<FamilyContent.Appointment> appointments = getAllFamilyAppointmentList(Integer.parseInt(family.getID()));
            for (FamilyContent.Appointment appointment :
                    appointments) {
                family.addAppointment(appointment);
            }
            // add family
            families.add(family);
        }
        Log.d("Database", "Families gotten");
        return families;
    }


    public Cursor getFamily(int familyID) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FamilyInfo.COLUMN_NAME_ENTRY_ID,
                FamilyInfo.COLUMN_NAME_NAME,
                FamilyInfo.COLUMN_NAME_PHONE_NUMBER,
                FamilyInfo.COLUMN_NAME_EMAIL_ADDRESS,
                FamilyInfo.COLUMN_NAME_POSTAL_ADDRESS
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FamilyInfo.COLUMN_NAME_NAME + " DESC";

        Cursor c = getReadableDatabase().query(
                FamilyInfo.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        Log.d("Database"," family gotten");

        return c;
    }
}
