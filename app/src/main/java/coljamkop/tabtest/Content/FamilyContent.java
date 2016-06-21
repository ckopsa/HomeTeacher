package coljamkop.tabtest.Content;

import android.content.Context;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
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

//        Family fam = new Family("Kopsa");
//        fam.setPhoneNumber("5305205087");
//        fam.setPostalAddress("3909 Neal Road, Paradise, CA");
//        fam.setEmailAddress("colton.kopsa@gmail.com");
//        fam.addMember(new FamilyMember("Colton", null, null, null));
//        fam.addMember(new FamilyMember("Kevin", null, null, null));
//        fam.addMember(new FamilyMember("Carson", null, null, null));
//        addFamily(fam);
//        fam = new Family("Gerasymenko");
//        fam.setPhoneNumber("5555555555");
//        fam.setPostalAddress("Ukraine");
//        fam.setEmailAddress("maxymax@gmail.com");
//        fam.addMember(new FamilyMember("Max", null, null, null));
//        addFamily(fam);
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
        private int id;
        public String familyName;
        public String phoneNumber;
        public String emailAddress;
        public String postalAddress;
        public Deque<Appointment> appointments;
        public List<FamilyMember> familyMembers;

        public Family(String familyName) {
            this.id = System.identityHashCode(this);
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
            appointments.addFirst(new Appointment(year, month, day, hourOfDay, minute, familyName));
        }

        public Appointment getNextAppointment() {
            if (appointments == null || appointments.isEmpty()) {
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
            for (FamilyMember member :
                    getMemberList()) {
                member.setLastName(familyName);
            }
            for (Appointment appointment :
                    appointments) {
                appointment.family = familyName;
            }
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

        public boolean deleteNextAppointment() {
            if(getNextAppointment() != null) {
                appointments.pop();
                return true;
            }
            else
                return false;
        }

        public void addAppointment(String id, String date, String time, String family, int completed) {
            if (appointments == null) {
                appointments = new ArrayDeque<>();
            }
            appointments.addFirst(new Appointment(id, date, time, family, completed));
        }
        public String getID() {
            return String.valueOf(id);
        }

        public void setID(String id) {
            this.id = Integer.parseInt(id);
        }
    }

    public static class Appointment implements Serializable {
        private int id;
        private int year;
        private int month;
        private int day;
        private int hourOfDay;
        private int minute;
        private boolean completed;
        private String family;

        public Appointment(int year, int month, int day, int hourOfDay, int minute, String family) {
            this.id = System.identityHashCode(this);
            this.year = year;
            this.month = month;
            this.day = day;
            this.hourOfDay = hourOfDay;
            this.minute = minute;
            this.completed = false;
            this.family = family;
        }

        public Appointment(String id, String date, String time, String family, int completed) {
            this.id = Integer.parseInt(id);
            String[] dateArray = date.split("/");
            month = Integer.parseInt(dateArray[0]);
            day = Integer.parseInt(dateArray[1]);
            year = Integer.parseInt(dateArray[2]);

            String[] timeArray = time.split(":|\\ ");
            if (timeArray[2].equals("PM")) {
                if (!timeArray.equals("12")) {
                    this.hourOfDay = 12 + Integer.parseInt(timeArray[0]);
                    this.minute = Integer.parseInt(timeArray[1]);
                }
            } else {
                this.hourOfDay = Integer.parseInt(timeArray[0]);
                this.minute = Integer.parseInt(timeArray[1]);
            }
            this.family = family;
            this.completed = completed == 1;
        }

        public boolean toggleCompleted() {
            if (this.completed == false)
                return this.completed = true;
            else
                return this.completed = false;
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

        public boolean getCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public String getFamily() {
            return family;
        }

        public String getID() {
            return String.valueOf(id);
        }
    }
    public static class FamilyMember implements Serializable {
        private int id;
        private String name;
        private String lastName;
        private String phoneNumber;
        private String email;
        private String birthday;

        public FamilyMember(String name, String lastName, @Nullable String phoneNumber, @Nullable String email, @Nullable String birthday) {
            this.id = System.identityHashCode(this);
            this.name = name;
            this.lastName = lastName;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.birthday = birthday;
        }

        public FamilyMember(String id, String name, String lastName, @Nullable String phoneNumber, @Nullable String email, @Nullable String birthday) {
            this.id = Integer.parseInt(id);
            this.name = name;
            this.lastName = lastName;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.birthday = birthday;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getID() {
            return String.valueOf(id);
        }
    }
}
