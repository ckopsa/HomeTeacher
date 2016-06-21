package coljamkop.tabtest.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

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
            + AppointmentInfo.COLUMN_NAME_FAMILY + " TEXT, "
            + AppointmentInfo.COLUMN_NAME_COMPLETED + " INTEGER);";

    private String SQL_CREATE_FAMILY_MEMBER_TABLE = "CREATE TABLE "
            + FamilyMemberInfo.TABLE_NAME
            + " ("
            + FamilyMemberInfo._ID + " INTEGER PRIMARY KEY, "
            + FamilyMemberInfo.COLUMN_NAME_ENTRY_ID + " TEXT, "
            + FamilyMemberInfo.COLUMN_NAME_FIRST_NAME + " TEXT, "
            + FamilyMemberInfo.COLUMN_NAME_LAST_NAME + " TEXT, "
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

    public AppointmentOperations appointmentOperations;
    public FamilyOperations familyOperations;
    public FamilyMemberOperations familyMemberOperations;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        appointmentOperations =  new AppointmentOperations(getWritableDatabase());
        familyMemberOperations =  new FamilyMemberOperations(getWritableDatabase());
        familyOperations =  new FamilyOperations(getWritableDatabase());
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

}
