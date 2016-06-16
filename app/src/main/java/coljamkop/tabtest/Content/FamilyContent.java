package coljamkop.tabtest.Content;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample familyName for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class FamilyContent implements Serializable {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Family> FAMILIES = new ArrayList<Family>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Family> FAMILY_MAP = new HashMap<String, Family>();

    static {
        Family fam = new Family("Kopsa");
        fam.setPhoneNumber("5305205087");
        fam.setPostalAddress("3909 Neal Road, Paradise, CA");
        fam.setEmailAddress("colton.kopsa@gmail.com");
        fam.addMember(new FamilyMember("Colton", null, null, null));
        fam.addMember(new FamilyMember("Kevin", null, null, null));
        fam.addMember(new FamilyMember("Carson", null, null, null));
        addFamily(fam);
        fam = new Family("Gerasymenko");
        fam.setPhoneNumber("5555555555");
        fam.setPostalAddress("Ukraine");
        fam.setEmailAddress("maxymax@gmail.com");
        fam.addMember(new FamilyMember("Max", null, null, null));
        addFamily(fam);
    }

    public static List<Appointment> getFamilysNextAppointment() {
        List<Appointment> temp = new ArrayList<>();
        for (Family family : FAMILIES) {
            temp.add(family.getNextAppointment());
        }
        return temp;
    }
    public static void addFamily(Family family) {
        FAMILIES.add(family);
        FAMILY_MAP.put(family.familyName, family);
    }

    public static void removeFamily(Family family) {
        FAMILIES.remove(family);
        FAMILY_MAP.remove(family.familyName);
    }

    public static String[] getFamilyNames() {
        List<String> familyNames = new ArrayList<String>();
        for(Family family : FAMILIES) {
            familyNames.add(family.familyName);
        }
        return familyNames.toArray(new String[familyNames.size()]);
    }

    /**
     * A dummy item representing a piece of familyName.
     */
    public static class Family implements Serializable {
        public String familyName;
        public String phoneNumber;
        public String emailAddress;
        public String postalAddress;
        public Deque<Appointment> appointments;
        public List<FamilyMember> familyMembers;

        public Family(String familyName) {
            this.familyName = familyName;
            this.familyMembers = new ArrayList<>();
            this.phoneNumber = null;
            this.emailAddress = null;
            this.postalAddress = null;
            this.appointments = null;
        }

        @Override
        public String toString() {
            return familyName;
        }

        /*
         * Family Member Methods
         */

        public void addMember(FamilyMember familyMember) {
            familyMembers.add(familyMember);
        }

        /*
         * Family Appointment Methods
         */
        public void addAppointment(int year, int month, int day, int hourOfDay, int minute) {
            if (appointments == null) {
                appointments = new ArrayDeque<>();
            }
            appointments.addFirst(new Appointment(year, month, day, hourOfDay, minute));
        }

        public Appointment getNextAppointment() {
            if (appointments == null) {
                return null;
            } else {
                return appointments.getFirst();
            }
        }

        /*
         * Setters & Getters
         */

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getPostalAddress() {
            return postalAddress;
        }

        public void setPostalAddress(String postalAddress) {
            this.postalAddress = postalAddress;
        }

        public String[] getMemberNameArray() {
            if (familyMembers != null) {
                List<String> stringMembers = new ArrayList<>();
                for (FamilyMember member :
                        familyMembers) {
                    stringMembers.add(member.name);
                }
                return stringMembers.toArray(new String[stringMembers.size()]);
            } else
                return null;
        }

        public List<FamilyMember> getMemberList() {
            return familyMembers;
        }
    }

    public static class Appointment {
        private int year;
        private int month;
        private int day;
        private int hourOfDay;
        private int minute;

        public Appointment(int year, int month, int day, int hourOfDay, int minute) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hourOfDay = hourOfDay;
            this.minute = minute;
        }

        public String getDate() {
            return String.valueOf(month) + "/" + String.valueOf(day) + "/" + String.valueOf(year);
        }

        public String getTime() {
            String time;
            // AM vs PM
            if (hourOfDay <= 12) {
                time = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
                // Ensures time doesn't look like 8:0 or 7:3
                if (minute == 0)
                    time += "0";
                time += " AM";
            }
            else {
                time = String.valueOf(hourOfDay - 12) + ":";
                // keep 11:03 from looking like 11:3
                if (minute  < 10)
                    time += "0";
                time += String.valueOf(minute);
                time += " PM";
            }
            return time;
        }
    }
    public static class FamilyMember implements Serializable {
        private String name;
        private String phoneNumber;
        private String email;
        private Date birthday;

        public FamilyMember(String name, @Nullable String phoneNumber, @Nullable String email, @Nullable Date birthday) {
            this.name = name;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.birthday = birthday;
        }

        public String getName() {
            return name;
        }
    }
}
