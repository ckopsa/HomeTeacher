package coljamkop.tabtest.Database;

import android.content.ContentValues;
import android.content.Context;
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
public class FamilyMemberOperations {
    SQLiteDatabase db;

    FamilyMemberOperations(SQLiteDatabase db) {
        this.db = db;
    }

    public void putFamilyMember(FamilyContent.FamilyMember familyMember) {
        ContentValues values = new ContentValues();
        values.put(FamilyMemberInfo.COLUMN_NAME_ENTRY_ID, familyMember.getID());
        values.put(FamilyMemberInfo.COLUMN_NAME_FIRST_NAME, familyMember.getName());
        values.put(FamilyMemberInfo.COLUMN_NAME_LAST_NAME, familyMember.getLastName());
        values.put(FamilyMemberInfo.COLUMN_NAME_PHONE_NUMBER, familyMember.getPhoneNumber());
        values.put(FamilyMemberInfo.COLUMN_NAME_BIRTHDAY, String.valueOf(familyMember.getBirthday()));

        db.insert(FamilyMemberInfo.TABLE_NAME, null, values);
    }

    public boolean updateAppointment(FamilyContent.FamilyMember familyMember) {
        ContentValues values = new ContentValues();
        values.put(FamilyMemberInfo.COLUMN_NAME_ENTRY_ID, familyMember.getID());
        values.put(FamilyMemberInfo.COLUMN_NAME_FIRST_NAME, familyMember.getName());
        values.put(FamilyMemberInfo.COLUMN_NAME_LAST_NAME, familyMember.getLastName());
        values.put(FamilyMemberInfo.COLUMN_NAME_PHONE_NUMBER, familyMember.getPhoneNumber());
        values.put(FamilyMemberInfo.COLUMN_NAME_BIRTHDAY, String.valueOf(familyMember.getBirthday()));

        return db.update(FamilyMemberInfo.TABLE_NAME, values, FamilyMemberInfo.COLUMN_NAME_ENTRY_ID + "=" + familyMember.getID(), null) > 0;
    }

    public void deleteFamilyMember(FamilyContent.FamilyMember familyMember) {
        // Define 'where' part of query.
        String selection = FamilyMemberInfo.COLUMN_NAME_ENTRY_ID + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { familyMember.getID() };
        // Issue SQL statement.
        db.delete(FamilyMemberInfo.TABLE_NAME, selection, selectionArgs);
    }

    public Cursor getFamilyMember() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
            FamilyMemberInfo.COLUMN_NAME_ENTRY_ID,
            FamilyMemberInfo.COLUMN_NAME_FIRST_NAME,
            FamilyMemberInfo.COLUMN_NAME_LAST_NAME,
            FamilyMemberInfo.COLUMN_NAME_PHONE_NUMBER,
            FamilyMemberInfo.COLUMN_NAME_BIRTHDAY
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
            FamilyMemberInfo.COLUMN_NAME_FIRST_NAME + " DESC";

        Cursor c = db.query(
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
}
