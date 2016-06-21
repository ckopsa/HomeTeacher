package coljamkop.tabtest.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

import coljamkop.tabtest.Content.FamilyContent;

import static coljamkop.tabtest.Database.TableData.*;

/**
 * Created by Aghbac on 5/27/16.
 */
public class FamilyOperations {
    SQLiteDatabase db;

    FamilyOperations(SQLiteDatabase db) {
        this.db = db;
    }
    public void putFamily(FamilyContent.Family family) {
        ContentValues values = new ContentValues();
        values.put(FamilyInfo.COLUMN_NAME_ENTRY_ID, family.getID());
        values.put(FamilyInfo.COLUMN_NAME_NAME, family.getFamilyName());
        values.put(FamilyInfo.COLUMN_NAME_PHONE_NUMBER, family.getPhoneNumber());
        values.put(FamilyInfo.COLUMN_NAME_EMAIL_ADDRESS, family.getEmailAddress());
        values.put(FamilyInfo.COLUMN_NAME_POSTAL_ADDRESS, family.getPostalAddress());

        db.insert(FamilyInfo.TABLE_NAME, null, values);
    }
    public boolean updateFamily(FamilyContent.Family family) {
            ContentValues values = new ContentValues();
            values.put(FamilyInfo.COLUMN_NAME_ENTRY_ID, family.getID());
            values.put(FamilyInfo.COLUMN_NAME_NAME, family.getFamilyName());
            values.put(FamilyInfo.COLUMN_NAME_PHONE_NUMBER, family.getPhoneNumber());
            values.put(FamilyInfo.COLUMN_NAME_EMAIL_ADDRESS, family.getEmailAddress());
            values.put(FamilyInfo.COLUMN_NAME_POSTAL_ADDRESS, family.getPostalAddress());

            return db.update(FamilyInfo.TABLE_NAME, values, FamilyInfo.COLUMN_NAME_ENTRY_ID + "=" + family.getID(), null) > 0;
        }

    public void deleteFamily(FamilyContent.Family family) {
        // Define 'where' part of query.
        String selection = FamilyInfo.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { family.getID() };
        // Issue SQL statement.
        db.delete(FamilyInfo.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getFamily() {
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

        Cursor c = db.query(
            FamilyInfo.TABLE_NAME,  // The table to query
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
