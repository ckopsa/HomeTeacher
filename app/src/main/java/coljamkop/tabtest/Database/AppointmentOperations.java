package coljamkop.tabtest.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

import coljamkop.tabtest.Content.FamilyContent;

import static coljamkop.tabtest.Database.TableData.*;
import static coljamkop.tabtest.Database.TableData.FamilyInfo;

/**
 * Created by Aghbac on 5/27/16.
 */
public class AppointmentOperations {
    SQLiteDatabase db;

    AppointmentOperations(SQLiteDatabase db) {
        this.db = db;
    }

    public void putAppointment(FamilyContent.Appointment appointment) {
        ContentValues values = new ContentValues();
        values.put(AppointmentInfo.COLUMN_NAME_ENTRY_ID, appointment.getID());
        values.put(AppointmentInfo.COLUMN_NAME_DATE, appointment.getDate());
        values.put(AppointmentInfo.COLUMN_NAME_TIME, appointment.getTime());
        values.put(AppointmentInfo.COLUMN_NAME_FAMILY, appointment.getFamily());
        values.put(AppointmentInfo.COLUMN_NAME_COMPLETED, appointment.getCompleted());

        db.insert(AppointmentInfo.TABLE_NAME, null, values);
    }
    public boolean updateAppointment(FamilyContent.Appointment appointment) {
        ContentValues values = new ContentValues();
        values.put(AppointmentInfo.COLUMN_NAME_ENTRY_ID, appointment.getID());
        values.put(AppointmentInfo.COLUMN_NAME_DATE, appointment.getDate());
        values.put(AppointmentInfo.COLUMN_NAME_TIME, appointment.getTime());
        values.put(AppointmentInfo.COLUMN_NAME_FAMILY, appointment.getFamily());
        values.put(AppointmentInfo.COLUMN_NAME_COMPLETED, appointment.getCompleted());

        return db.update(AppointmentInfo.TABLE_NAME, values, AppointmentInfo.COLUMN_NAME_ENTRY_ID + "=" + appointment.getID(), null) > 0;
    }

    public void deleteAppointment(FamilyContent.Appointment appointment) {
        // Define 'where' part of query.
        String selection = AppointmentInfo.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { appointment.getID() };
        // Issue SQL statement.
        db.delete(AppointmentInfo.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getAppointment() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
            AppointmentInfo.COLUMN_NAME_ENTRY_ID,
            AppointmentInfo.COLUMN_NAME_DATE,
            AppointmentInfo.COLUMN_NAME_TIME,
            AppointmentInfo.COLUMN_NAME_FAMILY,
            AppointmentInfo.COLUMN_NAME_COMPLETED
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
            AppointmentInfo.COLUMN_NAME_FAMILY + " DESC";

        Cursor c = db.query(
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

}
