package coljamkop.tabtest.Content;

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

    private static final int COUNT = 5;

    static {

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
        public Deque<Appointment> appointments;
        private String name;

        public Family(String familyName, String phoneNumber) {
            this.familyName = familyName;
            this.phoneNumber = phoneNumber;
            this.appointments = null;
        }

        @Override
        public String toString() {
            return familyName;
        }

        public void addAppointment(int year, int month, int day, int hourOfDay, int minute) {
            if (appointments == null) {
                appointments = new ArrayDeque<>();
                appointments.addFirst(new Appointment(year, month, day, hourOfDay, minute));
            } else {
                appointments.addFirst(new Appointment(year, month, day, hourOfDay, minute));
            }
        }

        public Appointment getNextAppointment() {
            if (appointments == null) {
                return null;
            } else {
                return appointments.getFirst();
            }
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public void setName(String name) {
            this.familyName = name;
        }
    }

    public static class Appointment {
        private int year;
        private int month;
        private int day;
        private int hourOfDay;
        private int minute;
        private boolean isPassed;

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
            return String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
        }
    }
}
