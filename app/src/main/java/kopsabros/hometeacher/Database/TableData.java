package kopsabros.hometeacher.Database;

import android.provider.BaseColumns;

/**
 * Created by Aghbac on 6/19/16.
 */
public class TableData {
    // no accidental initialization
    public TableData() {
    }

    public static abstract class AppointmentInfo implements BaseColumns {
        // table name
        public static final String TABLE_NAME = "appointments";

        // name of different columns
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_FAMILY_ID = "familyid";
        public static final String COLUMN_NAME_COMPLETED = "completed";

    }

    public static abstract class FamilyInfo implements BaseColumns {
        // table name
        public static final String TABLE_NAME = "families";

        // name of different columns
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";
        public static final String COLUMN_NAME_EMAIL_ADDRESS = "email_address";
        public static final String COLUMN_NAME_POSTAL_ADDRESS = "postal_address";
    }

    public static abstract class FamilyMemberInfo implements BaseColumns {
        // table name
        public static final String TABLE_NAME = "family_members";

        // name of different columns
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_FAMILY_ID = "familyid";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";
        //public static final String COLUMN_NAME_EMAIL_ADDRESS = "phone_number";
        public static final String COLUMN_NAME_BIRTHDAY = "birthday";
    }
}
